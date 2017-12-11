from ortools.constraint_solver import pywrapcp
import sys

import data

SOLUTION_WIDTH = 20
TRAILS = 10


def normalize_score(score, min_score, max_score):
    if max_score == min_score:
        return 5
    return round(float((max_score - score)) / (max_score - min_score) * 5, 2)


def main(tutor_file, course_file, shift_file, schedule_file, time_limit_in_second, solution_limit):
    # Creates the solver.
    solver = pywrapcp.Solver('schedule_shifts')

    tutors = data.read_tutors(tutor_file)
    courses = data.read_courses(course_file)
    shifts = data.read_shifts(shift_file)

    num_tutors = len(tutors)
    num_courses = len(courses)
    num_shifts = len(shifts)

    course_idx = {}
    for k in range(num_courses):
        course_idx[courses[k]['id']] = k

    # [START]
    # Create shift variables.
    schedule = {}
    for i in range(num_tutors):
        for j in range(num_shifts):
            for k in range(num_courses):
                schedule[(i, j, k)] = solver.BoolVar('tutor %d shift %d course %d' % (i, j, k))
    schedule_flat = [schedule[(i, j, k)] for i in range(num_tutors) for j in range(num_shifts) for k in
                     range(num_courses)]

    # scoring expression
    score = solver.IntVar(0, 10000000, 'score')

    # tutor only tutors certain courses
    tutor_course_scores = []
    for i in range(num_tutors):
        tutor = tutors[i]
        prefer_courses = data.get_prefer_courses(tutor)
        willing_courses = data.get_willing_courses(tutor)
        for j in range(num_shifts):
            for k in range(num_courses):
                if courses[k]['id'] not in prefer_courses + willing_courses:
                    solver.Add(schedule[(i, j, k)] == False)
        if willing_courses:
            tutor_course_scores.append(sum([schedule[(i, j, course_idx[course_id])] for j in range(num_shifts)
                                            for course_id in willing_courses if course_id in course_idx]))

    # tutor only tutors on certain shifts
    tutor_shift_scores = []
    for i in range(num_tutors):
        tutor = tutors[i]
        prefer_shifts = data.get_prefer_shifts(tutor)
        willing_shifts = data.get_willing_shifts(tutor)
        for j in range(num_shifts):
            shift = data.get_shift_id(shifts[j])
            for k in range(num_courses):
                if shift not in prefer_shifts + willing_shifts:
                    solver.Add(schedule[(i, j, k)] == False)
            if willing_shifts:
                tutor_shift_scores.append(sum([schedule[(i, j, course_idx[course_id])] for j in range(num_shifts)
                                               for course_id in willing_courses]))

    # tutor has max shift number
    tutor_shift_freq_scores = []
    for i in range(num_tutors):
        tutor = tutors[i]
        prefer_freq = data.get_prefer_shift_freq(tutor)
        willing_freq = data.get_willing_shift_freq(tutor)
        tutor_shifts = []
        for j in range(num_shifts):
            tutor_shift = solver.BoolVar('tutor %d on shift %d' % (i, j))
            solver.Add(tutor_shift == (sum([schedule[(i, j, k)] for k in range(num_courses)]) > 0))
            tutor_shifts.append(tutor_shift)
        solver.Add(sum(tutor_shifts) <= willing_freq)
        tutor_shift_freq_scores.append(solver.Max(sum(tutor_shifts) - prefer_freq, 0))

    # course has enough shifts in week
    course_shift_scores = []
    for k in range(num_courses):
        course = courses[k]
        course_shifts = []
        for j in range(num_shifts):
            course_shift = solver.BoolVar('course %d on shift %d' % (k, j))
            solver.Add(course_shift == (sum([schedule[(i, j, k)] for i in range(num_tutors)]) > 0))
            course_shifts.append(course_shift)
        # solver.Add(sum(course_shifts) >= data.get_required_shift_amount(course))
        course_shift_scores.append(solver.Max(data.get_required_shift_amount(course) - sum(course_shifts), 0) * 100)
        course_shift_scores.append(solver.Max(data.get_preferred_shift_amount(course) - sum(course_shifts), 0))

    # tutor has maximum course intensity limit
    tutor_intensity_scores = []
    for i in range(num_tutors):
        for j in range(num_shifts):
            course_intensities = []
            for k in range(num_courses):
                course = courses[k]
                intensity_str = data.get_intensity(course)
                intensity = 0
                if intensity_str == 'HIGH':
                    intensity = 6
                elif intensity_str == 'MEDIUM':
                    intensity = 3
                elif intensity_str == 'LOW':
                    intensity = 1
                course_intensity = solver.IntVar(0, 7, 'tutor %d shift %d course %d intensity' % (i, j, k))
                solver.Add(course_intensity == intensity * schedule[(i, j, k)])
                course_intensities.append(course_intensity)
        solver.Add(sum(course_intensities) <= 6)
        tutor_intensity_scores.append(6 - sum(course_intensities))

    # course has enough tutor on required shifts
    course_tutor_amount_scores = []
    for j in range(num_shifts):
        shift = data.get_shift_id(shifts[j])
        for k in range(num_courses):
            course = courses[k]
            if shift in data.get_required_shifts(course):
                tutor_num = sum([schedule[(i, j, k)] for i in range(num_tutors)])
                # solver.Add(tutor_num >= data.get_required_tutor_amount(course, shift))
                course_tutor_amount_scores.append(
                    solver.Max(data.get_required_tutor_amount(course, shift) - tutor_num, 0) * 100)

    solver.Add(score == sum(tutor_course_scores)
               + sum(tutor_shift_freq_scores)
               + sum(course_shift_scores)
               + sum(tutor_intensity_scores)
               + sum(course_tutor_amount_scores))
    objective = solver.Minimize(score, 1)

    # Create the decision builder.
    db = solver.Phase(schedule_flat, solver.CHOOSE_FIRST_UNBOUND,
                      solver.ASSIGN_RANDOM_VALUE)

    # Create the solution collector.
    collector = solver.AllSolutionCollector()
    collector.Add(schedule_flat)
    collector.Add(score)
    collector.AddObjective(score)

    time_limit = solver.TimeLimit(time_limit_in_second * 1000)

    if solver.Solve(db, [objective, collector, time_limit]):
        solution_count = collector.SolutionCount()
        print 'Solutions found:', solution_count, '\n'

        best_solution = collector.SolutionCount() - 1

        print 'C\Shift'.ljust(SOLUTION_WIDTH),
        for j in range(num_shifts):
            print str(data.get_shift_id(shifts[j])).ljust(SOLUTION_WIDTH),
        print

        for k in range(num_courses):
            course = courses[k]
            print str(course['name']).ljust(SOLUTION_WIDTH),
            for j in range(num_shifts):
                assignment = []
                for i in range(num_tutors):
                    if collector.Value(best_solution, schedule[(i, j, k)]):
                        # assignment.append(str(tutors[i]['id']))
                        assignment.append(tutors[i]['firstName'][0] + tutors[i]['lastName'][0])
                print '/'.join(assignment).ljust(SOLUTION_WIDTH),
            print
        print 'Score:', collector.Value(best_solution, score)
    else:
        print 'No solution found.'
        data.write_results(schedule_file, [])
        return
    print

    best_score = collector.Value(best_solution, score)
    worst_score_in_top_solutions = collector.Value(max(collector.SolutionCount() - solution_limit, 0), score)
    worst_score_in_top_solutions_2 = collector.Value(max(collector.SolutionCount() - solution_limit * 2, 0), score)
    worst_score = collector.Value(0, score)

    print 'Best score:', best_score
    print 'Worst score in top solutions', worst_score_in_top_solutions
    print 'Worst score in top 2 times solutions', worst_score_in_top_solutions_2
    print 'Worst score:', worst_score

    print 'Best solutions:'
    results = []
    for solution in reversed(range(max(collector.SolutionCount() - solution_limit, 0), collector.SolutionCount())):
        print int(collector.Value(solution, score))
        result = {
            'rating': normalize_score(int(collector.Value(solution, score)), best_score, worst_score_in_top_solutions_2),
            'scheduledShifts': []
        }
        for j in range(num_shifts):
            result_shift = {
                'shift': {
                    'id': shifts[j]['id'],
                    'day': shifts[j]['day']
                },
                'assignments': []
            }
            for i in range(num_tutors):
                result_assignment = {
                    'tutor': {
                        'id': tutors[i]['id'],
                        'firstName': tutors[i]['firstName'],
                        'lastName': tutors[i]['lastName']
                    },
                    'courses': []
                }
                for k in range(num_courses):
                    if collector.Value(solution, schedule[(i, j, k)]):
                        result_assignment['courses'].append({
                            'id': courses[k]['id'],
                            'name': courses[k]['name']
                        })
                if len(result_assignment['courses']) != 0:
                    result_shift['assignments'].append(result_assignment)
            if len(result_shift['assignments']) != 0:
                result['scheduledShifts'].append(result_shift)
        if len(result['scheduledShifts']) != 0:
            results.append(result)
    data.write_results(schedule_file, results)


if __name__ == '__main__':
    if len(sys.argv) != 7:
        print 'Usage: python scheduler.py' \
              ' <tutor file> <course file> <shift file> <time limit in second> <solution limit>'
    main(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], int(sys.argv[5]), int(sys.argv[6]))
