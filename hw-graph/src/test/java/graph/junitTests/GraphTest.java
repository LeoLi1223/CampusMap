package graph.junitTests;

import graph.Graph;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * The class is for unit tests for Graph class.
 */
public class GraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    private static final String N1 = "n1";
    private static final String N2 = "n2";
    private static final String N3 = "n3";

    private static final Graph.Edge E12 = new Graph.Edge(N1, N2, "e12");
    private static final Graph.Edge E23 = new Graph.Edge(N2, N3, "e23");
    private static final Graph.Edge E31 = new Graph.Edge(N3, N1, "e31");
    private static final Graph.Edge E12_new = new Graph.Edge(N1, N2, "e12_new");

    private Graph graph;

    @Before
    public void createGraph() {
        graph = new Graph();
    }

    /**
     * Tests that no identical nodes can be added to the graph.
     */
    @Test
    public void testAddSameNode() {
        graph.addNode(N1);
        graph.addNode(N1);
        graph.addNode(N1);
        assertEquals(Arrays.asList(N1), graph.getNodes());
    }

    /**
     * Tests that no identical edges can be added to the graph
     */
    @Test
    public void testAddSameEdge() {
        graph.addNode(N1);
        graph.addNode(N2);

        graph.addEdge(E12);
        graph.addEdge(E12);
        graph.addEdge(E12);
        assertEquals(Arrays.asList(E12), graph.getEdges(N1, N2));
    }

    /**
     * Tests that removeEdge removes an edge that exists in the graph.
     */
    @Test
    public void testRemoveEdgeExist() {
        graph.addNode(N1);
        graph.addNode(N2);

        graph.addEdge(E12);
        graph.addEdge(E12_new);
        graph.removeEdge(E12);
        assertEquals(Arrays.asList(E12_new), graph.getEdges(N1, N2));
    }

    /**
     * Tests that removeEdge does nothing when the edge doesn't exist in the graph.
     */
    @Test
    public void testRemoveEdge2NotExist() {
        graph.addNode(N1);
        graph.addNode(N2);

        graph.removeEdge(E12);
        assertEquals(new ArrayList<>(), graph.getEdges(N1, N2));
    }

    /**
     * Tests that getChildren throws a NoSuchElementException when given a node
     * that doesn't exist in the graph.
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetChildren() {
        graph.getChildren("not exist");
    }

    /**
     * Tests that getParents throws a NoSuchElementException when given a node
     * that doesn't exist in the graph.
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetParents() {
        graph.getParents("not exist");
    }

    /**
     * Tests that getEdges throws a NoSuchElementException when given a node
     * that doesn't exist in the graph.
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetEdges() {
        graph.addNode(N1);
        graph.getEdges(N1, "not exist either");
    }

    /**
     * Tests that getEdgesFrom throws a NoSuchElementException when given a node
     * that doesn't exist in the graph.
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetEdgesFrom() {
        graph.getEdgesFrom("not exist");
    }

    /**
     * Tests that getEdgesTo throws a NoSuchElementException when given a node
     * that doesn't exist in the graph.
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetEdgesTo() {
        graph.getEdgesTo("not exist");
    }

    /**
     * Tests that checkRep throws a RuntimeException when adding null to the map.
     */
    @Test(expected = RuntimeException.class)
    public void testCheckRepNull() {
        graph.addEdge(null);
    }

    // -------------------------------------------------------
    // Following tests are unit tests for the inner class Edge

    /**
     * Tests getParent method in Graph.Edge.
     */
    @Test
    public void testGetParent() {
        assertEquals(N1, E12.getParent());
        assertEquals(N2, E23.getParent());
        assertEquals(N3, E31.getParent());
    }

    /**
     * Tests getChild method in Graph.Edge.
     */
    @Test
    public void testGetChild() {
        assertEquals(N2, E12.getChild());
        assertEquals(N3, E23.getChild());
        assertEquals(N1, E31.getChild());
    }

    /**
     * Tests getLabel method in Graph.Edge.
     */
    @Test
    public void testGetLabel() {
        assertEquals("e12", E12.getLabel());
        assertEquals("e23", E23.getLabel());
        assertEquals("e31", E31.getLabel());
    }

    /**
     * Tests equals method in Graph.Edge.
     */
    @Test
    public void testEquals() {
        Graph.Edge edge1 = new Graph.Edge(N1, N2, "old label");
        Graph.Edge edge2 = new Graph.Edge(N1, N2, "old label");
        Graph.Edge edge3 = new Graph.Edge(N1, N2, "new label");
        assertEquals(edge1, edge2);
        assertNotEquals(edge1, edge3);
    }

    /**
     * Tests that checkRep throws a RuntimeException when a parameter of an edge is null.
     */
    @Test(expected = RuntimeException.class)
    public void testCheckRepEdge() {
        Graph.Edge edge = new Graph.Edge(null, N1, "error");
    }
}
