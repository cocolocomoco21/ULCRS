var electron = require('electron');

var BrowserWindow = electron.BrowserWindow;
var app = electron.app;
var ipc = electron.ipcMain;


app.on('ready', function() {
  var appWindow, viewTutorsWindow, viewSchedulesWindow;
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

  viewSchedulesWindow.loadURL('file://' + __dirname + '/viewschedules.html')

    ipc.on("ShowViewSchedules", function (event, args) {
        event.returnValue = '';
        viewSchedulesWindow.show();
        viewTutorsWindow.hide();
    });

    ipc.on("ShowExportSchedule", function (event, args) {
        viewExportScheduleWindow = new BrowserWindow({
            width : 1000,
            height: 900,
            transparent: false,
            show: false,
            frame: true
        });
        viewExportScheduleWindow.loadURL('file://' + __dirname + '/exportSchedule.html')
        event.returnValue = '';
        viewExportScheduleWindow.show();
    });

  // change the api for receive actual data
  ipc.on("request_course_data", (event, args) => {
      let fetch = require('electron-fetch');
      fetch('http://localhost:4567/ulcrs/tutor/')
          .then(res=> res.text())
          .then(body=> event.sender.send("get_tutor_data", body));
  });

  ipc.on("request_tutor_data", (event, args) =>{
      let fetch = require('electron-fetch');
      fetch('http://localhost:4567/ulcrs/course/')
          .then(res=> res.text())
          .then(body=> event.sender.send("get_course_data", body));

  })
}); //app is ready
