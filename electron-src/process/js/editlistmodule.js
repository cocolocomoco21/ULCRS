let React = require('react');

import { Form } from "react-redux-form";
import MultiSelect from './multiselect';


class PrefCourseList extends React.Component {
  constructor(props){
      super(props);
      this.state = {
          preLoadOption: this.props.tutorCourse,
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
          <MultiSelect model="user.category" options={this.state.preLoadOption} 
          tutorId={this.state.tutorId} isWilling={false}/>
        </Form>
      </div>
    );
  }
}
export default PrefCourseList;
