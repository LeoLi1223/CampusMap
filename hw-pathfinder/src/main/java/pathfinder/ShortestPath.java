package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

public class ShortestPath {

    /**
     * Return the shortest Path between two nodes where edge labels are floating point numbers.
     * @param graph the graph where the path is looking for
     * @param start the start node of the path
     * @param end   the destination of the path
     * @param <V>   the type of nodes
     * @spec.requires graph != null &amp;&amp; start != null &amp;&amp; end != null
     *                start and end nodes are in the graph.
     * @return  the Path with the lowest cost connecting the two nodes.
     *          Return null if there's no path from start to end.
     */
    public static <V> Path<V> dijkstra(Graph<V, Double> graph, V start, V end) {
        // The priority is the cost of the path.
        PriorityQueue<Path<V>> active = new PriorityQueue<>(new Comparator<Path<V>>() {
            @Override
            public int compare(Path<V> o1, Path<V> o2) {
                return Double.compare(o1.getCost(), o2.getCost());
            }
        });
        // Add a path from the start to itself with cost of 0 initially.
        active.add(new Path<>(start));
        // set of nodes to which the shortest path is known
        Set<V> finished = new HashSet<>();

        while (!active.isEmpty()) {
            Path<V> minPath = active.remove();
            V minDest = minPath.getEnd();

            if (minDest.equals(end)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }

            List<Graph.Edge<V, Double>> edgesFrom = graph.getEdgesFrom(minDest);
            for (Graph.Edge<V, Double> edge : edgesFrom) {
                V child = edge.getChild();
                if (!finished.contains(child)) {
                    Path<V> newPath = minPath.extend(child, edge.getLabel());
                    active.add(newPath);
                }
            }

            finished.add(minDest);
        }

        return null;
    }
}
