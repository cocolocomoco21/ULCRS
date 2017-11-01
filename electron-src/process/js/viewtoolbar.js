let React = require("react");
let electron = eRequire("electron");
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

        tutorCard.style.backgroundColor = "#d9edf7";
        courseCard.style.backgroundColor = "#5bc0de";

        text1.style.color = "#0055cc";
        text2.style.color = "#f7f7f7";

        this.clickViewButton("tutor");
    }


    clickCourseButton() {
        let tutorCard = document.getElementById("tutorCard");
        let courseCard = document.getElementById("courseCard");
        tutorCard.style.backgroundColor = "#5bc0de";
        courseCard.style.backgroundColor = "#d9edf7";

        let text1 = document.getElementById("tutorText");
        let text2 = document.getElementById("courseText");
        text1.style.color = "#f7f7f7";
        text2.style.color = "#0055cc";

        this.clickViewButton("course");
    }
    render(){
        return (
            <div id="toolbar" className="card">
                <h3 className="card-header">
                    Information
                </h3>

                <div id = "tutorCard" className = "card-body" onClick={this.clickTutorButton} style = {{backgroundColor: "#d9edf7"}}>
                    <h5 id="tutorText" style={{color:"#0055cc"}}>
                        <i className="fa fa-user" aria-hidden="true"></i>  Tutor
                    </h5>
                </div> {/*tool bar */}
                <div id = "courseCard" className = "card-body" onClick={this.clickCourseButton} style = {{backgroundColor: "#5bc0de"}}>
                    <h5 id="courseText" style={{color:"#f7f7f7"}}>
                        <i className="fa fa-book" aria-hidden="true"></i>  Course
                    </h5>
                </div>
            </div>
            )
    }
}

module.exports = ViewToolbar;
