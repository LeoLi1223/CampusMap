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

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import Map from "./Map";
import BuildingSelection from "./BuildingSelection";
import {Segment} from "./types";

interface AppState {
    shortestPath: Segment[] // a list of paths making up of the shortest path.
}

class App extends Component<{}, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
            shortestPath: []
        }
    }

    render() {
        return (
            <div>
                <h1 id="app-title">Find you route!</h1>
                <div id="separation-line"></div>
                <div>
                    <BuildingSelection onChange={(value) => {
                        this.setState({shortestPath: value});
                    }}/>
                </div>

                <div>
                    <Map shortestPath={this.state.shortestPath}/>
                </div>

            </div>
        );
    }

}

export default App;
