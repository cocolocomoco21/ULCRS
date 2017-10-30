let fs = eRequire('fs');
let loadData = JSON.parse(fs.readFileSync(dataLocation));
let _ = require("lodash");
let electron = eRequire('electron');

class Parser{

    constructor(data){
        this.data = data;
    }

    getCourses(){
        let courses = [];
        for (let i in this.data){
            let tutor = this.data[i] ;
            if (!tutor.tutorPreferences.hasOwnProperty("coursePreferences")){
                continue;
            }
            let coursePreference = tutor.tutorPreferences.coursePreferences;
            let preferCourses = coursePreference.PREFER;
            let willingCourses = coursePreference.WILLING;

            for (let index in preferCourses){
                let exists = false;
                let pc = preferCourses[index];
                for(let c in courses){
                    if (c.id == pc.id){
                        exists = true;
                    }
                }

                if (!exists){
                    courses.push(pc);
                }
            }

            for (let index in willingCourses){
                let exists = false;
                let wc = willingCourses[index];
                for(let c in courses){
                    if (c.id == wc.id){
                        exists = true;
                    }
                }

                if (!exists){
                    courses.push(wc);
                }
            }
        }
        return courses;
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
        })

        let wcs = _.map(willingCourses, function (c) {
            return c.name;
        })

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