let ReactDOM = require('react-dom');
let React = require('react');



class TutorEntry extends React.Component {
    render() {

        return (
            <tr>
                <th scope="row">{this.props.singleItem.id}</th>
                <td>{this.props.singleItem.firstName}</td>
                <td>{this.props.singleItem.lastName}</td>
                <td>{this.props.singleItem.tStatus}</td>
                <td>{this.props.singleItem.coursePreference}</td>
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