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
        let colors = [["#428BCA", "#5CC3E1"], ["#468847", "#46A546"],
                        ["#F89406", "#FBB450"], ["#C3325F", "#EE5F5B"]];
        let tableHeading = [];
        for (let col = 0; col < this.state.schedules.length; col++){
            tableHeading.push(<th className="text-center">{this.state.schedules[col].Shift}</th>);
        }

        let tableContent = [];
        let row = 0, colorShift = 0;
        while (true){
            let isEmptyRow = true;
            let rowContent = [];
            for (let col = 0; col < this.state.schedules.length; col++){
                console.log(col);
                let colorIndex = (colorShift + col) % 4;
                console.log(colorShift, col, colorIndex);
                if (this.state.schedules[col].Data.length > row){
                    isEmptyRow = false;
                    rowContent.push(<td>
                                        <div className="card">
                                            <ul className="list-group list-group-flush">
                                                <li className="list-group-item" style={{background: colors[colorIndex][0]}}>
                                                    {this.state.schedules[col].Data[row].Course}
                                                </li>
                                                <li className="list-group-item" style={{background: colors[colorIndex][1]}}>
                                                    {this.state.schedules[col].Data[row].Name}
                                                </li>
                                            </ul>
                                        </div>
                                    </td>);
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
            colorShift++;
            if (row % 2 == 1){
                colorShift++;
            }
        }
        return (
            <div className="card" style={{overflow: "auto", border: "#333"}}>
                <div className="card-block">
                    <h3 className="card-header" style={{background: "#049cdb", color: "#ffffff"}}>
                        Schedule: Schedule 1
                    </h3>
                    <div className = "card-body" style={{background: "#ffffff", color: "#000000"}}>
                        <table className="table">
                            <thead style={{background: "#F0F0F0"}}>
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
