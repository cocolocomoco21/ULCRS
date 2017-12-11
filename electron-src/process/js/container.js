let Card = require('./Card.js');
let React = require('react');
let update = require('react-addons-update');

let DropTarget = require('react-dnd').DropTarget;
let reactstrap = require('reactstrap');
let Modal = reactstrap.Modal;
let ModalHeader = reactstrap.ModalHeader;
let ModalBody = reactstrap.ModalBody;
let ModalFooter = reactstrap.ModalFooter;
import { connect, Provider } from "react-redux";
import { combineForms, Form, Control } from "react-redux-form";
import { compose, createStore } from "redux";
import Select from 'react-select';

const initialUser = {};
const store = createStore(
    combineForms({
        user: initialUser
    })
);

class TutorDropDown extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categoryValue: "",
            options: [],
            valueToIndex: {},
        };
        this.handleSelectChange = this.handleSelectChange.bind(this);
        this.setUp = this.setUp.bind(this);
        this.setUp();
        this.props.setTutorId(null);
    }

    setUp() {
        let shiftId = this.props.shiftId;
        console.log("shiftId");
        console.log(shiftId);
        let tutorData = this.props.tutorData;
        console.log(tutorData);
        let index = 0;
        for (let i=0; i<tutorData.length; i++) {
            let prefer = tutorData[i].tutorPreferences.shiftPreferences.PREFER;
            console.log("prefer");
            console.log(prefer);
            for (let j=0; j<prefer.length; j++) {
                if (prefer[j].id == shiftId) {
                    let name = tutorData[i].firstName + " " + tutorData[i].lastName + ", Preferred"
                    this.state.options.push({
                        value: tutorData[i].id,
                        label: name
                    });
                    console.log("index before");
                    console.log(index);
                    this.state.valueToIndex[tutorData[i].id] = index;
                    index++;
                }
            }
            let willing = tutorData[i].tutorPreferences.shiftPreferences.WILLING;
            console.log("willing");
            console.log(willing);
            for (let j=0; j<willing.length; j++) {
                if (willing[j].id == shiftId) {
                    let name = tutorData[i].firstName + " " + tutorData[i].lastName + ", Willing";
                    this.state.options.push({
                        value: tutorData[i].id,
                        label: name
                    });
                    this.state.valueToIndex[tutorData[i].id] = index;
                    index++;
                }
            }
            console.log("this.state.options");
            console.log(this.state.options);
        }
    }

    handleSelectChange(value){
        console.log("in TutorDropDown.handleSelectChange");
        console.log("value");
        console.log(value);
        this.setState({ categoryValue: value });
        let valueList = value.split(",");
        if (valueList[0] == "") {
            this.props.setTutorId(null);
        }
        else {
            this.props.setTutorId(parseInt(valueList[0]));
            console.log(parseInt(value));
        }
    };

    render() {
        let reactSelect = props => (
            <Select
                {...props}
            />
        );
        console.log("this.state.categoryValue");
        console.log(this.state.categoryValue);
        // console.log(this.state.categoryValue.length);

        let options = this.state.options;
        let categoryValueList = this.state.categoryValue.split(",");
        console.log("categoryValueList");
        console.log(categoryValueList);
        if (categoryValueList[0] != "") {
            let value = parseInt(categoryValueList[0]);
            options = [options[this.state.valueToIndex[value]]];
        }
        console.log("options");
        console.log(options);

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
                    options={options}
                    onChange={this.handleSelectChange}
                    joinValues
                    name={this.props.model}
                    required
                />
            </div>
        );
    }
}

class CourseDropDown extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categoryValue: null,
            tutorId: this.props.tutorId,
            options: [],
            valueToIndex: {},
        };
        this.handleSelectChange = this.handleSelectChange.bind(this);
        this.props.setTutorCourse(null);
    }

    handleSelectChange(value){
        console.log("in CourseDropDown.handleSelectChange");
        console.log("value");
        console.log(value);
        this.setState({ categoryValue: value });
        let valueList = value.split(",");
        console.log("valueList");
        console.log(valueList);

        let courses = [];
        for (let i=0; i<valueList.length; i++) {
            value = valueList[i];
            if (value == "") {
                continue;
            }
            let index = this.state.valueToIndex[parseInt(value)];
            let option = this.state.options[index];
            courses.push({
                id : parseInt(value),
                name : option.label.split(",")[0]
            });
        }
        console.log("courses");
        console.log(courses);
        if (courses.length == 0) {
            this.props.setTutorCourse(null);
        }
        else {
            this.props.setTutorCourse(courses);
        }
    };

    render() {
        console.log("in CourseDropDown render");
        let tutorId = this.props.tutorId;
        let tutorData = this.props.tutorData;
        console.log("tutorId, tutorData");
        console.log(tutorId);
        console.log(tutorData);
        this.state.options = [];
        if (tutorId != null) {
            let index = 0;
            for (let i=0; i<tutorData.length; i++) {
                if (tutorId == tutorData[i].id) {
                    let prefer = tutorData[i].tutorPreferences.coursePreferences.PREFER;
                    for (let j=0; j<prefer.length; j++) {
                        let name = prefer[j].name + ", Preferred";
                        this.state.options.push({
                            value: prefer[j].id,
                            label: name
                        });
                        this.state.valueToIndex[prefer[j].id] = index;
                        index++;
                    }
                    let willing = tutorData[i].tutorPreferences.coursePreferences.WILLING;
                    for (let j=0; j<willing.length; j++) {
                        let name = willing[j].name + ", Willing";
                        this.state.options.push({
                            value: willing[j].id,
                            label: name
                        });
                        this.state.valueToIndex[willing[j].id] = index;
                        index++;
                    }
                    break;
                }
            }
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

class Container extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            cards: props.list,
            tutorData: this.props.tutorData,
            setContainerDataList: this.props.setContainerDataList.bind(this),
            printc: this.props.printc.bind(this),
            day: this.props.day,
            modal : false,
            saveMessageModal: false,
            deleteMessageModal: false,
            tutorId: null,
            tutorCourse: null,
            cardKey: 1000,
        };
        this.setCard = this.setCard.bind(this);
        this.toggleGridModal = this.toggleGridModal.bind(this);
        this.toggleDeleteMessageModal = this.toggleDeleteMessageModal.bind(this);
        this.toggleMessageModal = this.toggleMessageModal.bind(this);
        this.setTutorId = this.setTutorId.bind(this);
        this.setTutorCourse = this.setTutorCourse.bind(this);
        this.savePopUp = this.savePopUp.bind(this);
  	}

    toggleGridModal() {
        this.setState({
            modal : ! this.state.modal
        })
    }

    toggleDeleteMessageModal() {
        this.setState({
            deleteMessageModal: ! this.state.deleteMessageModal
        })
    }

    toggleMessageModal() {
        this.setState({
            saveMessageModal: ! this.state.saveMessageModal
        })
    }

  	setCard(index, card) {
        console.log("in setCard");
        let cards = this.state.cards;
        cards[index] = card;
        console.log("cards");
        console.log(cards);
        this.props.setContainerDataList(this.props.id - 1, cards);
    }

    deleteCard(index) {
        console.log("in deleteCard");
        this.state.cards.splice(index, 1);
        console.log(this.state.cards);
        this.props.printc();
        this.props.setContainerDataList(this.props.id - 1, this.state.cards);
        this.props.printc();
        console.log("exiting deleteCard");
    }

    handleSubmit(e){
        console.log(e);
    };

    setTutorId(id) {
        this.setState({
            tutorId : id
        })
    }

    setTutorCourse(courses) {
        this.setState({
            tutorCourse : courses
        })
    }

    savePopUp() {
        console.log("in save pop up");
        let tutorId = this.state.tutorId;
        let tutorData = this.state.tutorData;
        if (tutorId == null || this.state.tutorCourse == null) {
            return;
        }
        let tutor = null;
        for (let i=0; i<tutorData.length; i++) {
            if (tutorData[i].id == tutorId) {
                tutor = tutorData[i]
            }
        }
        this.state.cards.push({
            id: this.state.cardKey,
            tutor: {
                id: tutor.id,
                firstName: tutor.firstName,
                lastName: tutor.lastName
            },
            tutorCourse: this.state.tutorCourse
        });
        this.state.cardKey++;
        this.props.printc();
        this.props.setContainerDataList(this.props.id - 1, this.state.cards);
        this.props.printc();
        this.toggleGridModal();
        this.toggleMessageModal();
        console.log("exiting save pop up");
    }

	pushCard(card) {
        console.log("in pushCard");
		this.setState(update(this.state, {
			cards: {
				$push: [ card ]
			}
		}));

        this.props.printc();
        this.props.setContainerDataList(this.props.id - 1, this.state.cards);
        this.props.printc();
        console.log("exiting pushCard");
	}

	removeCard(index) {
        console.log("in removeCard");
		this.setState(update(this.state, {
			cards: {
				$splice: [
					[index, 1]
				]
			}
		}));

		console.log(this.state.cards);
        this.props.printc();
        this.props.setContainerDataList(this.props.id - 1, this.state.cards);
        this.props.printc();
        console.log("exiting removeCard");
	}

    moveCard(dragIndex, hoverIndex) {
        console.log("in moveCard");
        const { cards } = this.state;
        const dragCard = cards[dragIndex];

        this.setState(update(this.state, {
            cards: {
                $splice: [
                    [dragIndex, 1],
                    [hoverIndex, 0, dragCard]
                ]
            }
        }));

        this.props.printc();
        this.props.setContainerDataList(this.props.id - 1, this.state.cards);
        this.props.printc();
        console.log("exiting moveCard");
    }

    render(){
        const { cards } = this.state;
        const { canDrop, isOver, connectDropTarget } = this.props;
        const isActive = canDrop && isOver;
        const style = {
            width: "100%",
            height: "100%"
        };

        const backgroundColor = isActive ? 'lightgreen' : '#FFF';

        return connectDropTarget(
            <div id="container" style={{backgroundColor}}>
                {cards.map((card, i) => {
                    return (
                        <Card
                            key={card.id}
                            index={i}
                            listId={this.props.id}
                            card={card}
                            setCard={this.setCard}
                            day={this.state.day}
                            deleteCard={this.deleteCard.bind(this)}
                            removeCard={this.removeCard.bind(this)}
                            moveCard={this.moveCard.bind(this)}
                            tutorData={this.props.tutorData}
                            toggleDeleteMessageModal={this.toggleDeleteMessageModal}
                            />
                    );
                })}

                <button type="button" className="btn btn-success btn-block" onClick={this.toggleGridModal}
                        style={{background: "#c5050c", "textAlign": "center"}} > Add Assignment </button>

                <Modal isOpen={this.state.modal} toggle={this.toggleGridModal}>
                    <ModalHeader toggle={this.toggleGridModal} >
                        <div style={{"textAlign": "left", "fontSize": "40px"}}>
                            <h3>Shift {this.state.day}</h3>
                        </div>
                    </ModalHeader>
                    <ModalBody>
                        <Provider store={store}>
                            <div>
                                <h4>Add Tutor Assignment</h4>
                                <div>Select a tutor available for this shift</div>
                                <div>
                                    <Form model="user" onSubmit={this.handleSubmit}>
                                        <TutorDropDown model="user.category" tutorData={this.props.tutorData}
                                                       shiftId={this.props.shiftId} setTutorId={this.setTutorId}/>
                                    </Form>
                                </div>
                                <div>Select a course for the tutor</div>
                                <div>
                                    <Form model="user" onSubmit={this.handleSubmit}>
                                        <CourseDropDown model="user.category" tutorData={this.props.tutorData}
                                                        tutorId={this.state.tutorId}
                                                        setTutorCourse={this.setTutorCourse}/>
                                    </Form>
                                </div>
                            </div>
                        </Provider>
                    </ModalBody>
                    <ModalFooter>
                        <button type="button" className="btn btn-success btn-block" onClick={this.savePopUp}
                                style={{"textAlign": "center"}} > Save </button>
                        <button type="button" className="btn btn-success btn-block" onClick={this.toggleGridModal}
                                style={{"textAlign": "center"}} > Cancel </button>
                    </ModalFooter>
                </Modal>

                <Modal isOpen={this.state.deleteMessageModal} toggle={this.toggleDeleteMessageModal}>
                    <ModalHeader toggle={this.toggleDeleteMessageModal} >
                        <div style={{"textAlign": "center", "fontSize": "20px"}}>
                            Shift Deletion
                        </div>
                    </ModalHeader>
                    <ModalBody>
                        <div id="message-content"> Shift Successfully Deleted </div>
                    </ModalBody>
                </Modal>

                <Modal isOpen={this.state.saveMessageModal} toggle={this.toggleMessageModal}>
                    <ModalHeader toggle={this.toggleMessageModal} >
                        <div style={{"textAlign": "center", "fontSize": "20px"}}>
                            Add Tutor Assignment
                        </div>
                    </ModalHeader>
                    <ModalBody>
                        <div id="message-content"> Successfully Added </div>
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}

const cardTarget = {
	drop(props, monitor, component ) {
		const { id } = props;
		const sourceObj = monitor.getItem();
		if ( id !== sourceObj.listId ) component.pushCard(sourceObj.card);
		return {
			listId: id
		};
	}
};

module.exports = DropTarget("CARD", cardTarget, (connect, monitor) => ({
    connectDropTarget: connect.dropTarget(),
    isOver: monitor.isOver(),
    canDrop: monitor.canDrop()
}))(Container);
