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
        const style = {
            display: "flex",
            justifyContent: "space-around",
            paddingTop: "20px"
        };
        const listOne = [
            {id: 1, Name: "Lily", Course: "CS 200"},
            {id: 2, Name: "Sally", Course: "CS 540"},
            {id: 3, Name: "Wid", Course: "CS 800"}
        ];

        const listTwo = [
            {id: 4, Name: "fan", Course: "CS 1110"},
            {id: 5, Name: "table", Course: "CS 524"},
            {id: 6, Name: "chair", Course: "CS 10"}
        ];

        const listThree = [
            {id: 7, Name: "cat", Course: "CS 885"},
            {id: 8, Name: "dog", Course: "CS 243"},
            {id: 9, Name: "pangolin", Course: "CS 789"}
        ];

        return (
            <div className="row">
                <div className="col-2">
                    <Container id={1} list={listOne} />
                </div>
                <div className="col-2">
                    <Container id={2} list={listTwo} />
                </div>
                <div className="col-2">
                    <Container id={3} list={listThree} />
                </div>
            </div>
        );
    }
}

module.exports = DragDropContext(HTML5Backend)(ScheduleTable);
//export default DragDropContext(HTML5Backend)(ScheduleTable);