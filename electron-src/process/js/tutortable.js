let requireLocal = ( localModule ) =>{
    let path = require("path");
    return require(path.resolve( __dirname, localModule))
};
let ReactDOM = require('react-dom');
let React = require('react');
console.log(__dirname);
let TutorEntry = requireLocal('./tutorentry');

class TutorTable extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            tutors : this.props.tutors
        };

        this.excludedIds = this.props.excludedIds;
        this.changeExcludedIds = this.changeExcludedIds.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
                tutors: nextProps.tutors
            })
    }

    changeExcludedIds(id, mode){
        if (mode === "add"){
            this.excludedIds.add(id);
            for(let i = 0; i < this.state.tutors.length; i++){
                let tutor = this.state.tutors[i];
                if (tutor.id === id){
                    tutor.notInclude = true;
                }
            }

        }else if (mode === "remove") {
            this.excludedIds.delete(id);
            for(let i = 0; i < this.state.tutors.length; i++){
                let tutor = this.state.tutors[i];
                if (tutor.id === id){
                    tutor.notInclude = false;
                }
            }
        }
    }

    render() {
        let tutors = this.state.tutors.map(function(item, index){
            return(
                <TutorEntry notIncludeHandler = {this.changeExcludedIds}
                            key = {index}
                            singleItem = {item}
                />
            )
        }.bind(this));
        return (
            <div className="card card-inverse" style={{height:"100%", width:"100%", background: "#c5050c", border: "#ffffff", color: "#ffffff"}}>
                <div className="card-block" style={{height:"100%"}}>
                    <h3 className="card-header">
                        Review your tutors
                    </h3>

                    <div className = "card-body p-0" style={{height:"90%"}}>
                        <div className="container-fluid table-scroll p-0 m-0" style={{height:"100%"}}>
                        <table className="table table-striped p-0" style={{background: "#c5050c", border: "#ffffff", color: "#ffffff"}}>
                            <thead className="thead" style={{backgroundColor: "#9b0000",
                                color: "#f9f9f9"}}>
                            <tr>
                                <th>ID</th>
                                <th className="no-wrap">Exclude</th>
                                <th className="no-wrap">First Name</th>
                                <th className="no-wrap">Last Name</th>
                                <th className="no-wrap">Status</th>
                                <th className="no-wrap">Courses Preferred</th>
                                <th className="no-wrap">Courses Willing</th>
                                <th className="no-wrap">Shifts Preferred</th>
                                <th className="no-wrap">Shifts Willing</th>
                                <th className="no-wrap">Shift Freq. Preferred</th>
                                <th className="no-wrap">Shift Freq. Willing</th>
                            </tr>
                            </thead>
                            <tbody>
                            {tutors}
                            </tbody>
                        </table>
                        </div>
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
