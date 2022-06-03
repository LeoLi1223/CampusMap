import {Component} from "react";
import {Building, Segment, ShortestPath} from "./types";

interface BuildingSelectionProps {
    onChange(value: Segment[]): void
}

interface BuildingSelectionState {
    startValue?: string, // the short name for the starting building
    endValue?: string, // the short name for the end building
    shortestPath?: ShortestPath, // the ShortestPath object
    options: Building[], // the list of Buildings consisting of short and long names
    alertMessage?: string // message when user does unexpected selection
}

class BuildingSelection extends Component<BuildingSelectionProps, BuildingSelectionState> {

    constructor(props: any) {
        super(props);
        this.state = {
            startValue: "",
            endValue: "",
            shortestPath: undefined,
            options: [], // list of buildings representing the options of the dropdown menus
            alertMessage: undefined
        }
        this.getBldgNames() // update the options list
    }

    /**
     * update the startValue or endValue in the state when making a selection.
     */
    handleChange = (event: any) => {
        if (event.target.id === "start-select") {
            let new_state = {
                startValue: event.target.value,
            }
            this.setState(new_state);
        } else {
            let new_state = {
                endValue: event.target.value,
            }
            this.setState(new_state);
        }
    }

    /**
     * The function is called when the reset button is clicked.
     * Return all states except the options state to the initial states.
     */
    resetClicked = () => {
        let new_state = {
            startValue: "",
            endValue: "",
            shortestPath: undefined,
            alertMessage: undefined
        };
        this.setState(new_state);
        this.props.onChange([]);
    }

    /**
     * The function is called when the draw button is clicked.
     * It makes a request to the Spark server and gets a Json object of ShortestPath,
     * which consists of the starting position, list of paths and a total cost.
     * Updates the ShortestPath state.
     */
    drawClicked = async() => {
        try {
            if (!this.checkSelect(this.state.startValue, this.state.endValue)) {
                return;
            }

            let start = this.state.startValue;
            let end = this.state.endValue;

            let response = await fetch("http://localhost:4567/findPath?start=" + start + "&end=" + end);
            let shortestPath = (await response.json()) as ShortestPath;

            this.setState({shortestPath: shortestPath});
            this.props.onChange(shortestPath.path);
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    /**
     * The function is called when the reverse button is clicked.
     * Switches the start and end building and updates the startValue, endValue and ShortestPath states.
     */

    reverseClicked = async() => {
        if (!this.checkSelect(this.state.startValue, this.state.endValue)) {
            return;
        }

        let start = this.state.endValue;
        let end = this.state.startValue;

        let shortestPath = this.state.shortestPath!;

        shortestPath.path[0] = {
            start: shortestPath.path[0].end,
            end: shortestPath.path[0].start,
            cost: shortestPath.path[0].cost
        }
        shortestPath.path[shortestPath.path.length-1] = {
            start: shortestPath.path[shortestPath.path.length-1].end,
            end: shortestPath.path[shortestPath.path.length-1].start,
            cost: shortestPath.path[shortestPath.path.length-1].cost
        }
        shortestPath.path = shortestPath.path.reverse();

        this.setState({
            startValue: start,
            endValue: end,
            shortestPath: shortestPath
        });
        this.props.onChange(shortestPath.path);
    }

    /**
     *  Checks whether the selections are legal.
     */
    checkSelect(start:string | undefined, end: string | undefined) {
        if (!start && !end) {
            this.setState({alertMessage: "Please select starting and end buildings!"})
            return false;
        }
        if (!start) {
            this.setState({alertMessage: "Please select a starting building!"})
            return false;
        }
        if (!end) {
            this.setState({alertMessage: "Please select an end building!"})
            return false;
        }
        if (start === end) {
            this.setState({alertMessage: "Please select different starting and end buildings!"})
            return false;
        }
        return true;
    }

    /**
     * This function makes a request to the Spark server with "/getNames" path and gets a list of
     * Buildings with short and long names. Updates the options state.
     */
    getBldgNames = async() => {
        try {
            let response = await fetch("http://localhost:4567/getNames");
            let bldgs = (await response.json()) as Building[];
            this.setState({options: bldgs});
        } catch (e) {
            console.log(e);
        }
    }

    /**
     * Returns a list of Option HTML elements used to populate the dropdown menus.
     */
    makeOptions() {
        let options: JSX.Element[] = [];
        let idx = 1
        for (const building of this.state.options) {
            options.push(
                <option value={building.shortName} key={idx}>{building.longName} ({building.shortName})</option>
            );
            idx++;
        };
        return options;
    }

    /**
     * Returns a <p> tag used to show alert messages on the page.
     */
    showAlertMessage() {
        if (this.state.alertMessage !== undefined) {
            return <p>{this.state.alertMessage}</p>;
        }
    }

    render() {
        return (
            <div id="building-selection">
                <div id="select-group">
                    From
                    <br/>
                    <select id="start-select" value={this.state.startValue}
                            onChange={this.handleChange}>
                        <option value="">Select Start Building</option>
                        {this.makeOptions()}
                    </select>
                    <br/>
                    To
                    <br/>
                    <select id="end-select" value={this.state.endValue}
                            onChange={this.handleChange}>
                        <option value="">Select End Building</option>
                        {this.makeOptions()}
                    </select>
                </div>

                <div id="button-group">
                    <button onClick={this.drawClicked}>Draw</button>
                    <button onClick={this.resetClicked}>Reset</button>
                    <button onClick={this.reverseClicked}>Reverse</button>
                </div>

                <div id="alert-message">
                    {this.showAlertMessage()}
                </div>
            </div>
        )
    }
}

export default BuildingSelection;