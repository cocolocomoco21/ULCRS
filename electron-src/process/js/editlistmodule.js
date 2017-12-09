let React = require('react');

import { Form } from "react-redux-form";
import MultiSelect from './multiselect';


class PrefCourseList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            tutorCourse: this.props.tutorCourse,
            tutorId: this.props.tutorId
        };
    }

    handleSubmit(e){
        console.log(e);
    };

    render() {
        return (
            <div>
                <Form model="user" onSubmit={this.handleSubmit}>
                <MultiSelect model="user.category" tutorCourse={this.state.tutorCourse}
                             tutorId={this.state.tutorId} isWilling={false}
                             setCoursesAssigned={this.props.setPreferCoursesAssigned}/>
                </Form>
            </div>
        );
    }
}
export default PrefCourseList;
