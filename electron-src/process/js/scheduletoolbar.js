let React = require("react");
let temp = ["Schedule1", "Schedule2", "Schedule3", "Schedule4"];
var tabs= [];
var index = -1;

class ToolbarTab extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            index : props.index
        }
        this.tabClick = this.tabClick.bind(this);
        this.tabColor = this.tabColor.bind(this);
    }

    tabClick () {
        this.tabColor();
        this.tabFunction();
    }

    tabColor() {
        this.resetOther();
        var cardName = "card" + this.state.index;
        var textName = "text" + this.state.index;
        var card = document.getElementById(cardName);
        var text = document.getElementById(textName);
        card.style.backgroundColor = "#d9edf7";
        text.style.color = "#0055cc";
        index = this.state.index;
    }

    tabFunction() {
        //TODO
    }

    resetOther() {
        if (index === -1) {
            return;
        }
        var cardName = "card" + index;
        var textName = "text" + index;
        var card = document.getElementById(cardName);
        var text = document.getElementById(textName);
        card.style.backgroundColor = "#5bc0de";
        text.style.color = "#f7f7f7";
    }

    render() {
        let cardName = "card" + this.state.index;
        let textName = "text" + this.state.index;
        return (
            <div id = {cardName} className = "card-body" style={{backgroundColor: "#5bc0de"}} onClick={this.tabClick}>
                <h5 id = {textName} style={{color: "#f7f7f7"}}>
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
            <div id="schedule-tool-bar" className="card" style={{backgroundColor:"#5bc0de"}}>
                <h3 className="card-header" style={{color:"#f7f7f7"}}>
                    Schedules
                </h3>
                {tabs}
            </div>
        )
    }
}

module.exports = ScheduleToolbar;