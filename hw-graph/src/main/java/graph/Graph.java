package graph;

import java.util.*;

/**
 * A Graph is a mutable directed labeled graph. A typical Graph consists of
 * a set of identical nodes, which are represented as strings, and a set of edges.
 * For example,
 *      G = (nodes, edges) where nodes = {n1, n2, ...} and edges = {e1, e2, ...}
 */
public class Graph {
    // RI: no null values.
    //     no identical values in adjacencyList.
    // AF(this) = a map with nodes of this.adjacencyList.keys and edges of this.adjacencyList.values.
    private Map<String, List<Edge>> adjacencyList;

    public static final boolean DEBUG = true;

    private void checkRep() {
        if (DEBUG) {
            for (String node: adjacencyList.keySet()) {
                List<Edge> edges = adjacencyList.get(node);
                for (int i = 0; i < edges.size(); i++) {
                    if (edges.get(i) == null) {
                        throw new RuntimeException("Edges cannot be null");
                    }
                    if (i != edges.lastIndexOf(edges.get(i))) {
                        throw new RuntimeException("There cannot be identical edges in the map.");
                    }
                }
            }
        }
    }

    /**
     * Construct a new Graph instance.
     * @spec.effects makes an empty graph.
     */
    public Graph() {
        adjacencyList = new HashMap<>();
        checkRep();
    }

    /**
     * Add a node to the graph.
     * @param node the new node being added to this graph
     * @spec.requires nodeName != null
     * @spec.modifies this
     * @spec.effects add the node to this graph, if no node with this name already exists in this graph
     */
    public void addNode(String node) {
        checkRep();
        adjacencyList.put(node, new LinkedList<>());
        checkRep();
    }

    /**
     * Add an edge to the graph.
     * @param edge the new edge being added to this graph
     * @spec.requires edge != null &amp;&amp; edge.parent and edge.child are in this graph
     * @spec.modifies this
     * @spec.effects add the edge to this graph, if no edge with the same label has the same parent and child nodes
     */
    public void addEdge(Edge edge) {
        checkRep();

        String parent = edge.getParent();
        if (!adjacencyList.get(parent).contains(edge)) {
            adjacencyList.get(parent).add(edge);
        }

        checkRep();
    }

    /**
     * Returns a list of node in the graph.
     * @return all nodes in this graph
     */
    public List<String> getNodes() {
        return List.copyOf(adjacencyList.keySet());
    }

    /**
     * Return a list of child nodes of the given node.
     * @param parent the node whose children are looked for
     * @spec.requires parent != null
     * @throws java.util.NoSuchElementException if the parent node doesn't exist in this graph
     * @return all child nodes of the given parent node
     */
    public List<String> getChildren(String parent) {
        checkRep();
        if (!adjacencyList.containsKey(parent)) {
            throw new NoSuchElementException("The given parent node doesn't exist in the map");
        }
        List<String> children = new ArrayList<>();
        for (Edge edge : adjacencyList.get(parent)) {
            children.add(edge.getChild());
        }
        return children;
    }

    /**
     * Return a list of parent nodes of the given node
     * @param child the node whose parents are looked for
     * @spec.requires child != null
     * @throws java.util.NoSuchElementException if the child node doesn't exist in this graph
     * @return all parent nodes of the given child node
     */
    public List<String> getParents(String child) {
        checkRep();
        if (!adjacencyList.containsKey(child)) {
            throw new NoSuchElementException("The given child node doesn't exist in the map");
        }
        List<String> parents = new ArrayList<>();

        List<Edge> edgesTo = getEdgesTo(child);
        for (Edge edge: edgesTo) {
            if (!parents.contains(edge.getParent())) {
                parents.add(edge.getParent());
            }
        }

        return parents;
    }

    /**
     * Return a list of edges pointing from the given parent node to the given child node.
     * @param parent the starting node of expected edges
     * @param child the end node of expected edges
     * @spec.requires parent != null &amp;&amp; child != null
     * @throws java.util.NoSuchElementException if the child or parent doesn't exist in this graph
     * @return a list of edges from the parent node to the child node
     */
    public List<Edge> getEdges(String parent, String child) {
        checkRep();
        if (!adjacencyList.containsKey(parent)) {
            throw new NoSuchElementException("The given parent node doesn't exist in the map");
        }
        if (!adjacencyList.containsKey(child)) {
            throw new NoSuchElementException("The given child node doesn't exist in the map");
        }
        List<Edge> edges = new ArrayList<>();
        for (Edge edge: adjacencyList.get(parent)) {
            if (edge.getChild().equals(child)) {
                edges.add(edge);
            }
        }
        return edges;
    }

    /**
     * Return a list of edges starting from the given node.
     * @param node the starting node
     * @spec.requires node != null
     * @throws java.util.NoSuchElementException if the node doesn't exist in this graph
     * @return a list of edges starting from the given node
     */
    public List<Edge> getEdgesFrom(String node) {
        checkRep();
        if (!adjacencyList.containsKey(node)) {
            throw new NoSuchElementException("The given node doesn't exist in the map");
        }
        return adjacencyList.get(node);
    }

    /**
     * Return a list of edges pointing to the given node.
     * @param node the destination node
     * @spec.requires node != null
     * @throws java.util.NoSuchElementException if the node doesn't exist in this graph
     * @return a list of edges pointing to the given node
     */
    public List<Edge> getEdgesTo(String node) {
        checkRep();
        if (!adjacencyList.containsKey(node)) {
            throw new NoSuchElementException("The given child node doesn't exist in the map");
        }
        List<Edge> edges = new ArrayList<>();
        for (String parent: adjacencyList.keySet()) {
            if (!parent.equals(node)) {
                for (Edge edge : adjacencyList.get(parent)) {
                    if (edge.getChild().equals(node)) {
                        edges.add(edge);
                    }
                }
            }
        }
        return edges;
    }

    /**
     * Remove the edge equal to the given edge.
     * @param edge the edge being removed
     * @spec.requires edge != null
     * @spec.modifies this
     * @spec.effects remove the same edge in the graph, if exists
     */
    public void removeEdge(Edge edge) {
        checkRep();
        adjacencyList.get(edge.getParent()).remove(edge);
        checkRep();
    }

    /**
     * An Edge is a mutable directed labeled edge, pointing from a source to a destination.
     * A typical Edge is a pair of nodes (node1, node2) with a label, representing
     * a labeled edge pointing from node1 to node2.
     */
     public static class Edge {
        // RI: parent != null && child != null && label != null
        // AF(this) = an edge from parent to child with label

        private String parent;
        private String child;
        private String label;

        /**
         * Construct a new Edge instance.
         * @param parent the source from which the new edge starts
         * @param child the destination in which the new edge ends
         * @param label the label for the new edge
         * @spec.requires source != null &amp;&amp; dest != null &amp;&amp; label != null
         * @spec.effects make a new edge from start to end with the given label
         */
        public Edge(String parent, String child, String label) {
            this.parent = parent;
            this.child = child;
            this.label = label;
        }

        private void checkRep() {
            if (parent == null || child == null || label == null) {
                throw new RuntimeException("Illegal edge occurs");
            }
        }

        /**
         * Return the parent node of this edge.
         * @return this.parent
         */
        public String getParent() {
            return parent;
        }

        /**
         * Return the child node of this edge.
         * @return this.child
         */
        public String getChild() {
            return child;
        }

        /**
         * Return the label of this edge.
         * @return this.label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Set the label of this edge to the given label.
         * @param newLabel the new label for this edge
         * @spec.requires newLabel != null
         * @spec.modifies this
         * @spec.effects this.label = newLabel
         */
        public void setLabel(String newLabel) {
            checkRep();
            this.label = newLabel;
            checkRep();
        }

        @Override
        public int hashCode() {
            return parent.hashCode() ^ child.hashCode() ^ label.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof Edge)) {
                return false;
            }
            Edge e = (Edge) obj;
            return parent.equals(e.parent) && child.equals(e.child) && label.equals(e.label);
        }
    }
}
