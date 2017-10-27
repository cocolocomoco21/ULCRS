
let ReactDOM = require('react-dom');
let React = require('react');
let ToolBar = require('./toolbar');

class MainInterface extends React.Component {
    render() {
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-4">
                        <ToolBar />
                    </div>

                    <div className="col-8">

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