/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import {ColoredEdge} from "./types";

interface EdgeListProps {
    onChange(edges: ColoredEdge[]): void;  // called when a new edge list is ready
                                 // once you decide how you want to communicate the edges to the App, you should
                                 // change the type of edge, so it isn't `any`
}

interface EdgeListState {
    value: string
    // parsedEdges: ColoredEdge[]
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeListState> {
    constructor(props: any) {
        super(props);
        this.state = {
            value: '',
            // parsedEdges: []
        }
    }

    // update the current-stored value to the change.
    handleInputChange = (event: any) => {
        let new_state = {
            value: event.target.value,
        }
        this.setState(new_state);
    }

    // the function when the draw button is clicked
    // parse the text contents into an array of ColoredEdges.
    // store the array of ColoredEdges in the state
    drawClicked = () => {
        const edges: string = this.state.value;
        let new_parsedEdges: ColoredEdge[] = [];
        let edgeList: string[] = edges.split("\n");
        let line = 1; // keep track of the line number
        for (let edge of edgeList){
            let props: string[] = edge.split(/\s+/);
            if (props.length !== 5) {
                alert("Invalid number of arguments the edge on line " + line + "!");
            } else {
                let x1_: number = parseFloat(props[0])
                let y1_: number = parseFloat(props[1])
                let x2_: number = parseFloat(props[2])
                let y2_: number = parseFloat(props[3])
                let color_ = props[4];
                if (isNaN(x1_) || isNaN(x2_) || isNaN(y1_) || isNaN(y2_)) {
                    alert("Cannot convert coordinates to numbers on line " + line + "!");
                } else if (x1_ < 0 || x1_ > 4000 || x2_ < 0 || x2_ > 4000 ||
                            y1_ < 0 || y1_ > 4000 || y2_ < 0 || y2_ > 4000) {
                    alert("Coordinates are beyond range on line " + line + ". \nCoordinates should be within [0, 4000].")
                } else{
                    new_parsedEdges.push(
                        { color: color_,
                            x1: x1_,
                            y1: y1_,
                            x2: x2_,
                            y2: y2_,
                            key: line }
                    )
                }
            }
            line++;
        }
        // this.setState({
        //     parsedEdges: new_parsedEdges
        // });
        this.props.onChange(new_parsedEdges);
    }

    // the function when the clear button is clicked
    // remove the internal state parsedEdges, but the text contents are not removed.
    clearClicked = () => {
        let new_parsedEdges: ColoredEdge[] = [];
        // this.setState({
        //     parsedEdges: new_parsedEdges
        // })
        this.props.onChange(new_parsedEdges);
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    // onChange should update the value in the text box
                    onChange={this.handleInputChange}
                    value= {this.state.value}
                /> <br/>
                <button onClick={this.drawClicked}>Draw</button>
                <button onClick={this.clearClicked}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
