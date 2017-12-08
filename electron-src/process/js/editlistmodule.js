let React = require('react');

import { Form } from "react-redux-form";
import MultiSelect from './multiselect';


class PrefCourseList extends React.Component {

  handleSubmit(e){
    console.log(e);
  };

  render() {

    return (
      <div>
        <Form model="user" onSubmit={this.handleSubmit}>
          <MultiSelect model="user.category" options={[
            { value: 'one', label: 'CS302' },
            { value: 'two', label: 'CS240' },
            { value: '3', label: 'CS367' },
            { value: '5', label: 'CS252' },
            { value: '4', label: 'CS577' }
          ]} />
        </Form>
      </div>
    );
  }
}
export default PrefCourseList;
