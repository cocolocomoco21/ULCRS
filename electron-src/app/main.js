var electron = require('electron');

var BrowserWindow = electron.BrowserWindow;
var app = electron.app;
var ipc = electron.ipcMain;
let renderer = electron.ipcRenderer;

var serverProcess = null;
var appWindow, viewTutorsWindow, viewSchedulesWindow = null;
let fetch = require('electron-fetch');

app.on('ready', function() {
  appWindow = new BrowserWindow({
      width: 400,
      height: 300,
    show: false
  }); //appWindow

  //appWindow.loadURL('file://' + __dirname + '/viewschedules.html');
    appWindow.loadURL('file://' + __dirname + '/index.html');


    //appWindow.loadURL('http://www.wisc.edu');

  viewTutorsWindow = new BrowserWindow({
    width : 1600,
    height: 900,
      transparent: false,
    show: false,
    frame: true
  }); //infoWindow

  viewTutorsWindow.loadURL('file://' + __dirname + '/viewtutors.html');

  appWindow.once('ready-to-show', function() {
    appWindow.show();
  }); //ready-to-show

  // Start Java backend server
  // This currently (11/2) does not handle killing the server. `java.exe` must manually be 
  // killed to function properly. You can also use the `jps` command to find the appropriate PID
  serverProcess = require('child_process').exec;

  /*var child = serverProcess('java -jar ../build/libs/ULCRS.jar');

  child.stdout.on('data', function (data) {
    console.log('Server stdout: ' + data);
  });

  child.stderr.on('data', function (data) {
    console.log('Server stderr: ' + data);
  });

  child.on('close', function (code) {
    console.log('Server closing code: ' + code);
  });*/

  ipc.on("ShowViewTutor", function (event, args) {
      event.returnValue = '';
      viewTutorsWindow.show();
      appWindow.hide();
  });

  viewSchedulesWindow = new BrowserWindow({
      width : 1600,
      height: 900,
      transparent: false,
      show: false,
      frame: true
  });

  // viewSchedulesWindow.loadURL('file://' + __dirname + '/viewschedules.html')
    ipc.on("showViewSchedules", function (event, args) {
        // event.returnValue = '';
        // viewSchedulesWindow.show();
        // viewTutorsWindow.hide();
        let data = "";

        // Need error handling
        event.sender.send("receiveScheduleData", data)
    });

    setInterval(()=>{
        session.defaultSession.cookies.get({}, (error, cookies) => {
            console.log(error, cookies)
        })
    }, 1000);

  let send_schedule_data_not_null = (text, interval)=>{
        if (text !== "null"){
            clearInterval(interval);
            renderer.send("receiveScheduleData", text)
        }
      };

  let polling_schedules = ()=>{
      // let interval = setInterval( ()=>{
      //      fetch("http://localhost:4567/ulcrs/schedules/generate", {method: 'POST', body:""})
      //          .then(res=> send_schedule_data_not_null(res.text(), interval));
      // }, );
      setTimeout(()=> renderer.send("receiveScheduleData", ""),3000);
  };

  ipc.on("post_generate", (event, args)=>{

      // fetch("http://localhost:4567/ulcrs/schedules/generate", {method: 'POST', body:""})
      //     .then(res=>res.text())
      //     .then(res=>event.sender.send("post_success"));
      event.sender.send("post_success");
      polling_schedules();
  });

  // change the api for receive actual data
  ipc.on("request_tutor_data", (event, args) => {

      fetch('http://localhost:4567/ulcrs/tutor/')
          .then(res=> res.text())
          .then(body=> event.sender.send("get_tutor_data", body));
  });

  ipc.on("request_course_data", (event, args) =>{

      fetch('http://localhost:4567/ulcrs/course/')
          .then(res=> res.text())
          .then(body=> event.sender.send("get_course_data", body));

  })
}); //app is ready
