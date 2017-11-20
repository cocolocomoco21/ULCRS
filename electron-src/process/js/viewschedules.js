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
            modal : false
        };
        this.toggleSaveModal = this.toggleSaveModal.bind(this);
    }

    toggleSaveModal(){
        this.setState({
            modal : ! this.state.modal
        })
    }

    ShowExportSchedule() {
        //ipc.sendSync("ShowExportSchedule");
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
                        <ExportSchedulePage />
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