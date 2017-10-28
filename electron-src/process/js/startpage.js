let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');
let electron = eRequire('electron');
let ipc = electron.ipcRenderer;

class StartPage extends React.Component{
    ShowViewTutor(){
        ipc.sendSync("ShowViewTutor")
    }
    ShowViewSchedules(){
        ipc.sendSync("ShowViewSchedules")
    }

    render(){
        return (
        <div className="container-fluid">

            <div className="row justify-content-center">
                <div className="col-4">
                    <div className="row">
                        <div style={{font:"23"}}>
                            ULCRS
                        </div>
                    </div>
                    <div className="row">
                        <div>
                            <button type="button" className="btn btn-info" onClick={this.ShowViewTutor} > Show view tutor </button>
                        </div>
                    </div>

                    <div className="row">
                        <div>
                            <button type="button" className="btn btn-info" onClick={this.ShowViewSchedules}> Show view Schedules </button>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        )
    }
}
ReactDOM.render( <StartPage/> ,
    document.getElementById("Start") );
