let ReactDOM = require('react-dom');
let React = require('react');

class CourseEntry extends React.Component {
    render() {
        let courseRequirements = this.props.singleItem.courseRequirements;
        return (
            <tr>


                <th scope="row">{this.props.singleItem.id}</th>
                <td>{this.props.singleItem.name}</td>
                <td>{this.props.singleItem.requiredShifts }</td>
                <td>{this.props.singleItem.requiredShiftAmount}</td>
                <td>{this.props.singleItem.preferredShiftAmount}</td>
                <td>{this.props.singleItem.intensity}</td>
            </tr>
        )
    }
}

/*
<h3 className="card-header">
    Tutor
</h3>*/

module.exports = CourseEntry;