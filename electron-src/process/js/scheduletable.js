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
            tutors: [],
            tutorData: null
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
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            index : nextProps.index
        });
    }

    passSchedule(schedule) {
        this.props.getSchedule(schedule);
    }

    scheduleName() {
        return "Schedule " + (this.state.index+1);
    }

    render() {

        let colors = [["#428BCA", "#5CC3E1"], ["#468847", "#46A546"],
                        ["#F89406", "#FBB450"], ["#C3325F", "#EE5F5B"]];


        console.log(this.state.schedules);
        let containerList = [];
        let scheduleShifts = this.state.schedules[this.state.index].scheduleShifts;
        console.log(scheduleShifts);
        let index = 1;
        let indexIncr = 0;
        for (let col = 0; col < scheduleShifts.length; col++){
            console.log(col);
            let containerDataList = [];
            let assignments = scheduleShifts[col].assignments;
            console.log(assignments);

            if (col % 2 == 0) {
                indexIncr += 1;
            }
            else {
                indexIncr += 2;
            }
            for (let row = 0; row < assignments.length; row++){
                let tutor = assignments[row].tutor.firstName + " " + assignments[row].tutor.lastName;
                let color = colors[(indexIncr + row) % 4];
                containerDataList.push(
                    {
                        id: index,
                        day: scheduleShifts[col].shift.day,
                        tutorName: tutor,
                        tutorCourse: assignments[row].courses,
                        nameColor: color[1],
                        courseColor: color[0]
                    });
                index++;
            }
            console.log(containerDataList);
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
                                                       id={col + 1} list={containerDataList}/>
                                        </div>
                                    </div>
                                </div>);
        }
        return (
            <div className="container">
                <div className="row">
                    <div className="card">
                        <div className="card-header" style={{background: "#c5050c", color: "#f9f9f9"}}>
                                <h3> {this.scheduleName()} </h3>
                                <h5> Rating: {this.state.schedules[this.state.index].rating} </h5>
                        </div>
                        <div className="row container-scroll">
                            <div className="col">
                                <div className="row" style={{margin: 0, width: "100%", height: "500px"}}>
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
