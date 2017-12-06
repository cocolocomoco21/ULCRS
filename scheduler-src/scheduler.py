from ortools.constraint_solver import pywrapcp

import data

SOLUTION_WIDTH = 7
TRAILS = 10


def main():
    # Creates the solver.
    solver = pywrapcp.Solver('schedule_shifts')

    tutors = data.get_tutors()
    courses = data.get_courses()

    num_tutors = len(tutors)
    num_courses = len(courses)
    num_days = 5

    # [START]
    # Create shift variables.
    shifts = {}
    for i in range(num_tutors):
        for j in range(num_days):
            for k in range(num_courses):
                shifts[(i, j, k)] = solver.BoolVar('tutor %d day %d course %d' % (i, j, k))
    shifts_flat = [shifts[(i, j, k)] for i in range(num_tutors) for j in range(num_days) for k in range(num_courses)]

    # scoring expression
    score = solver.IntVar(0, 1000000, 'score')

    # tutor only tutors certain courses
    tutor_course_scores = []
    for i in range(num_tutors):
        tutor = tutors[i]
        prefer_courses = data.get_prefer_courses(tutor)
        willing_courses = data.get_willing_courses(tutor)
        for j in range(num_days):
            for k in range(num_courses):
                if courses[k]['id'] not in prefer_courses + willing_courses:
                    solver.Add(shifts[(i, j, k)] == False)
        if willing_courses:
            tutor_course_scores.append(sum([shifts[(i, j, k)] for j in range(num_days) for k in willing_courses]))

    # tutor only tutors on certain days
    tutor_day_scores = []
    for i in range(num_tutors):
        tutor = tutors[i]
        prefer_shifts = data.get_prefer_shifts(tutor)
        willing_shifts = data.get_willing_shifts(tutor)
        for j in range(num_days):
            day = j
            for k in range(num_courses):
                if day not in prefer_shifts + willing_shifts:
                    solver.Add(shifts[(i, j, k)] == False)
            if willing_shifts:
                tutor_day_scores.append(sum([shifts[(i, j, k)] for j in range(num_days) for k in willing_courses]))

    # tutor has max shift number
    tutor_shift_scores = []
    for i in range(num_tutors):
        tutor = tutors[i]
        prefer_freq = data.get_prefer_shift_freq(tutor)
        willing_freq = data.get_willing_shift_freq(tutor)
        tutor_shifts = []
        for j in range(num_days):
            tutor_shift = solver.BoolVar('tutor %d on day %d' % (i, j))
            solver.Add(tutor_shift == (sum([shifts[(i, j, k)] for k in range(num_courses)]) > 0))
            tutor_shifts.append(tutor_shift)
        solver.Add(sum(tutor_shifts) < willing_freq)
        tutor_shift_scores.append(abs(sum(tutor_shifts) - prefer_freq))

    # course has enough amounts in week
    course_shift_scores = []
    for k in range(num_courses):
        course = courses[k]
        course_days = 0
        for j in range(num_days):
            day = j
            if sum([shifts[(i, j, k)] for i in range(num_tutors)]) > 0:
                course_days += 1
        solver.Add(course_days >= data.get_required_shift_amount(course))
        course_shift_scores.append(max(data.get_preferred_shift_amount(course) - course_days, 0))

    # tutor has maximum course intensity limit
    tutor_intensity_scores = []
    for i in range(num_tutors):
        tutor_intensity = 0
        for j in range(num_days):
            for k in range(num_courses):
                course = courses[k]
                if shifts[(i, j, k)]:
                    course_intensity = 0
                    intensity_str = data.get_intensity(course)
                    if intensity_str == 'HIGH':
                        course_intensity = 6
                    elif intensity_str == 'MEDIUM':
                        course_intensity = 3
                    elif intensity_str == 'LOW':
                        course_intensity = 1
                    tutor_intensity += course_intensity
        solver.Add(tutor_intensity <= 6)
        tutor_intensity_scores.append(6 - tutor_intensity)

    # course has enough tutor on required days
    for j in range(num_days):
        day = j
        for k in range(num_courses):
            course = courses[k]
            if day in data.get_required_shifts(course):
                # TODO: Add required tutor amount for each required day for courses
                solver.Add(sum([shifts[(i, j, k)] for i in range(num_tutors)]) >= 1)

    solver.Add(score == sum(tutor_course_scores)
               + sum(tutor_shift_scores)
               + sum(course_shift_scores)
               + sum(tutor_intensity_scores))
    objective = solver.Minimize(score, 1)

    # Create the decision builder.
    db = solver.Phase(shifts_flat, solver.CHOOSE_FIRST_UNBOUND,
                      solver.ASSIGN_RANDOM_VALUE)

    # Create the solution collector.
    collector = solver.AllSolutionCollector()
    collector.Add(shifts_flat)
    collector.Add(score)
    collector.AddObjective(score)

    if solver.Solve(db, [objective, collector]):
        solution_count = collector.SolutionCount()
        print "Solutions found:", solution_count, "\n"

        best_solution = collector.SolutionCount() - 1

        print 'C\Day'.ljust(SOLUTION_WIDTH),
        for day in range(num_days):
            print str(day).ljust(SOLUTION_WIDTH),
        print

        for k in range(num_courses):
            course = courses[k]
            print str(course['id']).ljust(SOLUTION_WIDTH),
            for j in range(num_days):
                assignment = []
                for i in range(num_tutors):
                    if collector.Value(best_solution, shifts[(i, j, k)]):
                        assignment.append(str(tutors[i]['id']))
                print ''.join(assignment).ljust(SOLUTION_WIDTH),
            print
        print 'Score:', collector.Value(best_solution, score)

    # for solution in range(collector.SolutionCount()):
    #     print collector.Value(solution, score)


if __name__ == '__main__':
    main()
