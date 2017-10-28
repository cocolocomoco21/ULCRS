let ReactDOM = require('react-dom');
let React = require('react');
let ViewToolBar = require('./viewtoolbar');

class StartPage extends React.Component{

    render(){
        return (
        <div className="container-fluid">

            <div className="row justify-content-center">
                <div className="col-4">
                    <div>
                        ULCRS
                        <button type="button" className="btn btn-info"> Start </button>
                    </div>
                </div>
            </div>
        </div>

        )
    }
}
ReactDOM.render( <StartPage/> ,
    document.getElementById("Start") );
