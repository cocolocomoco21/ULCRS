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
        let scheduleData = this.state.schedules[0].scheduleData;
        console.log(scheduleData);
        let index = 1;
        for (let col = 0; col < scheduleData.length; col++){
            console.log(col);
            let containerDataList = [];
            let shiftData = scheduleData[col].shiftData;
            console.log(shiftData);
            for (let row = 0; row < shiftData.length; row++){
                containerDataList.push(
                    {id: index,
                        tutorName: shiftData[row].tutorName,
                        tutorCourse: shiftData[row].tutorCourse
                    });
                index++;
            }
            console.log(containerDataList);
            containerList.push(<div className="col-2">
                                    <div className="row">
                                        <div className="col text-center"
                                             style={{background: "#5bc0de", color: "white", padding:12}}>
                                            {scheduleData[col].shiftName}
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <Container id={col + 1} list={containerDataList} />
                                        </div>
                                    </div>
                                </div>);
        }
        console.log(this.state.schedules[0].scheduleName);

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
            <div className="card">
                <h3 className="card-header" style={{background: "#5bc0de", color: "white"}}>
                    Schedule: {this.state.schedules[0].scheduleName}
                </h3>
                <div>
                    <div className="card-body container-scroll">
                        <div className="row">
                            {containerList}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

module.exports = DragDropContext(HTML5Backend)(ScheduleTable);
//export default DragDropContext(HTML5Backend)(ScheduleTable);