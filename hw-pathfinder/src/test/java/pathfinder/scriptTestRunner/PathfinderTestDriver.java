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

package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.ShortestPath;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> graph = graphs.get(graphName);

        graph.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);
        Double cost = Double.parseDouble(edgeLabel);

        addEdge(graphName, parentName, childName, cost);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double cost) {

        Graph<String, Double> graph = graphs.get(graphName);

        graph.addEdge(new Graph.Edge<>(parentName, childName, cost));
        output.println("added edge " + String.format("%.3f", cost) + " from " + parentName
                + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        String head = graphName + " contains:";
        Graph<String, Double> graph = graphs.get(graphName);
        List<String> nodes = graph.getNodes(); // getNodes() returns an unmodifiable list which cannot be sorted.
        List<String> copy = new ArrayList<>(nodes);
        Collections.sort(copy);
        for (String name : copy) {
            head += " " + name;
        }
        output.println(head);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        String head = "the children of " + parentName + " in " + graphName + " are:";
        Graph<String, Double> graph = graphs.get(graphName);
        List<Graph.Edge<String, Double>> edgesFrom = graph.getEdgesFrom(parentName);
        edgesFrom.sort((o1, o2) -> {
            int compareLabel = o1.getLabel().compareTo(o2.getLabel());
            int compareChild = o1.getChild().compareTo(o2.getChild());
            return compareChild != 0 ? compareChild : compareLabel;
        });
        for (Graph.Edge<String, Double> edge : edgesFrom) {
            String child = edge.getChild();
            Double label = edge.getLabel();
            head += " " + child + "(" + label + ")";
        }
        output.println(head);
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String startName = arguments.get(1);
        String endName = arguments.get(2);
        findPath(graphName, startName, endName);
    }

    private void findPath(String graphName, String startName, String endName) {
        Graph<String, Double> graph = graphs.get(graphName);
        if (!graph.getNodes().contains(startName) && !graph.getNodes().contains(endName)) {
            output.println("unknown: " + startName);
            output.println("unknown: " + endName);
        } else if (!graph.getNodes().contains(startName)) {
            output.println("unknown: " + startName);
        } else if (!graph.getNodes().contains(endName)) {
            output.println("unknown: " + endName);
        } else {
            output.println("path from " + startName  + " to " + endName + ":");
            // Get the shortest path from start to end.
            Path<String> path = ShortestPath.dijkstra(graph, startName, endName);
            if (path == null) {
                output.println("no path found");
            } else {
                Iterator<Path<String>.Segment> iterator = path.iterator();
                while (iterator.hasNext()) {
                    Path<String>.Segment segment = iterator.next();
                    String start = segment.getStart();
                    String end = segment.getEnd();
                    Double cost = segment.getCost();
                    String weight = String.format(" with weight %.3f", cost);
                    output.println(start + " to " + end + weight);
                }
                output.println(String.format("total cost: %.3f", path.getCost()));
            }
        }
    }


    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
