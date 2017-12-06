let fs = require('fs');
let dataLocation = require('path').resolve(__dirname, '..', '..', 'data', 'mockdata.json');
let loadData = JSON.parse(fs.readFileSync(dataLocation));
let _ = require("lodash");

class Parser{

    constructor(){
        this.getTutors = this.getTutors.bind(this);
     }

    getTutors(tutorData){
        let tutors = [];
        for (let i in tutorData) {
            let t = tutorData[i];
            let tempTutor = {};
            tempTutor.id = t.id;
            tempTutor.firstName = t.firstName;
            tempTutor.lastName = t.lastName;
            tempTutor.tutorStatus = t.tutorStatus;
            let pref = t.tutorPreferences;

            let courses = pref.coursePreferences;
            if (courses.PREFER.length !== 0) {
                tempTutor.coursePreference = courses.PREFER.map((c)=>c.name);
            }else{
                tempTutor.coursePreference = "";
            }

            let days = pref.shiftPreferences;
            if (days.PREFER.length !== 0) {
                tempTutor.shiftPreference = days.PREFER[0].day;
            }
            let fre = pref.shiftFrequencyPreferences;
            if (fre.PREFER.length !== 0){
                tempTutor.shiftFrequency = fre.PREFER;
            }
            else{
                tempTutor.shiftPreference = "";
            }

            tutors.push(tempTutor);
        }
        return tutors;

    }

    getCourses(courseData) {
        let courses = [];
        for (let i in courseData) {
            let course = courseData[i];
            let tempCourse = {};
            tempCourse.id = course.id;
            tempCourse.name = course.name;
            let courseReq = course.courseRequirements;
            if (courseReq.requiredShifts.length !== 0) {
                tempCourse.requiredShifts = courseReq.requiredShifts[0].day;
            } else {
                tempCourse.requiredShifts = "";
            }
            tempCourse.requiredShiftAmount = courseReq.requiredShiftAmount;
            tempCourse.preferredShiftAmount = courseReq.preferredShiftAmount;
            tempCourse.intensity = courseReq.intensity;
            courses.push(tempCourse);
        }
        return courses;
    }

}

module.exports = Parser;