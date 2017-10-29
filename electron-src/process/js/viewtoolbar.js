let React = require("react");
let electron = eRequire("electron");
let ipc = electron.ipcRenderer;

class ViewToolbar extends React.Component {
    //myFunction() {
    //    document.getElementById("card1").style.color = "blue";
    //}
    render(){
        return (
            <div id="toolbar" className="card">
                <h3 className="card-header">
                    Information
                </h3>

                <div id = "card1" type = "button" className = "card-body" onClick={function(){document.getElementById("card1").style.backgroundColor = "blue";}} style = {{background: "#A9A9A9"}}>
                    <h1>皮</h1>
                </div> {/*tool bar */}
                <div id = "card2" type = "button" className = "card-body" onClick={function(){document.getElementById("card2").style.backgroundColor = "green";}} style = {{background: "#A9A9A9"}}>
                    <h1>骚</h1>
                </div>
            </div>
            )
    }
}

module.exports = ViewToolbar;