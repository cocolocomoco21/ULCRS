let ReactDOM = require('react-dom');
let React = require('react');
let electron = eRequire('electron');
let ipc = electron.ipcRenderer;
let ScheduleToolbar = require('./scheduletoolbar');
let ScheduleTable = require('./scheduletable');
let fs = eRequire('fs');
let loadSchedules = JSON.parse(fs.readFileSync(scheLocation));

console.log(loadSchedules);
class ViewSchedulePage  extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : loadSchedules
        };

        this.exit = this.exit.bind(this);
    }

    exit(){
        ipc.send("kill-app");
    }

    render() {

        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-2">
                        <ScheduleToolbar />
                    </div>

                    <div className="col-8">
                        <ScheduleTable schedules={this.state.schedules}/>
                    </div>

                    <div className="col-2">
                        <div className="btn btn-danger" onClick={this.exit}> Exit </div>
                    </div>
                </div>
            </div>

        )
    }
}
//
// ReactDOM.render(
//     <ViewSchedulePage />,
//     document.getElementById("ViewSchedule")
// );

module.exports = ViewSchedulePage;