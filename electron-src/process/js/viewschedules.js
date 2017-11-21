let ReactDOM = require('react-dom');
let React = require('react');
let electron = eRequire('electron');
let ipc = electron.ipcRenderer;

let ScheduleToolbar = require('./scheduletoolbar');
let ScheduleTable = require('./scheduletable');
let fs = eRequire('fs');
let loadSchedules = JSON.parse(fs.readFileSync(scheLocation));


let ExportSchedulePage = require('./exportschedule');
let reactstrap = require('reactstrap');
let Modal = reactstrap.Modal;
let ModalHeader = reactstrap.ModalHeader;
let ModalBody = reactstrap.ModalBody;


console.log(loadSchedules);
class ViewSchedulePage  extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            schedules : loadSchedules,
            modal : false,
            saveMessage: "",
            saveMessageModal: false
        };
        this.toggleSaveModal = this.toggleSaveModal.bind(this);
        this.toggleMessageModal = this.toggleMessageModal.bind(this);
        this.exportSchedule = this.exportSchedule.bind(this);
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

    exportSchedule(value){
        if (value === 0) {
            this.toggleSaveModal();
        } else {
            this.toggleSaveModal();
            this.toggleMessageModal();
            if (value === 1) {
                this.state.saveMessage = "Session saved!";
            } else if (value === 2) {
                this.state.saveMessage = "Uploaded to server!";
            }
        }
    }


    render() {

        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-2">
                        <ScheduleToolbar />
                    </div>

                    <div className="col-8">
                        <ScheduleTable schedules={this.state.schedules}/>
                    </div>

                    <div className="col-2">
                        
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

                <button type="button" className="btn btn-lg btn-success" id="generate-button-pos" onClick={this.toggleSaveModal} > Save </button>
            </div>

        )
    }
}

// ReactDOM.render(
//     <ViewSchedulePage />,
//     document.getElementById("ViewSchedule")
// );

module.exports = ViewSchedulePage;