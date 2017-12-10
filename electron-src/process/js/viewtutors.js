let requireLocal = ( localModule ) =>{
    let path = require("path");
    return require(path.resolve( __dirname, './js',localModule))
};

// let requireLocal = ( localModule ) =>{
//     let path = require("path");
//     return require(path.resolve( __dirname,localModule))
// };

let dataLocation = require('path').resolve(__dirname, '..','..', 'data', 'data.json');
let mockData = require('path').resolve(__dirname, '..', 'data', 'mockTutorData.json');
let mockCourses= require('path').resolve(__dirname, '..', 'data', 'mockCourseData.json');
let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = requireLocal('./viewtoolbar');
let TutorTable = requireLocal('./tutortable');
let CourseTable = requireLocal('./coursetable');
let fs = require('fs');
//let loadTutors = JSON.parse(fs.readFileSync(dataLocation));
let loadCourses = JSON.parse(fs.readFileSync(mockCourses));
let scheLocation = require('path').resolve(__dirname, '..','data', 'scheduleData.json');
let sche = JSON.parse(fs.readFileSync(scheLocation));
// let loadTutors = JSON.parse(fs.readFileSync(dataLocation));
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

      //     tutors: (new Parser()).getTutors(mock),
      // courses: (new Parser()).getCourses(loadCourses),
        tutors: [],
        courses:[],
      view: "tutor", // view will only be tutor or course
      proceeding: false,
          showNotAuthorized: false,
          // TODO: turn it to true when I push
          loading: true,
          existing: true
    };

    this.clickViewButton = this.clickViewButton.bind(this);
    this.prepareView = this.prepareView.bind(this);
    this.proceedToGenerate = this.proceedToGenerate.bind(this);
    this.toggleProceeding = this.toggleProceeding.bind(this);
    this.toggleAuthorized = this.toggleAuthorized.bind(this);
    this.toggleExiting = this.toggleExiting.bind(this);
    this.exit = this.exit.bind(this);
    ipc.on("get-tutor-data",  (event, text) => {


        try {
            let d = JSON.parse(text);
            let p = new Parser();
            this.props.setTutorData(d);
            this.setState({
                tutors: p.getTutors(d),
                tutorData: d,
                loading:false
            });
        }catch (e) {
            //TODO: decomment it when push
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

    toggleExiting(){
        this.setState({
            exiting: !this.state.exiting
        })
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

    exit(){
        ipc.send("kill-app");
    }

    render() {
        return (
            <div className="container-fluid ">
                <div className="row" style={{height:"85%"}}>
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
                <div className="row" style={{height:"15%"}}>
                    <div className="col-2 d-flex align-items-center justify-content-center">
                        <button className="btn btn-exit btn-sm" onClick={this.toggleExiting}
                                style={{"textAlign": "center", width:"100%", height:"60%",
                                    fontSize: "1.7rem"
                                }} >
                            Exit </button>
                    </div>
                    <div className="col-6">
                    </div>

                    <div className="col-4 d-flex justify-content-center align-items-center" style={{height:"100%"}}>
                        <button type="button" className="btn btn-success btn-lg" onClick={this.toggleProceeding}
                                style={{"textAlign": "center", width:"100%", height:"60%"}} > Generate Schedules!</button>
                    </div>

                </div>

                <Modal isOpen={this.state.exiting} toggle={this.toggleExiting} >
                    <ModalHeader toggle={this.toggleExiting}>
                        Warning
                    </ModalHeader>
                    <ModalBody>
                        <div> Are you sure you want to exit ULCRS? </div>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={this.exit}>Exit</Button>{' '}
                        <Button color="secondary" onClick={this.toggleExiting}>Cancel</Button>
                    </ModalFooter>
                </Modal>
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
            tutorData: null,
            scheduleData: null
        };
        this.tutorData = mock;
        ipc.on("receive-schedule-data", (event, data) => {
            this.setState({
                pageName: "schedules",
                waiting: false,
                scheduleData: sche
            });
        });
        ipc.on("post_success", (event, data) => {
            this.setState({
                    waiting: true
                }
            );
        });
        this.showViewSchedules = this.showViewSchedules.bind(this);
        this.setTutorData = this.setTutorData.bind(this);
    }

    showViewSchedules(excludedIds){
        ipc.send("post_generate", excludedIds);
        console.log("post generate")
    }

    setTutorData(data){
        //this.tutorData = data;
    }

    componentWillUpdate(){
        console.log("Update");
    }

    render(){
        let component = 0;
        console.log("Rendering");
        console.log(this.state.pageName);
        if (this.state.pageName === "info"){
            component = <ViewInfo showSchedules = {this.showViewSchedules} setTutorData = {this.setTutorData}/>
        }
        else if (this.state.pageName === "schedules"){
            component = <ViewSchedulePage scheduleData={this.state.scheduleData} tutorData={this.tutorData}/>
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
