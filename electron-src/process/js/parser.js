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

            tempTutor.coursePreference = courses.PREFER.map((c)=>c.name);
            tempTutor.courseWilling = _.map(courses.WILLING, (c)=>c.name);

            let days = pref.shiftPreferences;


            tempTutor.shiftPreference = _.map(days.PREFER, (d)=>d.day);
            tempTutor.shiftWilling = _.map(days.WILLING, (d)=>d.day);

            let fre = pref.shiftFrequencyPreferences;

            tempTutor.shiftAmountPreferred = fre.PREFER;
            tempTutor.shiftAmountWilling = fre.WILLING;

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

            tempCourse.requiredShifts = _.map(courseReq.requiredShifts, (s)=>s.day);
            tempCourse.requiredShiftAmount = courseReq.requiredShiftAmount;
            tempCourse.preferredShiftAmount = courseReq.preferredShiftAmount;
            tempCourse.intensity = courseReq.intensity;
            courses.push(tempCourse);
        }
        return courses;
    }

}

module.exports = Parser;