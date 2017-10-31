let ReactDOM = require('react-dom');
let React = require('react');

class CourseEntry extends React.Component {
    render() {
        let courseRequirements = this.props.singleItem.courseRequirements;
        return (
            <tr>


                <th scope="row">{this.props.singleItem.id}</th>
                <td>{this.props.singleItem.name}</td>
                <td>{courseRequirements.requiredShifts[0].day}</td>
                <td>{courseRequirements.requiredShiftAmount}</td>
                <td>{courseRequirements.preferredShiftAmount}</td>
                <td>{courseRequirements.intensity}</td>
            </tr>
        )
    }
}

/*
<h3 className="card-header">
    Tutor
</h3>*/

module.exports = CourseEntry;