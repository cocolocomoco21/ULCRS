
let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');
let TutorTable = require('./tutortable');
let electron = eRequire ("electron");

class MainInterface extends React.Component {
    render() {
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-4">
                        <ViewToolBar />
                    </div>

                    <div className="col-8">
                        <TutorTable /> {/* Still thinking about how to load in CourseTable */}
                    </div>
                </div>
            </div>

        )
    }
}

ReactDOM.render(
    <MainInterface />,
    document.getElementById("ViewTutors")
);