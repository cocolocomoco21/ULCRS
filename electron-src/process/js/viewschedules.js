let ReactDOM = require('react-dom');
let React = require('react');

let ScheduleToolBar = require('./scheduletoolbar');
let ScheduleTable = require('./scheduletable');
let fs = eRequire('fs');
let loadSchedules = JSON.parse(fs.readFileSync(dataLocation));

console.log(loadSchedules);
class ViewSchedulePage  extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : loadSchedules
        }
    }
    render() {

        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-2">
                        {/* <ScheduleToolbar /> */}
                    </div>

                    <div className="col-8">
                        <ScheduleTable schedules={this.state.schedules}/>
                    </div>

                    <div className="col-2">
                        
                    </div>
                </div>
            </div>

        )
    }
}

ReactDOM.render(
    <ViewSchedulePage />,
    document.getElementById("ViewSchedule")
);