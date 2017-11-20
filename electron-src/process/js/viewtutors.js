
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
let ViewSchedulePage = require("./viewschedules");

let Reactstrap = require("reactstrap");
let Modal = Reactstrap.Modal;
let ModalHeader = Reactstrap.ModalHeader;
let ModalBody= Reactstrap.ModalBody;
let ModalFooter = Reactstrap.ModalFooter;
let Button = Reactstrap.Button;



class ViewInfo extends React.Component {
  constructor(props){
    super(props);
      this.state = {
       // tutors: loadTutors,
        tutorData: null,
        courseData: null,
        tutors: [],
      courses: [],
      view: "tutor", // view will only be tutor or course
      proceeding: false
    };

    this.clickViewButton = this.clickViewButton.bind(this);
    this.prepareView = this.prepareView.bind(this);
    this.proceedToGenerate = this.proceedToGenerate.bind(this);
    this.toggleProceeding = this.toggleProceeding.bind(this);
    ipc.on("get_tutor_data",  (event, text) => {
      let d = JSON.parse(text);
      let p = new Parser();
      this.setState({
          tutors: p.getTutors(d),
          tutorData: d,
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
      ipc.send("request_tutor_data");
      ipc.send("request_course_data");

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

    toggleProceeding(){
        this.setState({
            proceeding: ! this.state.proceeding
        })
    }

    proceedToGenerate(){
        this.props.showSchedules();
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
                        {/*<CourseTable courses = {this.state.courses}/>*/}
                        {this.prepareView()}
                    </div>

                    <button type="button" className="btn btn-lg btn-success" id="generate-button-pos" onClick={this.toggleProceeding} > Generate Schedules! </button>

                    <Modal isOpen={this.state.proceeding}>
                        <ModalHeader>
                            Action
                        </ModalHeader>

                        <ModalBody>
                            Do you want to proceed the generation?
                        </ModalBody>

                        <ModalFooter>
                            <Button color="primary" onClick={this.proceedToGenerate}>
                                Yes
                            </Button>

                            <Button color="second" onClick={this.toggleProceeding}>
                                Cancel
                            </Button>
                        </ModalFooter>
                    </Modal>


                </div>
            </div>

        )
    }
}


class MainInterface extends React.Component{

    constructor(props){
        super(props);

        this.state = {
            pageName : "info",
            waiting: false
        };

        ipc.on("receiveScheduleData", (event, data) => {
            this.setState ({
                pageName : "schedules",
                waiting: false
            });
        });

        this.showViewSchedules = this.showViewSchedules.bind(this);
    }

    showViewSchedules(){
        this.setState({
                waiting: true
            }
        );
        ipc.send("showViewSchedules")
    }

    componentWillUpdate(){
        console.log("Update");
    }

    render(){
        let component = 0;
        console.log("Rendering");
        console.log(this.state.pageName);
        if (this.state.pageName === "info"){
            component = <ViewInfo showSchedules = {this.showViewSchedules}/>
        }
        else if (this.state.pageName === "schedules"){
            component = <ViewSchedulePage/>
        }

        return(
            <div>
                {component}

                <Modal isOpen={this.state.waiting}>
                    <ModalHeader >

                        <div style={{"textAlign": "center", "fontSize": "40px"}}>
                            Schedule generating
                        </div>

                    </ModalHeader>
                    <ModalBody>
                        <div style={{"textAlign": "center"}}>
                            <i className="fa fa-spinner fa-spin" style={{"fontSize":"48px"}}/>
                        </div>

                    </ModalBody>
                </Modal>

            </div>
        )

    }
}

ReactDOM.render(
    <MainInterface />,
    document.getElementById("ViewTutors")
);
