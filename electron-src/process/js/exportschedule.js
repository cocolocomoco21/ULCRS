let ReactDOM = require('react-dom');
let React = require('react');

let fs = eRequire('fs');
let loadSchedules = JSON.parse(fs.readFileSync(dataLocation));

class ExportSchedulePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            schedules : loadSchedules
        }
    }

    saveSession() {
        let save_session = document.getElementById("save-session");
        let upload_to_server = document.getElementById("upload-to-server");
        save_session.style.backgroundColor = "silver";
        upload_to_server.style.backgroundColor = "white";
    }
    uploadToServer() {
        let save_session = document.getElementById("save-session");
        let upload_to_server = document.getElementById("upload-to-server");
        save_session.style.backgroundColor = "white";
        upload_to_server.style.backgroundColor = "silver";
    }

    render() {
        return (
            <div className="container">
                <div className="row justify-content-center mt-md-4">
                    {/*<ScheduleTable schedules={this.state.schedules}/>*/}

                    <div className="form-group">
                        <input className="form-control" id="filename" placeholder="File Name" style={{width:"25rem"}}></input>
                    </div>
                </div>
                <div className="row justify-content-center mt-md-1">
                    <div className="card" style={{width: "10rem"}}>
                        <div className="list-group list-group-flush">
                            <div className="list-group-item" id="save-session" style={{backgroundColor:"white"}} onClick={this.saveSession}>
                                <p id="center">
                                    Save session
                                </p>
                            </div>
                            <div className="list-group-item" id="upload-to-server" style={{backgroundColor:"white"}} onClick={this.uploadToServer}>
                                <p id="center">
                                    Upload to server
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="row justify-content-center mt-md-4 mb-md-4">
                    <div className="col">
                        <button type="button" className="btn btn-block btn-outline-success" id="center" > Save </button>
                    </div>
                    <div className="col">
                        <button type="button" className="btn btn-block btn-outline-secondary" id="center" > Back </button>
                    </div>
                </div>
            </div>
        )
    }
}
//
//
// ReactDOM.render(
//     <ExportSchedulePage />,
//     document.getElementById("ExportSchedule")
// );

module.exports = ExportSchedulePage;