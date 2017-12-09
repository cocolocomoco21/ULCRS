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
            schedules : loadSchedules,
            modal : false,
            saveMessage: "",
            saveMessageModal: false,
            exiting: false,
            index : 0,
            schedule : null
        };
        this.toggleSaveModal = this.toggleSaveModal.bind(this);
        this.toggleMessageModal = this.toggleMessageModal.bind(this);
        this.getSchedule = this.getSchedule.bind(this);
        this.exportSchedule = this.exportSchedule.bind(this);
        this.exit = this.exit.bind(this);
        this.toggleExiting = this.toggleExiting.bind(this);
        this.changeIndex = this.changeIndex.bind(this);
    }

    toggleExiting(){
        this.setState({
            exiting: !this.state.exiting
        })

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

    exportSchedule(value){
        let filename = document.getElementById("filename");
        if (value === 0) {
            this.toggleSaveModal();
        } else {
            this.toggleSaveModal();
            this.toggleMessageModal();
            if (value === 1) {
                this.state.saveMessage = "Session saved: " + filename.value;
            } else if (value === 2) {
                this.state.saveMessage = "Uploaded to server: " + filename.value;
            }
            ipc.send("save-session", filename.value, this.state.schedules[0]);
        }
    }

    exit(){
        ipc.send("kill-app");
    }

    render(){

        return (
            <div className="container-fluid">
                <div className="row" style={{height:"80%"}}>
                    <div className="col-2 p-0">
                        <ScheduleToolbar schedules={this.state.schedules} changeIndex={this.changeIndex}/>
                    </div>

                    <div className="col-10 pl-3 pr-1" style={{height:"100%"}}>
                        <ScheduleTable schedules={this.state.schedules} index={this.state.index}/>
                    </div>

                </div>
                <div className="row" style={{height:"20%"}}>
                    <div className="col-8">
                    </div>
                    <div className="col-2" style={{height:"100%"}}>
                        <div className="d-flex align-items-center justify-content-center">
                            <button type="button" className="btn btn-success btn-block" onClick={this.toggleSaveModal}
                                    style={{"textAlign": "center", width:"200px", height:"100px"}} > Save </button>
                        </div>
                    </div>
                    <div className="col-2">
                        <div className="d-flex align-items-center justify-content-center">
                            <button className="btn btn-danger btn-block" onClick={this.toggleExiting}
                                    style={{"textAlign": "center", width:"200px", height:"100px"}} > Exit </button>
                        </div>
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
