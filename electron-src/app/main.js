var electron = require('electron');
var BrowserWindow = electron.BrowserWindow;
var app = electron.app;

app.on('ready', function() {
  var appWindow, infoWindow;
  appWindow = new BrowserWindow({
    show: false
  }); //appWindow

  appWindow.loadURL('file://' + __dirname + '/index.html');
  //appWindow.loadURL('http://www.wisc.edu');

  infoWindow = new BrowserWindow({
    width: 400,
    height: 300,
    transparent: true,
    show: false,
    frame: false
  }); //infoWindow

  appWindow.once('ready-to-show', function() {
    appWindow.show();
  }); //ready-to-show

}); //app is ready
