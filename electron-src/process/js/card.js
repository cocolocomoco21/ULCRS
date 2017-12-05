let React = require('react');
let DropTarget = require('react-dnd').DropTarget;
let DragSource = require('react-dnd').DragSource;
let findDOMNode = require('react-dom').findDOMNode;
let flow = require('lodash.flow');

const style = {
    border: '1px dashed gray',
    padding: '0.5rem 1rem',
    margin: '.5rem',
    backgroundColor: 'white',
    cursor: 'move'
};

class Card extends React.Component {

	render() {
		const { card, isDragging, connectDragSource, connectDropTarget } = this.props;
		const opacity = isDragging ? 0 : 1;

		let course = [];
		if (card.tutorCourse.length == 1) {
		    course.push(<li className="list-group-item" style={{backgroundColor: card.courseColor}}>{card.tutorCourse[0].name}</li>);
        }
        else if (card.tutorCourse.length == 2) {
            course.push(<li className="list-group-item" style={{backgroundColor: card.courseColor}}>{card.tutorCourse[0].name + ", " + card.tutorCourse[1].name}</li>);
        }
        else {
		    let tooltip = card.tutorCourse[0].name;
		    for (let index=1; index<card.tutorCourse.length; index++) {
                tooltip = tooltip + ", " + card.tutorCourse[index].name;
            }
		    course.push(<li className="list-group-item" style={{backgroundColor: card.courseColor}}>
                <a href="#" data-toggle="tooltip" title={tooltip} style={{color: "white"}}>
                    {card.tutorCourse[0].name + "...."}
                </a>
            </li>);
        }

		return connectDragSource(connectDropTarget(
			<div style={{ style, opacity }}>
				<ul className="list-group">
                    {course}
					<li className="list-group-item" style={{backgroundColor: card.nameColor}}>{card.tutorName}</li>
				</ul>
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
