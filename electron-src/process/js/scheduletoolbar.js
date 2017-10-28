let React = require("react");

class ScheduleToolbar extends React.Component {
    render(){
        return (
            <div id="schedule-tool-bar" className="card">
                <h3 className="card-header">
                    Schedules
                </h3>

                <div className = "card-body">
                    <div className="card-title"> hello </div>
                    <button type="button" className="btn btn-info"> Button</button>
                </div> {/*tool bar */}
            </div>
        )
    }
}

module.exports = ScheduleToolbar;