let ReactDOM = require('react-dom');
let React = require('react');

class ShiftCard extends React.Component {
    constructor(props){
        super(props);
        let info = this.props.info;
        this.state = {
            tutorName:info.Name,
            tutorCourse: info.Course
        }
    }

    render(){
        return (
            <div className="card">
                <ul className="list-group list-group-flush">
                    <li className="list-group-item">{this.state.tutorCourse}</li>
                    <li className="list-group-item">{this.state.tutorName}</li>
                </ul>
            </div>
        )
    }
}
 

class ScheduleTable extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : this.props.schedules
        }
    }
    // Put your code here.
    render() {
        let tableHeading = [];
        for (let col = 0; col < this.state.schedules.length; col++){
            tableHeading.push(<th>{this.state.schedules[col].Slot}</th>);
        }

        let tableContent = [];
        let row = 0;
        while (true){
            let isEmptyRow = true;
            let rowContent = [];
            for (let col = 0; col < this.state.schedules.length; col++){
                console.log(col);
                if (this.state.schedules[col].Data.length > row){
                    isEmptyRow = false;
                    rowContent.push(<td><ShiftCard info={this.state.schedules[col].Data[row]} /></td>);
                }
                else{
                    rowContent.push(<td/>);
                }
            }
            console.log(isEmptyRow);
            if (isEmptyRow==true){
                break;
            }
            else{
                tableContent.push(<tr>{rowContent}</tr>);
            }
            row++;
        }
        return (
            <div className="card" style={{overflow: "auto", background: "#00ffff", border: "#333"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Schedule: schedule 1
                    </h3>
                    <div className = "card-body">
                    <table className="table">
                        <thead>
                            <tr>
                                {tableHeading}
                            </tr>
                        </thead>
                        <tbody>
                            {tableContent}
                        </tbody>
                    </table>
                    </div> {/*tool bar */}
                </div>
            </div>
        )
    }
}

module.exports = ScheduleTable;