let requireLocal = ( localModule ) =>{
    let path = require("path");
    return require(path.resolve( __dirname, './js',localModule))
};

// let requireLocal = ( localModule ) =>{
//     let path = require("path");
//     return require(path.resolve( __dirname,localModule))
// };

let dataLocation = require('path').resolve(__dirname, '..','..', 'data', 'data.json');
let mockData = require('path').resolve(__dirname, '..', 'data', 'mockTutorData2.json');
let mockCourses= require('path').resolve(__dirname, '..', 'data', 'mockCourseData.json');
let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = requireLocal('./viewtoolbar');
let TutorTable = requireLocal('./tutortable');
let CourseTable = requireLocal('./coursetable');
let fs = require('fs');
//let loadTutors = JSON.parse(fs.readFileSync(dataLocation));
let loadCourses = JSON.parse(fs.readFileSync(mockCourses));
let mock = JSON.parse(fs.readFileSync(mockData));
let electron = require('electron');
let ipc = electron.ipcRenderer;
let Parser = requireLocal("./parser");
let ViewSchedulePage = requireLocal("./viewschedules");

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
        // courseData: null,
        courseData: null,

          tutors: (new Parser()).getTutors(mock),
      courses: (new Parser()).getCourses(loadCourses),
      view: "tutor", // view will only be tutor or course
      proceeding: false,
          showNotAuthorized: false,
          loading: true
    };

    this.clickViewButton = this.clickViewButton.bind(this);
    this.prepareView = this.prepareView.bind(this);
    this.proceedToGenerate = this.proceedToGenerate.bind(this);
    this.toggleProceeding = this.toggleProceeding.bind(this);
    this.toggleAuthorized = this.toggleAuthorized.bind(this);
    ipc.on("get-tutor-data",  (event, text) => {


        try {
            let d = JSON.parse(text);
            let p = new Parser();
            this.setState({
                tutors: p.getTutors(d),
                tutorData: d,
                loading:false
            });
        }catch (e) {
            this.setState({
                showNotAuthorized:true,
                loading:false
            })
        }
    });

    this.excludedIds = new Set();
    ipc.on("get-course-data",  (event, text) => {
      let d = JSON.parse(text);
      let p = new Parser();
      this.setState({
          courses: p.getCourses(d),
          courseData: d
      });
    });

      ipc.send("request-tutor-data");
      ipc.send("request-course-data");

    ipc.on("post_success", (event, text)=>{

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
          return  <TutorTable tutors = {this.state.tutors} excludedIds = {this.excludedIds}/>
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
        });
    }

    proceedToGenerate(){
        this.props.showSchedules(this.excludedIds);
        console.log(this.excludedIds)
    }

    toggleAuthorized(){
        this.setState({
            showNotAuthorized: false
        })
    }

    render() {
        return (
            <div className="container-fluid ">
                <div className="row" style={{height:"80%"}}>
                    <div className="col-2 pr-0 pl-0">
                        <ViewToolBar clickViewButton={this.clickViewButton}/>
                    </div>
                    <div className="col-10 pl-0 " style={{height:"100%"}}>
                        {/* <TutorTable tutors = {this.state.tutors}/> */}{/* Still thinking about how to load in CourseTable */}
                        {/*<Cour  seTable courses = {this.state.courses}/>*/}
                        <div className="row" style={{margin: 0,height:"100%", width: "100%"}}>
                            {this.prepareView()}
                        </div>
                    </div>

                    <button color= "#0479a8" type="button" className="btn btn-lg" id="generate-button-pos" onClick={this.toggleProceeding} >
                            Generate Schedules!

                    </button>

                    <Modal isOpen={this.state.proceeding}>
                        <ModalHeader>
                            Generate Schedules
                        </ModalHeader>

                        <ModalBody>
                            Do you want to proceed with generating schedules?
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

                    <Modal isOpen={this.state.loading}>
                        <ModalHeader >

                            <div style={{"textAlign": "center", "fontSize": "40px"}}>
                                Loading data
                            </div>

                        </ModalHeader>
                        <ModalBody>
                            <div style={{"textAlign": "center"}}>
                                <i className="fa fa-spinner fa-spin" style={{"fontSize":"48px"}}/>
                            </div>

                        </ModalBody>
                    </Modal>

                    <Modal isOpen={this.state.showNotAuthorized}>
                        <ModalHeader >

                            <div style={{"textAlign": "center", "fontSize": "40px"}}>
                                Warning
                            </div>

                        </ModalHeader>
                        <ModalBody>
                            <div style={{"textAlign": "center"}}>
                                You are not authorized to view data.
                            </div>

                        </ModalBody>

                        <ModalFooter>
                            <Button color="second" onClick={this.toggleAuthorized}>
                                OK
                            </Button>
                        </ModalFooter>
                    </Modal>



                </div>
                <div className="row" style={{height:"20%"}}>
                </div>
            </div>

        )
    }
}


class MainInterface extends React.Component{

    constructor(props) {
        super(props);

        this.state = {
            pageName: "info",
            waiting: false,
            tutorData: null
        };

        ipc.on("receive-schedule-data", (event, data) => {
            this.setState({
                pageName: "schedules",
                waiting: false
            });
        });
        ipc.on("post_success", (event, data) => {
            this.setState({
                    waiting: true
                }
            );
        });
        this.showViewSchedules = this.showViewSchedules.bind(this);
    }

    showViewSchedules(excludedIds){
        ipc.send("post_generate", excludedIds);
        console.log("post generate")
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
            <div className="container-fluid fill" >
                <div className="row " style={{height:"100%"}}>
                    {component}
                </div>
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
