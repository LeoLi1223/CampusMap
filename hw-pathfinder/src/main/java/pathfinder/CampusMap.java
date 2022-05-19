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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {
    private Graph<Point, Double> campusMap;
    private Map<String, CampusBuilding> shortNameToBuilding;

    public CampusMap() {
        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");
        this.campusMap = new Graph<>();
        this.shortNameToBuilding = new HashMap<>();
        // Construct the graph of the campus.
        for (CampusBuilding building : buildings) {
            Point newNode = new Point(building.getX(), building.getY());
            campusMap.addNode(newNode);
            // store short names to corresponding CampusBuilding information.
            String shortName = building.getShortName();
            shortNameToBuilding.put(shortName, building);
        }
        for (CampusPath path : paths) {
            Point node1 = new Point(path.getX1(), path.getY1());
            Point node2 = new Point(path.getX2(), path.getY2());
            campusMap.addNode(node1);
            campusMap.addNode(node2);
            campusMap.addEdge(new Graph.Edge<>(node1, node2, path.getDistance()));
        }

    }

    @Override
    public boolean shortNameExists(String shortName) {
        return shortNameToBuilding.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName)) {
            throw new IllegalArgumentException("the short name provided does not exist.");
        }
        return shortNameToBuilding.get(shortName).getLongName();
    }

    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> namesMap = new HashMap<>();
        for (String shortName: shortNameToBuilding.keySet()) {
            namesMap.put(shortName, shortNameToBuilding.get(shortName).getLongName());
        }

        return namesMap;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if (startShortName == null || endShortName == null) {
            throw new IllegalArgumentException("building names cannot be null");
        }
        if (!shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException("the short name provided does not exist.");
        }
        // convert short names to Points
        Point start = new Point(shortNameToBuilding.get(startShortName).getX(),
                                shortNameToBuilding.get(startShortName).getY());
        Point end = new Point(shortNameToBuilding.get(endShortName).getX(),
                                shortNameToBuilding.get(endShortName).getY());
        return ShortestPath.dijkstra(campusMap, start, end);
    }

}
