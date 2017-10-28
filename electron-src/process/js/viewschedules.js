let ReactDOM = require('react-dom');
let React = require('react');

let ScheduleToolBar = require('./scheduletoolbar');
let ScheduleTable = require('./scheduletable');
console.log(ScheduleToolBar.toString());
class ViewSchedulePage  extends React.Component {
    render() {
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-4">
                        {/* <ScheduleToolbar /> */}
                    </div>

                    <div className="col-8">
                        <ScheduleTable />
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