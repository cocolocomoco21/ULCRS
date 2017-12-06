import React, { Component } from 'react';
//let React = require('react');
//let Control = require('react-redux-form');
//let Select = require('react-select');
import { Control } from 'react-redux-form';
import Select from 'react-select';

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
    let reactSelect = props => (
      <Select
        {...props}
      />
    );

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
          options={this.props.options}
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
