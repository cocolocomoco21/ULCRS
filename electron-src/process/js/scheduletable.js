let requireLocal = ( localModule ) =>{
    let path = require("path");
    return require(path.resolve( __dirname, localModule))
};
let Container = requireLocal ('./container'); // droppable container
let React = require('react');
let DragDropContext = require('react-dnd').DragDropContext;
let HTML5Backend = require('react-dnd-html5-backend');

let ipc = electron.ipcRenderer;
let Parser = requireLocal("./parser");

class ScheduleTable extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : this.props.schedules,
            index : this.props.index,
            curIndex : null,
            tutors: [],
            tutorData: null,
            containerDataList: null
        };
        ipc.on("get-tutor-data",  (event, text) => {
          let d = JSON.parse(text);
          let p = new Parser();
          this.setState({
              tutors: p.getTutors(d),
              tutorData: d,
          });
        });
        this.scheduleName = this.scheduleName.bind(this);
        this.passSchedule = this.passSchedule.bind(this);
        this.setContainerDataList = this.setContainerDataList.bind(this);
        this.printContainerDataList = this.printContainerDataList.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            index : nextProps.index
        });
    }

    passSchedule(schedule) {
        this.setState();
        this.props.getSchedule(schedule);
    }

    parseContainerDataList() {
        console.log("in parseContainerDataList");
        let schedule = this.state.schedules[this.state.curIndex];
        for (let col=0; col<this.state.containerDataList.length; col++) {
            let containerData = this.state.containerDataList[col];
            let assignments = [];
            for (let row=0; row<containerData.length; row++) {
                assignments.push({
                    tutor: containerData[row].tutor,
                    courses: containerData[row].tutorCourse
                });
            }
            schedule.scheduleShifts[col].assignments = assignments;
        }
        console.log("schedule");
        console.log(schedule);
        console.log("exiting parseContainerDataList");
        return schedule;
    }

    scheduleName() {
        return "Schedule " + (this.state.index+1);
    }

    setContainerDataList(index, list) {
        console.log("in setContainerDataList");
        let dataList = this.state.containerDataList;
        dataList[index] = list;
        this.setState({
            containerDataList : dataList
        });
        this.printContainerDataList();
        console.log("exiting setContainerDataList");
    }

    printContainerDataList(){
        console.log("in printContainerDataList: printing this.state.containerDataList");
        console.log(this.state.containerDataList);
    }

    render() {
        if (this.state.index != this.state.curIndex) {
            this.state.containerDataList = [];
            console.log("this.state.schedule");
            console.log(this.state.schedules);
            let scheduleShifts = this.state.schedules[this.state.index].scheduleShifts;
            this.state.curIndex = this.state.index;
            console.log(scheduleShifts);
            let index = 1;
            for (let col = 0; col < scheduleShifts.length; col++) {
                console.log(col);
                let containerDataList = [];
                let assignments = scheduleShifts[col].assignments;

                for (let row = 0; row < assignments.length; row++) {
                    containerDataList.push(
                        {
                            id: index,
                            tutor: assignments[row].tutor,
                            tutorCourse: assignments[row].courses
                        });
                    index++;
                }
                this.state.containerDataList.push(containerDataList);
            }
            console.log("this.state.containerDataList");
            console.log(this.state.containerDataList);
        }
        let containerList = [];
        let scheduleShifts = this.state.schedules[this.state.index].scheduleShifts;
        this.parseContainerDataList();
        for (let col = 0; col < this.state.containerDataList.length; col++){
            console.log(col);
            containerList.push(<div className="col-2">
                                    <div className="row">
                                        <div className="col text-center"
                                             style={{background: "#c5050c", color: "#f9f9f9", padding:12}}>
                                            {scheduleShifts[col].shift.day}
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <Container key={this.state.index * 100 + col + 1}
                                                       id={col + 1} list={this.state.containerDataList[col]}
                                                        setContainerDataList={this.setContainerDataList}
                                                       printc={this.printContainerDataList}
                                                        day={scheduleShifts[col].shift.day}/>
                                        </div>
                                    </div>
                                </div>);
        }
        return (
            <div className="container-fluid px-0 " style={{width:"100%",height:"100%"}}>
                <div className="row p-0" style={{height:"100%"}}>
                    <div className="card" style={{width:"100%"}}>
                        <div className="card-header" style={{background: "#c5050c", color: "#f9f9f9"}}>
                                <h3> {this.scheduleName()} </h3>
                                <h5> Rating: {this.state.schedules[this.state.index].rating} </h5>
                        </div>
                        <div className="row container-scroll" style={{height:"100%"}}>
                            <div className="col">
                                <div className="row" style={{margin: 0, width: "100%"}}>
                                    {containerList}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

module.exports = DragDropContext(HTML5Backend)(ScheduleTable);
