//let ReactDOM = require('react-dom');
let React = require('react');
let ReactDnD = require('react-dnd');
let Html5Backend = require('react-dnd-html5-backend');

let Container = require('./Container'); // droppable container
let ScheduleToolbar = require('./scheduletoolbar');
let ScheduleTable = require('./scheduletable');
let fs = eRequire('fs');
let loadSchedules = JSON.parse(fs.readFileSync(dataLocation));

console.log(loadSchedules);
class ViewSchedulePage  extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : loadSchedules
        }
    }
    render() {
      const style = {
			display: "flex",
			justifyContent: "space-around",
			paddingTop: "20px"
		}
      let containerHeadings = [], containerContent = [];
      for (let col = 0; col < this.state.schedules.length; col++){
          let shiftContainer = [];
          containerHeadings.push(<th className="text-center">{this.state.schedules[col].Shift}</th>);
          for(let row = 0; row < this.state.schedules[col].Data.length; row++){
            shiftContainer.push({id: row,
              name: this.state.schedules[col].Data[row].Name
            course: this.state.schedules[col].Data[row].Course});
          }
          containerContent.push(shiftContainer);
      }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-2">
                        <ScheduleToolbar />
                    </div>

                    <div className="col-8">
                      <div style={{...style}}>
                        <Container id={1} list={containerContent[1]} />
                        <Container id={2} list={containerContent[2]} />
                        <Container id={3} list={containerContent[3]} />
                      </div>
                    </div>

                    <div className="col-2">

                    </div>
                </div>
            </div>

        )
    }
}
module.exports = DragDropContext(HTML5Backend)(ViewSchedulePage);
//export default DragDropContext(HTML5Backend)(ViewSchedulePage);
