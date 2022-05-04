package graph;

import java.util.List;

/**
 * A Graph is a mutable directed labeled graph. A typical Graph consists of
 * a set of identical nodes, which are represented as strings, and a set of edges.
 * For example,
 *      G = (nodes, edges) where nodes = {n1, n2, ...} and edges = {e1, e2, ...}
 */
public class Graph {
    // Representation not shown

    /**
     * Construct a new Graph instance.
     * @spec.effects makes an empty graph.
     */
    public Graph() {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Add a node to the graph.
     * @param node the new node being added to this graph
     * @spec.requires nodeName != null
     * @spec.modifies this
     * @spec.effects add the node to this graph, if no node with this name already exists in this graph
     */
    public void addNode(String node) {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Add an edge to the graph.
     * @param edge the new edge being added to this graph
     * @spec.requires edge != null &amp;&amp; edge.parent and edge.child are in this graph
     * @spec.modifies this
     * @spec.effects add the edge to this graph, if no edge with the same label has the same parent and child nodes
     */
    public void addEdge(Edge edge) {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Returns a list of node in the graph.
     * @return all nodes in this graph
     */
    public List<String> getNodes() {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Return a list of child nodes of the given node.
     * @param parent the node whose children are looked for
     * @spec.requires parent != null
     * @throws java.util.NoSuchElementException if the parent node doesn't exist in this graph
     * @return all child nodes of the given parent node
     */
    public List<String> getChildren(String parent) {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Return a list of parent nodes of the given node
     * @param child the node whose parents are looked for
     * @spec.requires child != null
     * @throws java.util.NoSuchElementException if the child node doesn't exist in this graph
     * @return all parent nodes of the given child node
     */
    public List<String> getParents(String child) {
        throw new RuntimeException("Not Yet Implemented");
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
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Return a list of edges starting from the given node.
     * @param node the starting node
     * @spec.requires node != null
     * @throws java.util.NoSuchElementException if the node doesn't exist in this graph
     * @return a list of edges starting from the given node
     */
    public List<Edge> getEdgesFrom(String node) {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Return a list of edges pointing to the given node.
     * @param node the destination node
     * @spec.requires node != null
     * @throws java.util.NoSuchElementException if the node doesn't exist in this graph
     * @return a list of edges pointing to the given node
     */
    public List<Edge> getEdgesTo(String node) {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * Remove the edge equal to the given edge.
     * @param edge the edge being removed
     * @spec.requires edge != null
     * @spec.modifies this
     * @spec.effects remove the same edge in the graph, if exists
     */
    public void removeEdge(Edge edge) {
        throw new RuntimeException("Not Yet Implemented");
    }

    /**
     * A Node represents a mutable node that can be added into the graph.
     * A typical node has a value.
     */
//     public static class Node {
//        /**
//         * Construct a new Node instance.
//         * @param value value for the new node
//         * @spec.requires value != null
//         * @spec.effects makes a new node with the given value
//         */
//        public Node(String value) {
//            throw new RuntimeException("Not Yet Implemented");
//        }
//
//        /**
//         * Return the value associated with the node.
//         * @return this.value
//         */
//        public String getValue() {
//            throw new RuntimeException("Not Yet Implemented");
//        }
//
//        /**
//         * Set the node's value to the give value.
//         * @param newValue the new value for the node
//         * @spec.requires newValue != null
//         * @spec.modifies this
//         * @spec.effects this.value = newValue
//         */
//        public void setValue(String newValue) {
//            throw new RuntimeException("Not Yet Implemented");
//        }
//
//        @Override
//        public int hashCode() {
//            throw new RuntimeException("Not Yet Implemented");
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            throw new RuntimeException("Not Yet Implemented");
//        }
//    }

    /**
     * An Edge is a mutable directed labeled edge, pointing from a source to a destination.
     * A typical Edge is a pair of nodes (node1, node2) with a label, representing
     * a labeled edge pointing from node1 to node2.
     */
     public static class Edge {

        /**
         * Construct a new Edge instance.
         * @param parent the source from which the new edge starts
         * @param child the destination in which the new edge ends
         * @param label the label for the new edge
         * @spec.requires source != null &amp;&amp; dest != null &amp;&amp; label != null
         * @spec.effects make a new edge from start to end with the given label
         */
        public Edge(String parent, String child, String label) {
            throw new RuntimeException("Not Yet Implemented");
        }

        /**
         * Return the parent node of this edge.
         * @return this.parent
         */
        public String getParent() {
            throw new RuntimeException("Not Yet Implemented");
        }

        /**
         * Return the child node of this edge.
         * @return this.child
         */
        public String getChild() {
            throw new RuntimeException("Not Yet Implemented");
        }

        /**
         * Return the label of this edge.
         * @return this.label
         */
        public String getLabel() {
            throw new RuntimeException("Not Yet Implemented");
        }

        /**
         * Set the label of this edge to the given label.
         * @param newLabel the new label for this edge
         * @spec.requires newLabel != null
         * @spec.modifies this
         * @spec.effects this.label = newLabel
         */
        public void setLabel(String newLabel) {
            throw new RuntimeException("Not Yet Implemented");
        }

        @Override
        public int hashCode() {
            throw new RuntimeException("Not Yet Implemented");
        }

        @Override
        public boolean equals(Object obj) {
            throw new RuntimeException("Not Yet Implemented");
        }
    }
}
