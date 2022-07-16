package cz.cuni.mff.java.project.grapheditor.algorithms;

import java.util.*;

/**
 * A helper class that stores information about path/tree/cycle,
 * found during algorithm execution.
 */
public class Path {
    private String initial;
    private List<String> initialEdges;
    private List<List<Integer>> edges;

    /**
     * <p>A class constructor</p>
     * @param initial a path which may look like '1 2 4' where numbers
     * indicate ids of graph vertices
     */
    public Path(String initial) {
        this.initial = initial;
        this.edges = new ArrayList<>();
        this.initialEdges = new ArrayList<>();
    }

    /**
     * <p>A class constructor</p>
     * @param initialEdges a list of edges that look like '1 2' where numbers
     * indicate ids of graph vertices
     */
    public Path(List<String> initialEdges) {
        this.initial = null;
        this.edges = new ArrayList<>();
        this.initialEdges = initialEdges;
    }

    /**
     * <p>Returns the list of edges that form path/tree</p>
     * @return the list of edges that form path/tree
     */
    public List<List<Integer>> result() {
        return this.edges;
    }

    /**
     * <p>Checks if it is possible to build a path</p>
     * @return true if the information about found path/tree allows to form
     * the list of edges; otherwise false
     */
    public Boolean buildResult() {
        if ((this.initial == null & this.initialEdges.size() == 0) ||
            (this.initial != null && this.initial.equals("unreachable")))
            return false;

        if (this.initialEdges.size() > 0)
            buildResultFromArray();
        else
            buildResultFromString();

        return true;
    }

    /**
     * <p>Converts a path from string to the list of edges</p>
     */
    private void buildResultFromString() {
        String[] tmp = this.initial.split("\\s+");

        for (int i = 0; i < tmp.length - 1; i++)
            addEdge(tmp[i], tmp[i + 1]);
    }

    /**
     * <p>Converts a list of String edges to the list of Integer edges</p>
     */
    private void buildResultFromArray() {
        for (String edge : this.initialEdges) {
            String[] tmp = edge.split("\\s+");

            addEdge(tmp[0], tmp[1]);
        }
    }

    /**
     * <p>Creates an edge</p>
     * @param a vertex id
     * @param b vertex id
     */
    private void addEdge(String a, String b) {
        List<Integer> tmpArr = new ArrayList<>();

        tmpArr.add(Integer.parseInt(a));
        tmpArr.add(Integer.parseInt(b));

        this.edges.add(tmpArr);
    }
}