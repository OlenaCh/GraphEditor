package cz.cuni.mff.java.project.grapheditor.algorithms;

import cz.cuni.mff.java.project.grapheditor.graphs.Graph;
import java.util.*;

/**
 * Implements DFS algorithm.
 */
public class DFS extends AbstractAlgorithm {
    Graph graph;
    Integer start, end, visitedTotal;

    Map<Integer, Boolean> visited;
    Node curr;

    Boolean searching;

    /**
     * <p>A class constructor</p>
     * @param graph an instance of class Graph
     * @param start the start vertex of path
     * @param end the end vertex of path
     */
    public DFS(Graph graph, Integer start, Integer end) {
        this.graph = graph;

        this.start = start;
        this.end = end;

        this.visited = new TreeMap<>();
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

        while (this.visitedTotal > 0 && this.searching)
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
        if (this.visitedTotal <= 0 || this.curr.val == this.end) {
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

        for (Integer vertex : this.graph.neighbors(this.curr.val))
            if (!this.visited.containsKey(vertex))
                vertices.add(vertex);

        return vertices;
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

        this.visitedTotal = this.visited.size();
        this.curr = new Node(start);
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
        this.visited.put(this.curr.val, true);
        this.visitedTotal -= 1;

        for (Integer neighbor : this.graph.neighbors(this.curr.val)) {
            if (!this.visited.get(neighbor)) {
                this.curr = new Node(neighbor, curr, null);

                if (neighbor == this.end)
                    this.searching = false;

                return;
            }
        }

        this.curr = this.curr.parent;
    }
}
