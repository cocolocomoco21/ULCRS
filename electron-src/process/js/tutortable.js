let ReactDOM = require('react-dom');
let React = require('react');
let TutorEntry = require('./tutorentry');

class TutorTable extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            tutors : this.props.tutors
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
                tutors: nextProps.tutors
            })
    }


    render() {
        let tutors = this.state.tutors.map(function(item, index){
            return(
                <TutorEntry key = {index}
                            singleItem = {item}
                />
            )
        }.bind(this));
        return (
            <div className="card card-inverse" style={{background: "#3a99ff", border: "#ffffff"}}>
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
                            {tutors}
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