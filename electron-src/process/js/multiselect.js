import React, { Component } from 'react';
//let React = require('react');
//let Control = require('react-redux-form');
//let Select = require('react-select');
import { Control } from 'react-redux-form';
import Select from 'react-select';

let fs = require('fs');

let tutorLocation = require('path').resolve(__dirname, '..', '..','data', 'mockTutorData.json');
let loadTutorData = JSON.parse(fs.readFileSync(tutorLocation));

class MultiSelect extends React.Component {
  constructor(props) {
    super(props);
    this.state = { categoryValue: [] };
    this.handleSelectChange = this.handleSelectChange.bind(this);
  }

  handleSelectChange(value){
    this.setState({ categoryValue: value });
  };

  render() {
    let targetId = this.props.tutorId;
    console.log("targetId");
    console.log(targetId);
    console.log("loadTurorCourse");
    console.log(loadTutorData);
    let result = null;
    for (let i=0; i<loadTutorData.length; i++){
      if (loadTutorData[i].id == targetId) {
        result=loadTutorData[i];
      }
    }
    console.log("result");
      console.log(result);
    let courses = [];
    if (this.props.isWilling == true) {
      let willing = result.tutorPreferences.coursePreferences.WILLING;
      for (let i=0; i<willing.length; i++) {
        courses.push({
          value: i,
          label: willing[i].name
        });
      }
      if (this.state.categoryValue.length == 0) {
          this.state.categoryValue = "0";
      }
      console.log("in willing");
    }
    else {
      let prefer = result.tutorPreferences.coursePreferences.PREFER;
      for (let i=0; i<prefer.length; i++) {
        courses.push({
          value: i,
          label: prefer[i].name
        });
      }
        if (this.state.categoryValue.length == 0) {
            this.state.categoryValue = "0,1";
        }
        console.log("in prefer");
    }
    
    let reactSelect = props => (
      <Select
        {...props}
      />
    );
      console.log("this.state.categoryValue");
    console.log(this.state.categoryValue);
    console.log(this.state.categoryValue.length);

    return (
      <div className="form__row">
        <div className="form__label">
          <span className="form__title">
            {this.props.title}
            {this.props.isRequired ? (
              <span className="form__required">*</span>
            ) : (
                ''
              )}
          </span>
        </div>
        <Control.custom
          model={this.props.model}
          mapProps={
            {
              onChange: (props) => props.onChange
            }
          }
          id={this.props.model}
          component={reactSelect}
          simpleValue
          multi
          value={this.state.categoryValue}
          options={courses}
          onChange={this.handleSelectChange}
          joinValues
          name={this.props.model}
          required
        />
      </div>
    );
  }
}

export default MultiSelect;
