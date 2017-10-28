let React = require("react");

class ViewToolbar extends React.Component {
    render(){
        return (
            <div id="toolbar" className="card">
                <h3 className="card-header">
                    Information
                </h3>

                <div className = "card-body">
                    <div className="card-title"> hello </div>
                    <button type="button" className="btn btn-info"> Button</button>
                </div> {/*tool bar */}
            </div>
            )
    }
}

module.exports = ViewToolbar;