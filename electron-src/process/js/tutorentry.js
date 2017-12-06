let ReactDOM = require('react-dom');
let React = require('react');


class TutorEntry extends React.Component {
    constructor(props){
        super(props);
        this.decideDropDown = this.decideDropDown.bind(this);
        this.joinData = this.joinData.bind(this);
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

    joinData(coursePreferences){
        if (coursePreferences === ""){
            return "";
        }
        else{
            return coursePreferences.join(" ");
        }
    }

    render() {

        return (
            <tr>
                <th scope="row">{this.props.singleItem.id}</th>

                <td style={{textAlign:"center"}}>
                    <div className="form-check" >
                    <input type="checkbox" className="form-check-input"/>
                    </div>
                </td>
                <td>{this.props.singleItem.firstName}</td>
                <td>{this.props.singleItem.lastName}</td>
                <td>{this.props.singleItem.tutorStatus}</td>
                <td>{this.joinData(this.props.singleItem.coursePreference)}</td>
                <td>{this.joinData(this.props.singleItem.courseWilling)}</td>
                <td>{this.joinData(this.props.singleItem.shiftPreference)}</td>
                <td>{this.joinData(this.props.singleItem.shiftWilling)}</td>
                <td>{this.props.singleItem.shiftAmountPreferred}</td>
                <td>{this.props.singleItem.shiftAmountWilling}</td>
            </tr>
        )
    }
}

module.exports = TutorEntry;