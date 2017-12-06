let ReactDOM = require('react-dom');
let React = require('react');
let path = require("path");
let CourseEntry = require(path.resolve( __dirname,'./courseentry'));

class CourseTable extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            courses : this.props.courses
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            courses: nextProps.courses
        })
    }

    render() {
        let courses = this.state.courses.map(function(item, index){
            return(
                <CourseEntry key = {index}
                            singleItem = {item}
                />
            )
        }.bind(this));
        return (
            <div className="card card-inverse" style={{background: "#c5050c", border: "#ffffff", color: "#ffffff"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Review your courses
                    </h3>
                    <div className="card-body p-0">
                        <div className="container table-scroll p-0">
                        <table className="table table-striped" >
                            <thead style={{backgroundColor: "#9b0000",
                                color: "#f9f9f9"}}>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Course Requirement</th>
                                <th>Preferred Shift Amountt</th>
                                <th>Willing Shift Amount</th>
                                <th>Intensity</th>
                            </tr>
                            </thead>
                            <tbody>
                            {courses}
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

module.exports = CourseTable;
