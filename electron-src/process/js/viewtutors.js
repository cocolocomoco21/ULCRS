
let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');
let TutorTable = require('./tutortable');
let fs = eRequire('fs');
let loadTutors = JSON.parse(fs.readFileSync(dataLocation));
class MainInterface extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      tutors: loadTutors
      //this.state.tutors[0]
    };
  }
    render() {

      //let tutors = this.state.tutors.map(function(item, index){
      //  return(
    //    <TutorTable key = {index}
    //      singleItem = {item}
    //    />
    //    )
    //  }.bind(this));
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-4">
                        <ViewToolBar />
                    </div>

                    <div className="col-8">
                        <TutorTable singleItem = {this.state.tutors[0]}/> {/* Still thinking about how to load in CourseTable */}
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
