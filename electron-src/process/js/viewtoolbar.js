let React = require("react");
let electron = require("electron");
let ipc = electron.ipcRenderer;

class ViewToolbar extends React.Component {
    constructor(props) {
        super(props);
        this.clickTutorButton = this.clickTutorButton.bind(this);
        this.clickCourseButton = this.clickCourseButton.bind(this);
        this.clickViewButton = this.props.clickViewButton.bind(this);
    }

    clickTutorButton() {
        let tutorCard = document.getElementById("tutorCard");
        let courseCard = document.getElementById("courseCard");
        let text1 = document.getElementById("tutorText");
        let text2 = document.getElementById("courseText");

        tutorCard.style.backgroundColor = "#F20056";
        courseCard.style.backgroundColor = "#c41e3a";

        text1.style.color = "#ffffff";
        text2.style.color = "#ffffff";

        this.clickViewButton("tutor");
    }


    clickCourseButton() {
        let tutorCard = document.getElementById("tutorCard");
        let courseCard = document.getElementById("courseCard");
        tutorCard.style.backgroundColor = "#c41e3a";
        courseCard.style.backgroundColor = "#F20056";

        let text1 = document.getElementById("tutorText");
        let text2 = document.getElementById("courseText");
        text1.style.color = "#ffffff";
        text2.style.color = "#ffffff";

        this.clickViewButton("course");
    }
    render(){
        return (
            <div id="toolbar" className="card">
                <h3 className="card-header" style={{backgroundColor: "#c41e3a"}}>
                    <div style={{color:"#f7f7f7"}}>
                        Information
                    </div>
                </h3>

                <div id = "tutorCard" className = "card-body" onClick={this.clickTutorButton} style = {{backgroundColor: "#F20056"}}>
                    <h5 id="tutorText" style={{color:"#f7f7f7"}}>
                        <i className="fa fa-user" aria-hidden="true"></i>  Tutor
                    </h5>
                </div> {/*tool bar */}
                <div id = "courseCard" className = "card-body" onClick={this.clickCourseButton} style = {{backgroundColor: "#c41e3a"}}>
                    <h5 id="courseText" style={{color:"#f7f7f7"}}>
                        <i className="fa fa-book" aria-hidden="true"></i>  Course
                    </h5>
                </div>
            </div>
            )
    }
}

module.exports = ViewToolbar;
