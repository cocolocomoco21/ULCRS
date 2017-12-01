let requireLocal = ( localModule ) =>{
    let path = require("path");
    return require(path.resolve( __dirname, localModule))
};
let ReactDOM = require('react-dom');
let React = require('react');
console.log(__dirname);
let TutorEntry = requireLocal('./tutorentry');

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
            <div className="card card-inverse" style={{background: "#049cdb", border: "#ffffff", color: "#ffffff"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Review your tutors
                    </h3>
                    <div className = "card-body">
                        <div className="container table-scroll">
                        <table className="table table-striped" style={{background: "#049cdb", border: "#ffffff", color: "#ffffff"}}>
                            <thead className="thead" style={{backgroundColor: "#0479a8",
                                color: "white"}}>
                            <tr>
                                <th>id</th>
                                <th>Not Include</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Status</th>
                                <th>Course Preferred</th>
                                <th>Course Willing</th>
                                <th>Shift Preferred</th>
                                <th>Shift Willing</th>
                                <th>Shift Preferred</th>
                                <th>Shift Frequency</th>
                            </tr>
                            </thead>
                            <tbody>
                            {tutors}
                            </tbody>
                        </table>
                        </div>
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
