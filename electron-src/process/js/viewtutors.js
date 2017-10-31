
let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');
let TutorTable = require('./tutortable');
let CourseTable = require('./coursetable');
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
      // tutors: loadTutors,
      tutors: [],
      courses: [],
      data: null,
      parser: null,
      view: "tutor" // view will only be tutor or course
    };
    this.clickViewButton = this.clickViewButton.bind(this);
    this.prepareView = this.prepareView.bind(this);
      ipc.on("get_data",  (event, text) => {
          let p = new Parser(mock);
          this.setState({
              data: text,
              parser: p,
              courses: p.getCourses()
          });
      });
  }

  clickViewButton(v){
      if (this.state.view === v){
          return;
      }

      this.setState({
          view: v
       });
  }

  prepareView(){
      if (this.state.view === "tutor"){
          return  <TutorTable tutors = {this.state.tutors} />
      }
      return  <CourseTable courses = {this.state.courses} />

  }
    prepareInfo(){
      if (this.state.data === null){
          return "placeholder"
      }
      return this.state.data
    }
    render() {

        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-4">
                        <ViewToolBar clickViewButton={this.clickViewButton} curView = {this.state.view}/>
                    </div>
                    <div className="col-8">
                        {/* <TutorTable tutors = {this.state.tutors}/> */}{/* Still thinking about how to load in CourseTable */}
                        {/*<CourseTable courses = {this.state.courses}/>*/}
                        {this.prepareView()}
                    </div>
                </div>
            </div>

        )
    }
}

ReactDOM.render(
    <MainInterface />,
    document.getElementById("ViewTutors")
);
