let ReactDOM = require('react-dom');
let React = require('react');


class TutorEntry extends React.Component {
    constructor(props){
        super(props);
        this.decideDropDown = this.decideDropDown.bind(this);
    }

    decideDropDown(coursePreferences){
        if (coursePreferences.length === 1){
            return coursePreferences[0];
        }else if (coursePreferences.length > 1) {

            return (
                <div className="dropdown">
                    <button className="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        {coursePreferences[0]}
                    </button>

                    <div className="dropdown-menu" aria-labelledby="dropdownMenu2">
                        <button className="dropdown-item" type="button">{coursePreferences[1]}</button>
                    </div>
                </div>
            )
        }
    }

    render() {

        return (
            <tr>
                <th scope="row">{this.props.singleItem.id}</th>
                <td>{this.props.singleItem.firstName}</td>
                <td>{this.props.singleItem.lastName}</td>
                <td>{this.props.singleItem.tutorStatus}</td>
                <td>{this.props.singleItem.coursePreference.join(" ")}</td>
                <td>{this.props.singleItem.shiftPreference}</td>
                <td>{this.props.singleItem.shiftFrequency}</td>
            </tr>
        )
    }
}

/*
<h3 className="card-header">
    Tutor
</h3>*/

module.exports = TutorEntry;