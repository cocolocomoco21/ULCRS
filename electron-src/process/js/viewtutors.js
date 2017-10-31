
let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');
let TutorTable = require('./tutortable');
let fs = eRequire('fs');
let loadTutors = JSON.parse(fs.readFileSync(dataLocation));
let mock = JSON.parse(fs.readFileSync(mockData));
let electron = eRequire('electron');
let ipc = electron.ipcRenderer;
let Parser = require("./parser");


ipc.send("request_data");
class MainInterface extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      tutors: loadTutors,
      data: null,
      parser: null
    };
  }
    prepareInfo(){
      if (this.state.data === null){
          return "placeholder"
      }
      return this.state.data
    }
    render() {
        ipc.on("get_data",  (event, text) => {
            this.setState({
                data: text,
                parser: new Parser(mock)
            });
        });

        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-4">
                        <ViewToolBar />
                    </div>
                    <div className="col-8">
                        <TutorTable singleItem = {this.state.tutors[0]}/> {/* Still thinking about how to load in CourseTable */}
                    </div>

                    <div>TEST</div>
                </div>
            </div>

        )
    }
}

ReactDOM.render(
    <MainInterface />,
    document.getElementById("ViewTutors")
);
