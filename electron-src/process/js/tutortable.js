let ReactDOM = require('react-dom');
let React = require('react');



class TutorTable extends React.Component {
    render() {
        return (
            <div className="card card-inverse" style={{background: "#ffbf00", border: "#333"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Tutor
                    </h3>

                    <div className = "card-body">
                        <table className="table table-striped">
                          <thead>
                            <tr>
                              <th>id</th>
                              <th>First Name</th>
                              <th>Last Name</th>
                              <th>Status</th>
                              <th>Course Preferences</th>
                              <th>Shift Preferences</th>
                              <th>Shift Frequency</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <th scope="row">1</th>
                              <td>{this.props.singleItem.firstName}</td>
                              <td>{this.props.singleItem.lastName}</td>
                              <td>{this.props.singleItem.tStatus}</td>
                              <td>{this.props.singleItem.coursePreference}</td>
                              <td>{this.props.singleItem.shiftPreference}</td>
                              <td>{this.props.singleItem.shiftFrequency}</td>
                            </tr>
                          </tbody>
                        </table>
                    </div> {/*tool bar */}
                </div>
            </div>
        )
    }
}

/*
<h3 className="card-header">
    Tutor
</h3>*/

module.exports = TutorTable;
