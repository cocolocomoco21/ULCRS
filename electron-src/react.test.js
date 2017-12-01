let React = require('react');
let renderer = require('react-test-renderer');
let path = require('path');
let spectron = require('spectron');
let assert = require('assert');
let Application = require('spectron').Application;
let main_path = path.resolve(__dirname,'app/main.js');
let Parser = require(path.resolve(__dirname, 'process/js/parser.js'));
let fs = require('fs');

describe('application launch', function () {
    let app = undefined;
    beforeEach(function () {
        app = new Application({
            path: path.resolve(__dirname,'node_modules/.bin/electron'),
            args: [main_path]
        });
        return app.start()
    });

    afterEach(function () {
        if (app && app.isRunning()) {
            return app.stop()
        }
    });

    it('shows a correct window', function () {
        app.client.getWindowCount().then(function (count) {
            assert.equal(count, 1)
        });
        // No selected text
        app.client.getSelectedText().then(function (str) {
            assert.equal(str, "")
        })
    });

});

describe('parser test', () => {
    let dataPath = path.resolve(__dirname,'data/mockTutorData.json');
    let mockTutorData = null;
    let p = null;
    beforeEach(() => {
        p = new Parser();
    });

    it('parse tutor correctly', () =>{
        let mockTutorData = JSON.parse(fs.readFileSync(dataPath));
        let tutors = p.getTutors(mockTutorData);
        assert.equal(tutors.length, 5)
    });

    it('parse course correctly', ()=>{
        let mockCourseData = JSON.parse(fs.readFileSync(path.resolve(__dirname,'data/mockCourseData.json')));
        let courses = p.getCourses(mockCourseData);
        assert.equal(courses.length, 5)
    })

    it('parse tutor with empty data correctly', ()=>{
        let mockTutorData = JSON.parse(fs.readFileSync(path.resolve(__dirname,'data/mockTutorData2.json')));
        let tutors = p.getTutors(mockTutorData);
        assert.equal(tutors.length, 10)
    })
});