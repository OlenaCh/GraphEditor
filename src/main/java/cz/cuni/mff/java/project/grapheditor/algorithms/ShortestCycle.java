package cz.cuni.mff.java.project.grapheditor.algorithms;

import cz.cuni.mff.java.project.grapheditor.graphs.Graph;
import java.util.*;

/**
 * Implements shortest cycle search algorithm.
 */
public class ShortestCycle extends AbstractAlgorithm {
    private Graph graph;
    private SortedMap<Integer, List<Integer>> edges =
        new TreeMap<Integer, List<Integer>>();

    private Node curr;
    private Integer shortest;

    private Boolean searching;

    /**
     * <p>A class constructor</p>
     * @param graph an instance of class Graph
     */
    public ShortestCycle(Graph graph) {
        this.graph = graph;

        this.edges = new TreeMap<>();
        this.curr = null;
        this.shortest = Integer.MAX_VALUE;
        this.searching = false;

        initValues();
    }

    /**
     * <p>Triggers an automated search for the shortest cycle in graph</p>
     */
    @Override
    public void search() {
        for (Integer vertex : this.graph.vertices())
            step(vertex);
    }

    /**
     * <p>Returns the result of search</p>
     * @return an instance of class Path
     */
    @Override
    public Path result() {
        return new Path((this.curr != null) ? shortestPath() : "unreachable");
    }

    /**
     * <p>Executes one search step at a time during a manual search
     * for the shortest cycle in graph. In this algorithm does nothing.</p>
     */
    @Override
    public void executeSearchStep() {}

    /**
     * <p>Returns the list of visited vertices</p>
     * @return the list of visited vertices (always null)
     */
    @Override
    public List<Integer> visitedVertices() {
        return null;
    }

    /**
     * <p>Returns the list of frontier vertices</p>
     * @return the list of frontier vertices (always null)
     */
    @Override
    public List<Integer> frontierVertices() {
        return null;
    }

    /**
     * <p>Returns the list of frontier edges</p>
     * @return the list of frontier edges (always null)
     */
    @Override
    public List<String> frontierEdges() {
        return null;
    }

    /**
     * <p>Checks if the algorithm is running</p>
     * @return true if the algorithm is running, otherwise false
     */
    @Override
    public Boolean running() {
        return this.searching;
    }

    /**
     * <p>Initializes values that are required to run the algorithm</p>
     */
    private void initValues() {
        for (Integer vertex : this.graph.vertices()) {
            this.edges.put(vertex, new ArrayList<Integer>());

            for (Integer neighbor : this.graph.neighbors(vertex))
                this.edges.get(vertex).add(neighbor);
        }

        this.searching = true;
    }

    /**
     * <p>Builds a string that contains the found shortest cycle</p>
     * @return a string that contains the found shortest cycle
     */
    private String shortestPath() {
        String initialNode = this.curr.val.toString();
        String result = " " + this.curr.val;
        Node p = this.curr.parent;

        while (p != null) {
            result = " " + p.val + result;
            p = p.parent;
        }

        return initialNode + " " + result.trim();
    }

    /**
     * <p>Performs a single step in search</p>
     * @param vertex the id of vertex in graph
     */
    private void step(Integer vertex) {
        for (Integer neighbor : this.graph.neighbors(vertex)) {
            removeEdge(vertex, neighbor);
            executeBfs(vertex, neighbor);
            addRemovedEdge(vertex, neighbor);
        }
    }

    /**
     * <p>Removes an edge from the list of copied graph edges</p>
     * @param v the id of vertex
     * @param n the id of vertex
     */
    private void removeEdge(Integer v, Integer n) {
        this.edges.get(v).remove(n);
        this.edges.get(n).remove(v);
    }

    /**
     * <p>Adds an edge to the list of copied graph edges</p>
     * @param v the id of vertex
     * @param n the id of vertex
     */
    private void addRemovedEdge(Integer v, Integer n) {
        this.edges.get(v).add(n);
        this.edges.get(n).add(v);
    }

    /**
     * <p>Performs BFS to find the shortest path between two vertices</p>
     * @param v the id of vertex
     * @param n the id of vertex
     */
    private void executeBfs(Integer v, Integer n) {
        Map<Integer, Boolean> visited = new TreeMap<>();

        for (Integer vertex : this.graph.vertices())
            visited.put(vertex, vertex == v ? true : false);

        Node node = bfsUtil(visited, new Node(v), n);

        if (node != null) {
            Integer count = countEdges(new Node(v, node, null));

            if (count < this.shortest) {
                this.curr = node;
                this.shortest = count;
            }
        }
    }

    /**
     * <p>A helper method that implements BFS routine</p>
     * @param visited the array that stores information about visited vertices
     * @param node the starting point of BFS (vertex id wrapped in Node)
     * @param end the end point of BFS (vertex id)
     * @return null if path not found; otherwise, the end node in the found path
     */
    private Node bfsUtil(Map<Integer, Boolean> visited, Node node, Integer end) {
        Queue<Node> queue = new LinkedList<>();
        Boolean bfsInProgress = true;

        while (node != null && bfsInProgress) {
            for (Integer neighbor : this.edges.get(node.val)) {
                if (!visited.get(neighbor)) {
                    visited.put(neighbor, true);

                    Node tmp = new Node(neighbor, node, null);
                    queue.add(tmp);

                    if (neighbor == end) {
                        node = tmp;
                        bfsInProgress = false;

                        return node;
                    }
                }
            }

            node = queue.size() > 0 ? queue.remove() : null;
        }

        return node;
    }

    /**
     * <p>Counts the number of edges in the shortest cycle</p>
     * @param node the end node in the found shortest path
     * @return the number of edges that form the shortest cycle
     */
    private Integer countEdges(Node node) {
        Node p = node.parent;
        Integer count = 1;

        while (p != null) {
            count++;
            p = p.parent;
        }

        return count;
    }
}
