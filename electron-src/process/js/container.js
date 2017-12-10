let Card = require('./Card.js');
let React = require('react');
let update = require('react-addons-update');

let DropTarget = require('react-dnd').DropTarget;


class Container extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            cards: props.list,
            tutorData: this.props.tutorData,
            setContainerDataList: this.props.setContainerDataList.bind(this),
            printc: this.props.printc.bind(this),
            day: this.props.day
        };
        this.setCard = this.setCard.bind(this);
  	}

  	setCard(index, card) {
        console.log("in setCard");
        let cards = this.state.cards;
        cards[index] = card;
        console.log("cards");
        console.log(cards);
        this.props.setContainerDataList(this.props.id - 1, cards);
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
                            removeCard={this.removeCard.bind(this)}
                            moveCard={this.moveCard.bind(this)}
                            tutorData={this.props.tutorData}
                            />
                    );
                })}
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
