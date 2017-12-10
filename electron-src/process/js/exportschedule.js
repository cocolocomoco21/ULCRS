let ReactDOM = require('react-dom');
let React = require('react');

class DefaultForm extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <input className="form-control" id="filename" placeholder="File Name" style={{width:"25rem"}}></input>
        )
    }
}

class ErrorForm extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="form-group row has-danger">
                <div className="col-sm-10">
                    <input className="form-control form-control-danger" id="filename" placeholder="File Name" style={{width:"25rem"}}></input>
                        <small className="form-text text-muted">{this.props.errorMessage}</small>
                </div>
            </div>
        )
    }
}

class ExportSchedulePage extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            saveOption: 1,
            error : 0,
            errorMessage : ""
        }
        this.save = this.save.bind(this);
        this.cancel = this.cancel.bind(this);
        this.saveSession = this.saveSession.bind(this);
        this.uploadToServer = this.uploadToServer.bind(this);
        this.renderForm = this.renderForm.bind(this);
        this.exportSchedule = this.props.exportSchedule.bind(this);
    }

    saveSession() {
        this.state.saveOption = 1;
    }

    uploadToServer() {
        this.state.saveOption = 2;
    }

    save() {
        let errorM = this.exportSchedule(this.state.saveOption);
        if (errorM === "") {
            this.setState(
                {
                    error: 0
                }
            )
        } else {
            this.setState(
                {
                    error: 1,
                    errorMessage: errorM
                }
            )
        }
    }

    renderForm() {
        if (this.state.error === 0) {
            return (
                <DefaultForm/>
            )
        } else {
            return (
                <ErrorForm errorMessage = {this.state.errorMessage}/>
            )
        }
    }

    cancel() {
        this.exportSchedule(0);
    }

    render() {
        return (
            <div className="container">
                <div className="row justify-content-center mt-md-4">
                    <div className="form-group">
                        {this.renderForm()}
                    </div>
                </div>
                <div className="row justify-content-center mt-md-1">
                    <div className="custom-controls-stacked">
                        <label className="custom-control custom-radio">
                            <input id="radioStacked1" name="radio-stacked" type="radio" className="custom-control-input" onClick={this.saveSession} checked/>
                                <span className="custom-control-indicator"/>
                                <span className="custom-control-description">Save session</span>
                        </label>
                        <label className="custom-control custom-radio">
                            <input id="radioStacked2" name="radio-stacked" type="radio" className="custom-control-input" onClick={this.uploadToServer}/>
                                <span className="custom-control-indicator"/>
                                <span className="custom-control-description">Upload to server</span>
                        </label>
                    </div>
                </div>
                <div className="row justify-content-center mt-md-4 mb-md-4">
                    <div className="col">
                        <button type="button" className="btn btn-block btn-outline-success" id="center" onClick={this.save}> Save </button>
                    </div>
                    <div className="col">
                        <button type="button" className="btn btn-block btn-outline-secondary" id="center" data-dismiss="modal" onClick={this.cancel}> Back </button>
                    </div>
                </div>
            </div>
        )
    }
}
//
//
// ReactDOM.render(
//     <ExportSchedulePage />,
//     document.getElementById("ExportSchedule")
// );

module.exports = ExportSchedulePage;