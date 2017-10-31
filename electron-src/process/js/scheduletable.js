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
            <td>
                <div className="card">
                    <ul className="list-group list-group-flush">
                        <li className="list-group-item">{this.state.tutorCourse}</li>
                        <li className="list-group-item">{this.state.tutorName}</li>
                    </ul>
                </div>
            </td>
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
        let a = [];
        for (var i = 0; i < this.state.schedules.length; i++){
            a.push(<th>{this.state.schedules[i].Slot}</th>)
        };

        let b = [];
        var j = 0;
        while (true){
            var emptyRow = true;
            let row = [];
            for (var i = 0; i < this.state.schedules.length; i++){
                console.log(i);
                if (this.state.schedules[i].Data.length > j){
                    emptyRow = false;
                    row.push(<td><ShiftCard info={this.state.schedules[i].Data[j]} /></td>);
                }
                else{
                    row.push(<td></td>);
                }
            }
            console.log(emptyRow);
            if (emptyRow==true){
                break;
            }
            else{
                b.push(<tr>{row}</tr>);
            }
            j++;
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
                                {a}
                            </tr>
                        </thead>
                        <tbody>
                            {b}
                        </tbody>
                    </table>
                    </div> {/*tool bar */}
                </div>
            </div>
        )
    }
}

module.exports = ScheduleTable;