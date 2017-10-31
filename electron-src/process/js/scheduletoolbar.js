let React = require("react");
let temp = ["Schedule1", "Schedule2", "Schedule3", "Schedule4"];
var tabs= [];

class ToolbarTab extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            index : props.index
        }
    }
    render() {
        return (
            <div id = "card1" type = "button" className = "card-body">
                <h5>
                    Schedule {this.state.index}
                </h5>
            </div>
        )
    }
}

class ScheduleToolbar extends React.Component {
    constructor(props) {
        super(props);
        for(let i = 0; i < temp.length; i++) {
           tabs.push(<ToolbarTab index={i+1} key={i}/>);
        }
    }

    render(){
        return (
            <div id="schedule-tool-bar" className="card">
                <h3 className="card-header">
                    Schedules
                </h3>
                {tabs}
            </div>
        )
    }
}

module.exports = ScheduleToolbar;