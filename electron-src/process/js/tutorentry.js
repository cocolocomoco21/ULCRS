let ReactDOM = require('react-dom');
let React = require('react');


class TutorEntry extends React.Component {
    constructor(props){
        super(props);
        this.decideDropDown = this.decideDropDown.bind(this);
        this.joinData = this.joinData.bind(this);
        this.excludedId = [];
        this.checkHandler = this.checkHandler.bind(this);
        this.state = {
            checked:this.props.singleItem.notInclude
        };
        this.decideCheckbox = this.decideCheckbox.bind(this);
    };

    componentWillReceiveProps(nextProps){
        this.setState({
            checked: nextProps.singleItem.notInclude
        })
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

    checkHandler(event){
        this.setState({
            checked:! this.state.checked
        });
        if (this.state.checked === true) {
            this.props.notIncludeHandler(this.props.singleItem.id, "remove")
        }
        else{
            this.props.notIncludeHandler(this.props.singleItem.id, "add")
        }

    }

    decideCheckbox(){

        if (this.state.checked){
            return (<input type="checkbox" className="form-check-input"
                           checked={true}
                           onChange={this.checkHandler}
                    />
            )
        }
        else {
            return (<input type="checkbox" className="form-check-input"
                           checked={false}
                           onChange={this.checkHandler}
                />
            )
        }
    }

    render() {



        return (
            <tr>
                <th scope="row">{this.props.singleItem.id}</th>

                <td style={{textAlign:"center"}}>
                    <div className="form-check" >
                        {this.decideCheckbox()}
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