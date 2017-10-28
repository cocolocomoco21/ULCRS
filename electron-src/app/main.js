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
  });

  viewSchedulesWindow = new BrowserWindow({
      transparent: false,
      show: false,
      frame: true
  });

  viewSchedulesWindow.loadURL('file://' + __dirname + '/viewschedules.html')

    ipc.on("ShowViewSchedules", function (event, args) {
        event.returnValue = '';
        viewSchedulesWindow.show();
    });
}); //app is ready
