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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        CampusMap map = new CampusMap();

        // Respond to a "GET" request being made to the server's "/findPath" endpoint.
        // Sends the shortest path between the given buildings in the Json format.
        Spark.get("/findPath", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String start = request.queryParams("start");
                String end = request.queryParams("end");

                if (!map.shortNameExists(start) || !map.shortNameExists(end)) {
                    Spark.halt(400, "Short names don't exist");
                }
                Path<Point> shortestPath = map.findShortestPath(start, end);

                Gson gson = new Gson();
                return gson.toJson(shortestPath);
            }
        });

        // Respond to a "GET" request being made to the server's "/getNames" endpoint.
        // Sends a list of buildings with its short and lone names in the Json format.
        Spark.get("/getNames", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Map<String, String> shortToLongNames = map.buildingNames();

                List<CampusBuilding> bldgs = new ArrayList<>();
                for (Map.Entry<String, String> bldg: shortToLongNames.entrySet()) {
                    String shortName = bldg.getKey();
                    String longName = bldg.getValue();
                    bldgs.add(new CampusBuilding(shortName, longName, 0, 0));
                }
                bldgs.sort(new Comparator<CampusBuilding>() {
                    @Override
                    public int compare(CampusBuilding o1, CampusBuilding o2) {
                        return o1.getLongName().compareTo(o2.getLongName());
                    }
                });
                Gson gson = new Gson();
                return gson.toJson(bldgs);
            }
        });
    }

}
