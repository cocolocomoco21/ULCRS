let React = require("react");
let electron = eRequire("electron");
let ipc = electron.ipcRenderer;

class ViewToolbar extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            curView : this.props.curView
        };
        this.decideColor = this.decideColor.bind(this);
        this.clickTutorButton = this.clickTutorButton.bind(this);
        this.clickCourseButton =  this.clickCourseButton.bind(this);
        this.clickViewButton = this.props.clickViewButton.bind(this);
    }


    decideColor( view ) {
        if (this.state.curView === view) {
            return {backgroundColor: "#A9A9A9"};
        }
        return {backgroundColor: "#D3D3D3"};
    }

    clickTutorButton() {
        let card1 = document.getElementById("card1");
        let card2 = document.getElementById("card2");
        //card1.style.backgroundColor = "#A9A9A9";
        //card2.style.backgroundColor = "#D3D3D3";

        this.clickViewButton("tutor");
    }


    clickCourseButton() {
        let card1 = document.getElementById("card1");
        let card2 = document.getElementById("card2");
        //card1.style.backgroundColor = "#D3D3D3";
      //  card2.style.backgroundColor = "#A9A9A9";

        this.clickViewButton("course");
    }
    render(){
        return (
            <div id="toolbar" className="card">
                <h3 className="card-header">
                    Information
                </h3>

                <div id = "card1" className = "card-body" onClick={this.clickTutorButton} style = {this.decideColor("tutor")}>
                    <h5>
                        <i className="fa fa-user" aria-hidden="true"></i>  Tutor
                    </h5>
                </div> {/*tool bar */}
                <div id = "card2" className = "card-body" onClick={this.clickCourseButton} style = {this.decideColor("course")}>
                    <h5>
                        <i className="fa fa-book" aria-hidden="true"></i>  Course
                    </h5>
                </div>
            </div>
            )
    }
}

module.exports = ViewToolbar;