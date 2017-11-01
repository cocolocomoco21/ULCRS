let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');
let electron = eRequire('electron');
let ipc = electron.ipcRenderer;

class StartPage extends React.Component{
    ShowViewTutor(){
        ipc.sendSync("ShowViewTutor")
    }

    render(){
        return (
        <div className="container-fluid">

            <div className="row justify-content-center">
                <div className="col">
                    <div className="row justify-content-center">
                        <div style={{fontSize:42}}>
                            <strong>ULCRS</strong>
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div style={{fontSize:15}}>
                            Welcome to ULCRS! What do you want to do today?
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div>
                            <button type="button" className="btn btn-info" onClick={this.ShowViewTutor} > Load data from server </button>
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div>
                            <button type="button" className="btn btn-info">Resume a saved session</button>
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
