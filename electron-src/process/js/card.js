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
import { createStore } from "redux";
import { connect, Provider } from "react-redux";

// Track any change events to the store
const store = createStore((state = { value: "" }, action) => {
  switch (action.type) {
    case "UPDATE":
      // see new options come through correctly
      console.log({ state });
      console.log({ action });
      return { state, value: action.value };
    default:
      return state;
  }
});

// pass any new values in as props
const mapState = ({ value }) => ({ value });

// dispatch an update for any onChange
const mapDispatch = dispatch => ({
  update: event => dispatch({ type: "UPDATE", value: event })
});

// starting options array
const defaultOptions = [
  { value: 1, label: "One" },
  { value: "hello", label: "hello" }
];
const Select = ({ value = "not an option", update }) =>
  <Creatable
    simpleValue
    value={value}
    onChange={update}
    options={defaultOptions}
  />;

const App = connect(mapState, mapDispatch)(Select);


const style = {
    border: '1px dashed gray',
    padding: '0.5rem 1rem',
    margin: '.5rem',
    backgroundColor: 'white',
    cursor: 'move'
};

class Card extends React.Component {
  constructor(props){
      super(props);
      this.state = {
          modal : false,
          index : 0,
          //test

          multiValue: [],
          filterOptions: [
            { value: "foo", label: "Foo" },
            { value: "bar", label: "Bar" },
            { value: "bat", label: "Bat" }
          ]

      };
  this.toggleSaveModal = this.toggleSaveModal.bind(this);
  this.toggleMessageModal = this.toggleMessageModal.bind(this);
}
  toggleSaveModal(){
      this.setState({
          modal : ! this.state.modal
      })
  }

  toggleMessageModal(){
      this.setState({
          saveMessageModal: ! this.state.saveMessageModal
      })
  }

  changeIndex(v) {
      this.setState({
          index : v
      })
  }

  exportSchedule(value){
      if (value === 0) {
          this.toggleSaveModal();
      } else {
          this.toggleSaveModal();
          this.toggleMessageModal();
          if (value === 1) {
              this.state.saveMessage = "Session saved!";
          } else if (value === 2) {
              this.state.saveMessage = "Uploaded to server!";
          }
      }
  }
//test



	render() {
		const { card, isDragging, connectDragSource, connectDropTarget } = this.props;
		const opacity = isDragging ? 0 : 1;


		return connectDragSource(connectDropTarget(
    <div className="container-fluid">
			<div style={{ style, opacity }}>
				<ul className="list-group" onClick={this.toggleSaveModal}style={{"textAlign": "center"}}>
					<li className="list-group-item">{card.tutorCourse}</li>
					<li className="list-group-item">{card.tutorName}</li>
				</ul>
			</div>

      <Modal isOpen={this.state.modal} toggle={this.toggleSaveModal}>
          <ModalHeader toggle={this.toggleSaveModal} >
              <div style={{"textAlign": "left", "fontSize": "40px"}}>
                  <p>{card.tutorName}</p>
              </div>
              <marquee behavior="alternate" width="”150″" scrollamount="”26″" scrolldelay="”10″" height="”37″" bgcolor="#FFFFFF">{card.day}</marquee>
          </ModalHeader>
          <ModalBody>
          <Provider store={store}>
            <div>
              <h3>React Select</h3>
                <div>Standard</div>
                  <App />
            </div>
          </Provider>
          </ModalBody>
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
