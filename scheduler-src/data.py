import json


def read_tutors(tutor_file):
    with open(tutor_file, 'r') as f:
        raw_tutors = json.loads(f.read())
        tutors = [tutor for tutor in raw_tutors if tutor['tutorStatus'] == 'ACTIVE']
        return tutors


def read_courses(course_file):
    with open(course_file, 'r') as f:
        return json.loads(f.read())


def read_shifts(shift_file):
    with open(shift_file, 'r') as f:
        return json.loads(f.read())


def write_results(schedule_file, results):
    with open(schedule_file, 'w') as f:
        f.write(json.dumps(results, indent=2, separators=(',', ': ')))
        f.close()


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
    if 'PREFER' in tutor['tutorPreferences']['shiftFrequencyPreferences']:
        return int(tutor['tutorPreferences']['shiftFrequencyPreferences']['PREFER'])
    return 0


def get_willing_shift_freq(tutor):
    if 'WILLING' in tutor['tutorPreferences']['shiftFrequencyPreferences']:
        return int(tutor['tutorPreferences']['shiftFrequencyPreferences']['WILLING'])
    return 0


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
    for shift in course['courseRequirements']['numTutorsPerShift']:
        if str(day) == shift:
            return int(course['courseRequirements']['numTutorsPerShift'][shift])
    return 0


def get_preferred_tutor_amount(course, day):
    for shift in course['courseRequirements']['requiredShifts']:
        if shift['id'] == day:
            return int(shift['preferredTutorAmount'])
    return 0


def get_shift_id(shift):
    return shift['id']
