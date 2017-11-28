let React = require("react");
//let temp = ["Schedule1", "Schedule2", "Schedule3", "Schedule4"];
var index = -1;

class ToolbarTab extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            index : props.index,
            curIndex: props.curIndex,
            changeIndex : this.props.changeIndex
        };
        this.tabClick = this.tabClick.bind(this);
        this.tabColor = this.tabColor.bind(this);
        this.changeIndex = this.props.changeIndex.bind(this);
    }

    tabClick () {
        this.props.setCurrentIndex(this.state.index);
        this.props.changeIndex(this.state.index);
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

    componentWillReceiveProps(nextProps){
        if (nextProps.curIndex !== this.state.curIndex){
            this.setState({
                curIndex: nextProps.curIndex
            })
        }
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

        if (this.state.index === this.state.curIndex){
            return (
                <div id = {cardName} className = "card-body" style={{backgroundColor: "#d9edf7"}} onClick={this.tabClick}>
                    <h5 id = {textName} style={{color: "#0055cc"}}>
                        Schedule {this.state.index}
                    </h5>
                </div>
            )
        }else {

            return (
                <div id={cardName} className="card-body" style={{backgroundColor: "#5bc0de"}} onClick={this.tabClick}>
                    <h5 id={textName} style={{color: "#f7f7f7"}}>
                        Schedule {this.state.index}
                    </h5>
                </div>
            )
        }
    }
}

class ScheduleToolbar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedScheduleIndex: 1,
            size: this.props.size
        };
        this.setCurrentIndex = this.setCurrentIndex.bind(this);



    }

    setCurrentIndex(i){
        this.setState({
            selectedScheduleIndex:i
        });
    }

    render(){
        let tabs = [];
        for(let i = 0; i < this.state.size; i++) {
            tabs.push(<ToolbarTab index={i+1} setCurrentIndex={this.setCurrentIndex} curIndex ={this.state.selectedScheduleIndex}
                                  changeIndex = {this.props.changeIndex} key={i}/>);
        }
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