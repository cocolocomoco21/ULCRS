let ReactDOM = require('react-dom');
let React = require('react');

class TutorTable extends React.Component {
    render() {
        return (

            <div className="card card-inverse" style={{background: "#ffbf00", border: "#333"}}>
                <div className="card-block">
                    <h3 className="card-header">
                        Tutor
                    </h3>

                    <div className = "card-body">
                        <div className="card-title"> hello </div>
                    </div> {/*tool bar */}
                </div>
            </div>
        )
    }
}

/*
<h3 className="card-header">
    Tutor
</h3>*/

module.exports = TutorTable;