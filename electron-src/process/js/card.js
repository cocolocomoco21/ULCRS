let React = require('react');
let DropTarget = require('react-dnd').DropTarget;
let DragSource = require('react-dnd').DragSource;
let findDOMNode = require('react-dom').findDOMNode;
let flow = require('lodash.flow');

let reactstrap = require('reactstrap');
let Modal = reactstrap.Modal;
let ModalHeader = reactstrap.ModalHeader;
let ModalBody = reactstrap.ModalBody;
let ModalFooter = reactstrap.ModalFooter;

import { Creatable } from "react-select";
import { compose, createStore } from "redux";
import { connect, Provider } from "react-redux";
import { combineForms, Form, Control } from "react-redux-form";
import MultiSelect from './multiselect';
import PrefCourseList from './editlistmodule';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const initialUser = {};
const store = createStore(
  combineForms({
    user: initialUser
  })
);

class WillCourseList extends React.Component {
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
          tutorId={this.state.tutorId} isWilling={true}
           setWillCoursesAssigned={this.props.setWillCoursesAssigned.bind(this)}/>
        </Form>
      </div>
    );
  }
}

const style = {
    border: '3px dashed gray',
    padding: 10,
    margin: '.5rem',
    backgroundColor: 'black',
    cursor: 'move'
};

class Card extends React.Component {
  constructor(props){
      super(props);
      this.state = {
          modal : false,
          saveMessageModal: false,
          card : this.props.card,
          day : this.props.day,
          willCoursesAssigned : [1,2],
          preferCoursesAssigned : [],
      };
  this.toggleMessageModal = this.toggleMessageModal.bind(this);
  this.toggleGridModal = this.toggleGridModal.bind(this);
  this.setPreferCoursesAssigned = this.setPreferCoursesAssigned.bind(this);
  this.setWillCoursesAssigned = this.setWillCoursesAssigned.bind(this);
  this.savePopUp = this.savePopUp.bind(this);
  //this.changeCourses = this.changeCourses.bind(this);
}

  // changeCourses(c){
  //   this.setState({
  //     card.tutorCourse : c
  //   })
  // }
    setTutorCourse() {
        let card = this.state.card;
        card.tutorCourse = this.state.preferCoursesAssigned.concat(this.state.willCoursesAssigned);
        this.props.setCard(this.props.index, card);
    }

    setWillCoursesAssigned(courses) {
        this.setState({
            willCoursesAssigned: courses
        });
    }

    setPreferCoursesAssigned(courses) {
        this.setState({
            preferCoursesAssigned: courses
        });
    }

    savePopUp() {
        console.log("in save pop up");
        this.toggleMessageModal();
        console.log(this.state.preferCoursesAssigned);
        console.log(this.state.willCoursesAssigned);
    }

  toggleGridModal(){
      this.setState({
          modal : ! this.state.modal
      })
  }

  toggleMessageModal(){
      this.setState({
          saveMessageModal: ! this.state.saveMessageModal
      })
  }

	render() {
    const card = this.state.card;
		const { isDragging, connectDragSource, connectDropTarget } = this.props;
		const opacity = isDragging ? 0 : 1;

		let course = [];
		let tutorName = card.tutor.firstName + " " + card.tutor.lastName;
		if (card.tutorCourse.length == 1) {
		    course.push(<li className="list-group-item" style={{backgroundColor: "#f9f9f9"}}>
        <a style={{color: "black"}}>
          {card.tutorCourse[0].name}
        </a>
        </li>);
        }
        else if (card.tutorCourse.length == 2) {
            course.push(<li className="list-group-item" style={{backgroundColor: "#f9f9f9"}}>
            <a style={{color: "black"}}>
            {card.tutorCourse[0].name + ", " + card.tutorCourse[1].name}
            </a>
        </li>);
        }
        else {
		    let tooltip = card.tutorCourse[0].name;
		    for (let index=1; index<card.tutorCourse.length; index++) {
                tooltip = tooltip + ", " + card.tutorCourse[index].name;
            }
		    course.push(<li className="list-group-item" style={{backgroundColor: "#f9f9f9"}}>
                <a href="#" data-toggle="tooltip" title={tooltip} style={{color: "black"}}>
                    {card.tutorCourse[0].name + "...."}
                </a>
            </li>);
        }

		return connectDragSource(connectDropTarget(
    <div style={{ style, opacity }}>
			<div>
				<ul className="list-group" onClick={this.toggleGridModal}style={{"textAlign": "center"}}>
					<li className="list-group-item" style={{backgroundColor: "#f9f9f9"}}>
          <a style={{color: "black"}}>
          {tutorName}
           </a>
          </li>
          {course}
				</ul>
			</div>

      <Modal isOpen={this.state.modal} toggle={this.toggleGridModal}>
          <ModalHeader toggle={this.toggleGridModal} >
              <div style={{"textAlign": "left", "fontSize": "40px"}}>
                  <h3>Shift {this.state.day} of {tutorName}</h3>
              </div>
          </ModalHeader>
          <ModalBody>
          <Provider store={store}>
            <div>
              <h4>Add/Delete</h4>
                <div>Prefer Course List of {tutorName}</div>
                    <PrefCourseList tutorCourse={card.tutorCourse} tutorId={card.tutor.id}
                    setPreferCoursesAssigned={this.setPreferCoursesAssigned}/>
                <div>Willing Course List of {tutorName}</div>
                  <WillCourseList tutorCourse={card.tutorCourse} tutorId={card.tutor.id}
                  setWillCoursesAssigned={this.setWillCoursesAssigned}/>
            </div>
          </Provider>
          </ModalBody>
          <ModalFooter>
            <button type="button" className="btn btn-success btn-block" onClick={this.savePopUp} style={{"textAlign": "center"}} > Save </button>
          </ModalFooter>

          <Modal isOpen={this.state.saveMessageModal} toggle={this.toggleMessageModal}>
              <ModalHeader toggle={this.toggleMessageModal} >
                  <div style={{"textAlign": "center", "fontSize": "20px"}}>
                      Shift {this.state.day} of {tutorName}
                  </div>
              </ModalHeader>
              <ModalBody>
                  <div id="message-content"> Succesfully Modified </div>
              </ModalBody>
          </Modal>
      </Modal>
    </div>
		));
	}
}

const cardSource = {

	beginDrag(props) {
		return {
			index: props.index,
			listId: props.listId,
			card: props.card
		};
	},

	endDrag(props, monitor) {
		const item = monitor.getItem();
		const dropResult = monitor.getDropResult();

		if ( dropResult && dropResult.listId !== item.listId ) {
			props.removeCard(item.index);
		}
	}
};

const cardTarget = {

	hover(props, monitor, component) {
		const dragIndex = monitor.getItem().index;
		const hoverIndex = props.index;
		const sourceListId = monitor.getItem().listId;

		// Don't replace items with themselves
		if (dragIndex === hoverIndex) {
			return;
		}

		// Determine rectangle on screen
		const hoverBoundingRect = findDOMNode(component).getBoundingClientRect();

		// Get vertical middle
		const hoverMiddleY = (hoverBoundingRect.bottom - hoverBoundingRect.top) / 2;

		// Determine mouse position
		const clientOffset = monitor.getClientOffset();

		// Get pixels to the top
		const hoverClientY = clientOffset.y - hoverBoundingRect.top;

		// Only perform the move when the mouse has crossed half of the items height
		// When dragging downwards, only move when the cursor is below 50%
		// When dragging upwards, only move when the cursor is above 50%

		// Dragging downwards
		if (dragIndex < hoverIndex && hoverClientY < hoverMiddleY) {
			return;
		}

		// Dragging upwards
		if (dragIndex > hoverIndex && hoverClientY > hoverMiddleY) {
			return;
		}

		// Time to actually perform the action
		if ( props.listId === sourceListId ) {
			props.moveCard(dragIndex, hoverIndex);

			// Note: we're mutating the monitor item here!
			// Generally it's better to avoid mutations,
			// but it's good here for the sake of performance
			// to avoid expensive index searches.
			monitor.getItem().index = hoverIndex;
		}
	}
};

module.exports = flow(
    DropTarget("CARD", cardTarget, connect => ({
        connectDropTarget: connect.dropTarget()
    })),
    DragSource("CARD", cardSource, (connect, monitor) => ({
        connectDragSource: connect.dragSource(),
        isDragging: monitor.isDragging()
    }))
)(Card);
