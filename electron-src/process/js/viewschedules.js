let requireLocal = ( localModule ) =>{
    let path = require("path");
    return require(path.resolve( __dirname, localModule))
};

let ReactDOM = require('react-dom');
let React = require('react');
let electron = require('electron');
let ipc = electron.ipcRenderer;
let ScheduleToolbar = requireLocal('./scheduletoolbar');
let ScheduleTable = requireLocal('./scheduletable');
let fs = require('fs');

let scheLocation = require('path').resolve(__dirname, '..', '..','data', 'scheduleData.json');
let loadSchedules = JSON.parse(fs.readFileSync(scheLocation));

let Parser = requireLocal('parser');

let ExportSchedulePage = requireLocal('./exportschedule');
let reactstrap = require('reactstrap');
let Modal = reactstrap.Modal;
let ModalHeader = reactstrap.ModalHeader;
let ModalBody = reactstrap.ModalBody;
let ModalFooter = reactstrap.ModalFooter;
let Button = reactstrap.Button;

console.log(loadSchedules);
class ViewSchedulePage  extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            // schedules : loadSchedules,
            schedules:props.scheduleData,
            modal : false,
            saveMessage: "",
            saveMessageModal: false,
            exiting: false,
            index : 0,
            schedule : null
        }
        this.toggleSaveModal = this.toggleSaveModal.bind(this);
        this.toggleMessageModal = this.toggleMessageModal.bind(this);
        this.getSchedule = this.getSchedule.bind(this);
        this.updateSchedule = this.updateSchedule.bind(this);
        this.exportSchedule = this.exportSchedule.bind(this);
        this.exit = this.exit.bind(this);
        this.toggleExiting = this.toggleExiting.bind(this);
        this.changeIndex = this.changeIndex.bind(this);

        this.isAlphanum = this.isAlphanum.bind(this);
        this.handleNullScheduleData = this.handleNullScheduleData.bind(this);
    }

    isAlphanum(value) {
        for(var i=0; i<value.length; i++)
        {
            var char1 = value.charAt(i);
            var cc = char1.charCodeAt(0);

            if((cc>47 && cc<58) || (cc>64 && cc<91) || (cc>96 && cc<123))
            {
            }
            else {
                return false;
            }
        }
        return true;
    }

    toggleExiting() {
        this.setState({
            exiting: !this.state.exiting
        })

    }
    componentWillReceiveProps(nextProps){
        if (nextProps.scheduleData !== null){
            this.setState(
                {schedules:nextProps.scheduleData}
            )
        }
    }

    toggleSaveModal(){
        this.setState({
            modal : ! this.state.modal
        })
    }

    toggleMessageModal(){
        this.setState({
            saveMessageModal: ! this.state.saveMessageModal
        })
    }

    changeIndex(v) {
        this.setState({
            index : v
        })
    }

    getSchedule(input) {
        this.state.schedule = input;
    }

    updateSchedule(s) {
        this.state.schedule = s;
    }

    exportSchedule(value){
        if (value === 0) {
            this.toggleSaveModal();
        }
        let filename = document.getElementById("filename");
        let errorMessage = "";
        // Check filename
        if (filename.value.length >= 20) {
            value = -1;
            errorMessage = "Please enter less than or equal to 20 characters";
        } else if (!this.isAlphanum(filename.value)) {
            value = -1;
            errorMessage = "Filename only allows numbers and/or letters";
        }
        // Process save
        if(value === -1) {
            return errorMessage;
        }
        this.toggleSaveModal();
        if (value === 1) {
            this.state.saveMessage = "Session saved: " + filename.value;
            this.toggleMessageModal();
            ipc.send("save-session", filename.value, this.state.schedule);
        } else if (value === 2) {
            this.state.saveMessage = "Uploaded to server: " + filename.value;
            this.toggleMessageModal();
            ipc.send("save-session", filename.value, this.state.schedule);
        }
        return errorMessage;
    }

    exit(){
        ipc.send("kill-app");
    }

    handleNullScheduleData(scheduleData){
        if (scheduleData=== null || scheduleData.length === 0){
            return (<div></div>)
        }

        return (<ScheduleTable schedules={this.state.schedules} updateSchedule={this.updateSchedule} index={this.state.index} tutorData={this.props.tutorData}/>)
    }

    render(){

        return (
            <div className="container-fluid">
                <div className="row" style={{height:"85%"}}>
                    <div className="col-2 p-0">
                        <ScheduleToolbar schedules={this.state.schedules} changeIndex={this.changeIndex}/>
                    </div>

                    <div className="col-10 pl-3 pr-1" style={{height:"100%"}}>
                        {this.handleNullScheduleData(this.state.schedules)}
                    </div>

                </div>
                <div className="row" style={{height:"15%"}}>
                    <div className="col-2 d-flex align-items-center justify-content-center">
                        <button className="btn btn-exit btn-sm" onClick={this.toggleExiting}
                                style={{"textAlign": "center", width:"100%", height:"60%",
                                        fontSize: "1.7rem"
                                        }} >
                                Exit
                        </button>
                    </div>
                    <div className="col-6">
                    </div>
                    <div className="col-4 d-flex justify-content-center align-items-center" style={{height:"100%"}}>
                        <button type="button" className="btn btn-lg" onClick={this.toggleSaveModal}
                                style={{"textAlign": "center", width:"100%", height:"60%"}} > Save this Schedule</button>
                    </div>
                </div>
                <Modal isOpen={this.state.modal} toggle={this.toggleSaveModal}>

                    <ModalHeader toggle={this.toggleSaveModal} >
                        <div style={{"textAlign": "center", "fontSize": "40px"}}>
                            Save/Export Schedule
                        </div>
                    </ModalHeader>

                    <ModalBody>
                        <ExportSchedulePage exportSchedule={this.exportSchedule}/>
                    </ModalBody>
                </Modal>

                <Modal isOpen={this.state.saveMessageModal} toggle={this.toggleMessageModal}>
                    <ModalHeader toggle={this.toggleMessageModal} >
                        <div style={{"textAlign": "center", "fontSize": "20px"}}>
                            Message
                        </div>
                    </ModalHeader>
                    <ModalBody>
                        <div id="message-content"> {this.state.saveMessage} </div>
                    </ModalBody>
                </Modal>

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
//
// ReactDOM.render(
//     <ViewSchedulePage />,
//     document.getElementById("ViewSchedule")
// );

module.exports = ViewSchedulePage;
