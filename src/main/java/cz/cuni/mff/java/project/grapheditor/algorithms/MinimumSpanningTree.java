package cz.cuni.mff.java.project.grapheditor.algorithms;

import cz.cuni.mff.java.project.grapheditor.graphs.Graph;
import java.util.*;

/**
 * Implements minimum spanning tree algorithm.
 */
public class MinimumSpanningTree extends AbstractAlgorithm {
    private Graph graph;
    private List<String> edges;
    private Integer start;

    private Queue<Integer> queue;
    private Map<Integer, Boolean> visited;
    private Node curr;

    private Boolean searching;
    private Boolean nextStepVisuallyCompleted;

    /**
     * <p>A class constructor</p>
     * @param graph an instance of class Graph
     */
    public MinimumSpanningTree(Graph graph) {
        this.graph = graph;
        this.edges = new ArrayList<>();
        this.start = -1;

        this.queue = new LinkedList<>();
        this.visited = new TreeMap<>();
        this.searching = false;
        this.nextStepVisuallyCompleted = false;

        initValues();
    }

    /**
     * <p>Triggers an automated search for the minimum spanning tree</p>
     */
    @Override
    public void search() {
        while (this.queue.size() > 0 && this.searching)
            step();

        this.searching = false;
    }

    /**
     * <p>Returns the result of search</p>
     * @return an instance of class Path
     */
    @Override
    public Path result() {
        if (this.edges.size() > 0)
            return new Path(this.edges);

        return new Path("unreachable");
    }

    /**
     * <p>Executes one search step at a time during a manual search
     * for minimum spanning tree</p>
     */
    @Override
    public void executeSearchStep() {
        this.nextStepVisuallyCompleted = false;

        if (this.queue.size() <= 0) {
            this.searching = false;
            return;
        }

        while (this.queue.size() > 0 && !this.nextStepVisuallyCompleted)
            step();
    }

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
     * @return the list of frontier edges
     */
    @Override
    public List<String> frontierEdges() {
        return this.edges;
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
            if (this.start == -1)
                this.start = vertex;

            this.visited.put(vertex, false);

            for (Integer neighbor : this.graph.neighbors(vertex))
                this.edges.add(vertex + " " + neighbor);
        }

        this.queue.add(start);
        this.visited.put(start, true);
        this.searching = true;
    }

    /**
     * <p>Performs a single step in search</p>
     */
    private void step() {
        this.curr = new Node(this.queue.remove());

        for (Integer neighbor : this.graph.neighbors(this.curr.val)) {
            if (!this.visited.get(neighbor)) {
                this.queue.add(neighbor);
                this.visited.put(neighbor, true);
            }
            else {
                this.edges.remove(this.curr.val + " " + neighbor);

                if (!this.edges.contains(neighbor + " " + this.curr.val))
                    this.nextStepVisuallyCompleted = true;
            }
        }
    }
}
