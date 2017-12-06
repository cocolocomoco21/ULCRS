import json


def get_tutors():
    with open('resources/mockTutors_FrontendExpects.json', 'r') as f:
        raw_tutors = json.loads(f.read())
        tutors = [tutor for tutor in raw_tutors if tutor['tutorStatus'] == 'ACTIVE']
        return tutors


def get_courses():
    with open('resources/mockCourses_FrontendExpects.json', 'r') as f:
        return json.loads(f.read())


def get_prefer_courses(tutor):
    prefer_courses = []
    for course in tutor['tutorPreferences']['coursePreferences']['PREFER']:
        prefer_courses.append(int(course['id']))
    return prefer_courses


def get_willing_courses(tutor):
    willing_courses = []
    for course in tutor['tutorPreferences']['coursePreferences']['WILLING']:
        willing_courses.append(int(course['id']))
    return willing_courses


def get_prefer_shifts(tutor):
    prefer_shifts = []
    for shift in tutor['tutorPreferences']['shiftPreferences']['PREFER']:
        prefer_shifts.append(int(shift['id']))
    return prefer_shifts


def get_willing_shifts(tutor):
    willing_shifts = []
    for shift in tutor['tutorPreferences']['shiftPreferences']['WILLING']:
        willing_shifts.append(int(shift['id']))
    return willing_shifts


def get_prefer_shift_freq(tutor):
    return int(tutor['tutorPreferences']['shiftFrequencyPreferences']['PREFER'])


def get_willing_shift_freq(tutor):
    return int(tutor['tutorPreferences']['shiftFrequencyPreferences']['WILLING'])


def get_required_shifts(course):
    required_shifts = []
    for shift in course['courseRequirements']['requiredShifts']:
        required_shifts.append(int(shift['id']))
    return required_shifts


def get_required_shift_amount(course):
    return int(course['courseRequirements']['requiredShiftAmount'])


def get_preferred_shift_amount(course):
    return int(course['courseRequirements']['preferredShiftAmount'])


def get_intensity(course):
    return course['courseRequirements']['intensity']


def get_required_tutor_amount(course, day):
    for shift in course['courseRequirements']['requiredShifts']:
        if shift['id'] == day:
            return int(shift['requiredTutorAmount'])
    return 0


def get_preferred_tutor_amount(course, day):
    for shift in course['courseRequirements']['requiredShifts']:
        if shift['id'] == day:
            return int(shift['preferredTutorAmount'])
    return 0
