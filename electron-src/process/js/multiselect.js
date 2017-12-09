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
        this.state = {
            categoryValue: null,
            courses: null,
            options: [],
            idToIndex: {}
        };
        this.handleSelectChange = this.handleSelectChange.bind(this);
    }

    handleSelectChange(value){
        this.setState({ categoryValue: value });
    };

    render() {
        if (this.state.courses == null) {
            let targetId = this.props.tutorId;
            console.log("targetId");
            console.log(targetId);
            console.log("loadTurorData");
            console.log(loadTutorData);
            let result = null;
            for (let i=0; i<loadTutorData.length; i++){
                if (loadTutorData[i].id == targetId) {
                    result=loadTutorData[i];
                }
            }
            console.log("result");
            console.log(result);
            if (this.props.isWilling == true) {
                console.log("in willing");
                this.state.courses = result.tutorPreferences.coursePreferences.WILLING;
                for (let i=0; i<this.state.courses.length; i++) {
                    this.state.options.push({
                        value: this.state.courses[i].id,
                        label: this.state.courses[i].name
                    });
                    this.state.idToIndex[this.state.courses[i].id] = i;
                }
                console.log("this.state.idToIndex");
                console.log(this.state.idToIndex);
            }
            else {
                console.log("in prefer");
                this.state.courses = result.tutorPreferences.coursePreferences.PREFER;
                for (let i=0; i<this.state.courses.length; i++) {
                    this.state.options.push({
                        value: this.state.courses[i].id,
                        label: this.state.courses[i].name
                    });
                    this.state.idToIndex[this.state.courses[i].id] = i;
                }
                console.log("this.state.idToIndex");
                console.log(this.state.idToIndex);
            }
        }

        if (this.props.isWilling == true) {

        }

        let reactSelect = props => (
          <Select
            {...props}
          />
        );
        console.log("this.state.categoryValue");
        console.log(this.state.categoryValue);
        // console.log(this.state.categoryValue.length);

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
                    options={this.state.options}
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
