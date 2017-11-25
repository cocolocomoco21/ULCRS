let Container = require('./container'); // droppable container
let React = require('react');
let DragDropContext = require('react-dnd').DragDropContext;
let HTML5Backend = require('react-dnd-html5-backend');

class ScheduleTable extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : this.props.schedules
        }
    }
    // Put your code here.
    render() {
        // const style = {
        //     display: "flex",
        //     justifyContent: "space-around",
        //     paddingTop: "20px"
        // };
        // let colors = [["#428BCA", "#5CC3E1"], ["#468847", "#46A546"],
        //                 ["#F89406", "#FBB450"], ["#C3325F", "#EE5F5B"]];
        // let tableHeading = [];
        // //TODO: show the heading HTML
        // for (let col = 0; col < this.state.schedules.length; col++){
        //     tableHeading.push(<div className="col-2 text-center">{this.state.schedules[col].Shift}</div>);
        // }

        console.log(this.state.schedules);
        let containerList = [];
        let scheduleShifts = this.state.schedules[0].scheduleShifts;
        console.log(scheduleShifts);
        let index = 1;
        for (let col = 0; col < scheduleShifts.length; col++){
            console.log(col);
            let containerDataList = [];
            let assignments = scheduleShifts[col].assignments;
            console.log(assignments);
            for (let row = 0; row < assignments.length; row++){
                let tutor = assignments[row].tutor.firstName + " " + assignments[row].tutor.lastName;
                containerDataList.push(
                    {
                        id: index,
                        tutorName: tutor,
                        tutorCourse: assignments[row].courses[0].name
                    });
                index++;
            }
            console.log(containerDataList);
            containerList.push(<div className="col-2">
                                    <div className="row">
                                        <div className="col text-center"
                                             style={{background: "#5bc0de", color: "white", padding:12}}>
                                            {scheduleShifts[col].shift.day}
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <Container id={col + 1} list={containerDataList} />
                                        </div>
                                    </div>
                                </div>);
        }

        const listOne = [
            {id: 1, tutorName: "Lily", tutorCourse: "CS 200"},
            {id: 2, tutorName: "Sally", tutorCourse: "CS 540"},
            {id: 3, tutorName: "Wid", tutorCourse: "CS 800"}
        ];

        const listTwo = [
            {id: 4, tutorName: "fan", tutorCourse: "CS 1110"},
            {id: 5, tutorName: "table", tutorCourse: "CS 524"},
            {id: 6, tutorName: "chair", tutorCourse: "CS 10"}
        ];

        const listThree = [
            {id: 7, tutorName: "cat", tutorCourse: "CS 885"},
            {id: 8, tutorName: "dog", tutorCourse: "CS 243"},
            {id: 9, tutorName: "pangolin", tutorCourse: "CS 789"}
        ];

        return (
            <div className="container">
                <div className="row">
                    <div className="col">
                        <div className="row" style={{background: "#5bc0de", color: "white", padding: 15}}>
                            <h3>Schedule: Schedule 1</h3>
                        </div>
                        <div className="row container-scroll">
                            <div className="col" style={{padding: 0}}>
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
//export default DragDropContext(HTML5Backend)(ScheduleTable);