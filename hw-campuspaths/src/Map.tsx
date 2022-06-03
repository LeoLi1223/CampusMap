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

import {LatLngExpression} from "leaflet";
import React, { Component } from "react";
import {CircleMarker, MapContainer, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import {
    UW_LATITUDE,
    UW_LATITUDE_CENTER, UW_LATITUDE_OFFSET, UW_LATITUDE_SCALE,
    UW_LONGITUDE,
    UW_LONGITUDE_CENTER,
    UW_LONGITUDE_OFFSET,
    UW_LONGITUDE_SCALE
} from "./Constants";
import {Segment} from "./types";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

// NOTE: This component is a suggestion for you to use, if you would like to. If
// you don't want to use this component, you're free to delete it or replace it
// with your hw-lines Map

/**
 * Converts x coordinate to longitude
 */
function xToLon(x: number): number {
    return UW_LONGITUDE + (x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE;
}

/**
 * Converts y coordinate to latitude
 */
function yToLat(y: number): number {
    return UW_LATITUDE + (y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE;
}

interface MapProps {
    shortestPath: Segment[]
}

interface MapState {}

class Map extends Component<MapProps, MapState> {

  // Returns the starting building of the shortestPath.
  getStart() {
      return this.props.shortestPath[0].start;
  }

  // Returns the end building of the shortestPath.
  getEnd() {
      let len = this.props.shortestPath.length;
      return this.props.shortestPath[len - 1].end;
  }

  // Returns a list of MapLines components as well as two CircleMarker components indicating the
  // starting and end buildings.
  addMapLines() {
      let mapLines: JSX.Element[] = [];
      let idx = 1;
      for (let segment of this.props.shortestPath) {
          mapLines.push(
              <MapLine key={"line" + idx} color="blue" x1={segment.start.x} y1={segment.start.y}
                                                        x2={segment.end.x} y2={segment.end.y}/>
          )
          if (idx === 1) {
              mapLines.push(
                  <CircleMarker key="start-marker" center={[yToLat(this.getStart().y), xToLon(this.getStart().x)]}
                                radius={5} color="green"/>
              )
          }
          if (idx === this.props.shortestPath.length) {
              mapLines.push(
                  <CircleMarker key="end-marker" center={[yToLat(this.getEnd().y), xToLon(this.getEnd().x)]}
                  radius={5} color="red"/>
              )
          }
          idx++;
      }

      return mapLines;
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
            // <MapLine key="key1" color="red" x1={1000} y1={1000} x2={2000} y2={2000}/>
            // will draw a red line from the point 1000,1000 to 2000,2000 on the
            // map. Note that key should be a unique key that only this MapLine has.
              this.addMapLines()
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
