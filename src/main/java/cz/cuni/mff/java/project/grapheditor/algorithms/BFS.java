package cz.cuni.mff.java.project.grapheditor.algorithms;

import cz.cuni.mff.java.project.grapheditor.graphs.Graph;
import java.util.*;

/**
 * Implements BFS algorithm.
 */
public class BFS extends AbstractAlgorithm {
    private Graph graph;
    private Integer start, end;

    private Map<Integer, Boolean> visited;
    private Queue<Node> queue;
    private Node curr;

    private Boolean searching;

    /**
     * <p>A class constructor</p>
     * @param graph an instance of class Graph
     * @param start the start vertex of path
     * @param end the end vertex of path
     */
    public BFS(Graph graph, Integer start, Integer end) {
        this.graph = graph;

        this.start = start;
        this.end = end;

        this.visited = new TreeMap<>();
        this.queue = new LinkedList<>();
        this.searching = false;

        initValues();
    }

    /**
     * <p>Triggers an automated search for the shortest path</p>
     */
    @Override
    public void search() {
        if (this.curr.val == this.end) {
            this.searching = false;
            return;
        }

        while (this.curr != null && this.searching)
            step();

        this.searching = false;
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
     * for shortest path</p>
     */
    @Override
    public void executeSearchStep() {
        if (this.curr == null || this.curr.val == this.end) {
            this.searching = false;
            return;
        }

        step();
    }

    /**
     * <p>Returns the list of visited vertices</p>
     * @return the list of visited vertices
     */
    @Override
    public List<Integer> visitedVertices() {
        List<Integer> vertices = new ArrayList<>();

        for (Integer vertex : this.visited.keySet())
            if (this.visited.get(vertex))
                vertices.add(vertex);

        return vertices;
    }

    /**
     * <p>Returns the list of frontier vertices</p>
     * @return the list of frontier vertices
     */
    @Override
    public List<Integer> frontierVertices() {
        List<Integer> vertices = new ArrayList<>();

        for (Node vertex : this.queue)
            vertices.add(vertex.val);

        return vertices;
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
        for (Integer vertex : this.graph.vertices())
            this.visited.put(vertex, false);

        this.curr = new Node(start);
        this.visited.put(this.curr.val, true);
        this.searching = true;
    }

    /**
     * <p>Builds a string that contains the found shortest path</p>
     * @return a string that contains the found shortest path
     */
    private String shortestPath() {
        String result = " " + this.curr.val;
        Node p = this.curr.parent;

        while (p != null) {
            result = " " + p.val + result;
            p = p.parent;
        }

        return result.trim();
    }

    /**
     * <p>Performs a single step in search</p>
     */
    private void step() {
        for (Integer neighbor : this.graph.neighbors(this.curr.val)) {
            if (!this.visited.get(neighbor)) {
                this.visited.put(neighbor, true);

                Node node = new Node(neighbor, curr, null);
                queue.add(node);

                if (neighbor == this.end) {
                    this.curr = node;
                    this.searching = false;
                    return;
                }
            }
        }

        this.curr = queue.size() > 0 ? queue.remove() : null;
    }
}
