
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

ipc.send("request_tutor_data");
ipc.send("request_course_data");
class MainInterface extends React.Component {
  constructor(props){
    super(props);
    this.state = {
       // tutors: loadTutors,
        tutorData: null,
        courseData: null,
        tutors: [],
      courses: [],
      view: "tutor" // view will only be tutor or course
    };

    this.clickViewButton = this.clickViewButton.bind(this);
    this.prepareView = this.prepareView.bind(this);
    ipc.on("get_tutor_data",  (event, text) => {
      let d = JSON.parse(text);
      let p = new Parser();
      this.setState({
          tutors: p.getTutors(d),
          tutorData: d
      });
    });

    ipc.on("get_course_data",  (event, text) => {
      let d = JSON.parse(text);
      let p = new Parser();
      this.setState({
          courses: p.getCourses(d),
          courseData: d
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
    ShowViewSchedules(){
        ipc.sendSync("ShowViewSchedules")
    }
    render() {

        return (
            <div className="container-fluid ">
                <div className="row">
                    <div className="col-3">
                        <ViewToolBar clickViewButton={this.clickViewButton}/>
                    </div>
                    <div className="col-9">
                        {/* <TutorTable tutors = {this.state.tutors}/> */}{/* Still thinking about how to load in CourseTable */}
                        {/*<Cour  seTable courses = {this.state.courses}/>*/}
                        {this.prepareView()}
                    </div>

                        <button type="button" className="btn btn-lg btn-success" id="generate-button-pos" onClick={this.ShowViewSchedules} > Generate Schedules! </button>

                </div>
            </div>

        )
    }
}

ReactDOM.render(
    <MainInterface />,
    document.getElementById("ViewTutors")
);
