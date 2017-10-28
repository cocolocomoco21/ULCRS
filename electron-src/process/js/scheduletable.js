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
        let cards = [<ShiftCard info={this.state.schedules[0].Sunday1[0]} key="0"/>,   
        <ShiftCard info={this.state.schedules[0].Sunday1[1]} key="1" />,
        <ShiftCard info={this.state.schedules[0].Sunday1[2]} key="2" />]
        return (

            <div className="card" style={{background: "#00ffff", border: "#333"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Schedule: schedule 1
                    </h3>

                    <div className = "card-body">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Sunday</th>
                                <th>Monday</th>
                                <th>Tuesday</th>
                                <th>Wednesday</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                {cards}
                                <td>Chandler</td>
                            </tr>
                            <tr>
                                <td scope="row"></td>
                                <td>Jacob</td>
                                <td>Thornton</td>
                                <td>@fat</td>
                            </tr>
                            <tr>
                                <td scope="row">3</td>
                                <td>Larry</td>
                                <td>the Bird</td>
                                <td>@twitter</td>
                            </tr>
                        </tbody>
                    </table>
                    </div> {/*tool bar */}
                </div>
            </div>
        )
    }
}

module.exports = ScheduleTable;