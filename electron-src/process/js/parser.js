let fs = require('fs');
let dataLocation = require('path').resolve(__dirname, '..', '..', 'data', 'mockdata.json');
let loadData = JSON.parse(fs.readFileSync(dataLocation));
let _ = require("lodash");
let electron = require('electron');


class Parser{

    constructor(){
        this.getTutors = this.getTutors.bind(this);
        this.getTutorCoursePrefNames = this.getTutorCoursePrefNames.bind(this);
        this.getTutorShiftPrefDay = this.getTutorShiftPrefDay.bind(this);
        this.getTutorShiftFreqPref = this.getTutorShiftFreqPref.bind(this);
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
            tempCourse.requiredShifts = courseReq.requiredShifts[0].day;
            tempCourse.requiredShiftAmount = courseReq.requiredShiftAmount;
            tempCourse.preferredShiftAmount = courseReq.preferredShiftAmount;
            tempCourse.intensity = courseReq.intensity;
            courses.push(tempCourse);
        }
        return courses;
    }


    getTutorShiftFreqPref(index){
        let tutorPre = this.data[index.toString()].tutorPreferences;
        if (!tutorPre.hasOwnProperty("shiftFrequencyPreferences")){
            return {"PREFER":[], "WILLING": []};
        }
        let shiftFrePref = tutorPre.shiftFrequencyPreferences;
        let num = shiftFrePref.PREFER;
        return {"PREFER":num};

    }

    getTutorCoursePrefNames(index){

        let tutorPre = this.data[index.toString()].tutorPreferences;
        if (!tutorPre.hasOwnProperty("coursePreferences")){
            return {"PREFER":[], "WILLING": []};
        }
        let coursePreference = tutorPre.coursePreferences;
        let preferCourses = coursePreference.PREFER;
        let willingCourses = coursePreference.WILLING;

        let pcs = _.map(preferCourses, function (c) {
            return c.name;
        });

        let wcs = _.map(willingCourses, function (c) {
            return c.name;
        });

        return {"PREFER":pcs, "WILLING": wcs};
    }

    getTutorShiftPrefDay(index){
        let tutor = this.data[index.toString()];
        let tutorPre = tutor.tutorPreferences;
        if (!tutorPre.hasOwnProperty("shiftPreferences")){
            return {"PREFER":[], "WILLING": []};
        }
        let shiftPreference = tutorPre.shiftPreferences;
        let preferShifts = shiftPreference.PREFER;
        let willingShifts = shiftPreference.WILLING;


        let pss = _.map(preferShifts, function (s) {
            return s.day;
        });

        let wss = _.map(willingShifts, function (s) {
            return s.day;
        });

        return {"PREFER":pss, "WILLING": wss};
    }
}


let ReactDOM = require('react-dom');
let React = require('react');
let Client = require('node-rest-client').Client;
let ipc = electron.ipcRenderer;

module.exports = Parser;