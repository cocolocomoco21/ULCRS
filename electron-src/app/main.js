var electron = require('electron');
var session = electron.session;
var BrowserWindow = electron.BrowserWindow;
var app = electron.app;
var ipc = electron.ipcMain;
let renderer = electron.ipcRenderer;
let net = electron.net;
var serverProcess = null;
let appWindow, viewTutorsWindow, authWindow = null;
let engrCookie = null;
let fetch = require("node-fetch");

app.on('ready', function () {
    appWindow = new BrowserWindow({
        width: 400,
        height: 300,
        show: false
    });

    //appWindow.loadURL('file://' + __dirname + '/viewschedules.html');
    appWindow.loadURL('file://' + __dirname + '/index.html');


    //appWindow.loadURL('http://www.wisc.edu');

    viewTutorsWindow = new BrowserWindow({
        width: 1600,
        height: 900,
        transparent: false,
        show: false,
        frame: false
    }); //infoWindow

    appWindow.once('ready-to-show', function () {
        appWindow.show();
    }); //ready-to-show

    // Start Java backend server
    // This currently (11/2) does not handle killing the server. `java.exe` must manually be
    // killed to function properly. You can also use the `jps` command to find the appropriate PID
    serverProcess = require('child_process').exec;

    let child = serverProcess('java -jar ../build/libs/ULCRS.jar');

    child.stdout.on('data', function (data) {
      console.log('Server stdout: ' + data);
    });

    child.stderr.on('data', function (data) {
      console.log('Server stderr: ' + data);
    });

    child.on('close', function (code) {
      child.stdin.pause();
      console.log('Server closing code: ' + code);
      console.log('Killed.............');
    });

    child.on('exit', function(code){

        console.log("ending");
        app.quit();
    });

    authWindow = new BrowserWindow({
        width: 900,
        height: 506,
        transparent: false,
        show: false,
        frame: false
    });

    app.on("will-quit", ()=>{
        console.log("In will quit");
        child.kill()
    });

    ipc.on("ShowViewTutor", function (event, args) {
        event.returnValue = '';
        appWindow.hide();
        authWindow.loadURL("http://dropin-dev.engr.wisc.edu");
        authWindow.once("ready-to-show", () => {
            authWindow.show();
        });
        // viewTutorsWindow.show();
        // appWindow.hide();
    });


    let redirect = function () {
        authWindow.close();
        viewTutorsWindow.loadURL('file://' + __dirname + '/viewtutors.html');
        viewTutorsWindow.once('ready-to-show', function () {
            viewTutorsWindow.show();
        });
    };


    let interval = setInterval(() => {
        session.defaultSession.cookies.get({domain: "dropin-dev.engr.wisc.edu"}, (error, cookies) => {
            if (error !== null) {
                console.log("ERROR *====");
                console.log(error);
                clearInterval(interval);
            }
            if (cookies.length !== 0) {
                engrCookie = cookies[0];
                console.log(typeof engrCookie);
                redirect();
                clearInterval(interval);
            }
        })
    }, 500);

    // hold the interval
    let polling_schedules = null;

    ipc.on("post_generate", function (event, args) {

        // fetch("http://localhost:4567/ulcrs/generate_schedules", {method: "POST"})
        //     .then(res => {event.sender.send("post_success");});

        // Need error handling
        console.log('preparing schedule data');
        //event.sender.send("receiveScheduleData", data)
        event.sender.send("post_success");

        // // set up time interval
        // polling_schedules = setInterval(()=>{
        //     fetch('http://localhost:4567/ulcrs/schedules')
        //         .then(res => res.txt)
        //         .then(data => {
        //             if (data !== "null") {
        //                 console.log("received data");
        //                 event.sender.send("receiveScheduleData", data);
        //                 clearInterval(polling_schedules);
        //             }
        //         });
        // }, 500);
        let data = "";

        setTimeout(()=> {event.sender.send("receiveScheduleData", data);}, 2000);
    });



    //child.kill();
    ipc.on("kill-app", (event,  args) =>{

        // child.kill();
        viewTutorsWindow.close();
        app.quit();
    });


    // change the api for receive actual data
    ipc.on("request_tutor_data", (event, args) => {
        let addCookieOption = {
            headers: {"Set-Cookie": [engrCookie.name + "="+ engrCookie.value]}
        };

        fetch('http://localhost:4567/ulcrs/tutor/', addCookieOption)
            .then(res => res.text())
            .then(body => event.sender.send("get_tutor_data", body));
        // });
    });
    ipc.on("request_course_data", (event, args) => {
        let addCookieOption = {
            headers: {"Set-Cookie": [engrCookie.name + "="+ engrCookie.value]}
        };
        fetch('http://localhost:4567/ulcrs/course/', addCookieOption)
            .then(res => res.text())
            .then(body => event.sender.send("get_course_data", body));

        // request.end();
    })
}); //app is ready
