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

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import {ColoredEdge} from "./types";
import edgeList from "./EdgeList";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
    parsedEdges: ColoredEdge[];
  // so you can render them here
}

interface MapState {}

class Map extends Component<MapProps, MapState> {

  // convert the array of ColoredEdges to an array of MapLines
  to_MapLines() {
      let mapLineList: JSX.Element[] = []
      for (let edge of this.props.parsedEdges) {
          mapLineList.push(
              <MapLine color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2} key={edge.key}/>
          )
      }
      return mapLineList;
  }

  render() {
    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {
            // <MapLine key={key1} color="red" x1={1000} y1={1000} x2={2000} y2={2000}/>
            // will draw a red line from the point 1000,1000 to 2000,2000 on the
            // map
            this.to_MapLines()
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
