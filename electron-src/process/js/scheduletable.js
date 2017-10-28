let ReactDOM = require('react-dom');
let React = require('react');

class ScheduleTable extends React.Component {

    // Put your code here.
    render() {
        return (

            <div className="card card-inverse" style={{background: "#00ffff", border: "#333"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Schedule: schedule 1
                    </h3>

                    <div className = "card-body">
                        <div className="card-title"> Your schedules </div>
                    </div> {/*tool bar */}
                </div>
            </div>
        )
    }
}

module.exports = ScheduleTable;