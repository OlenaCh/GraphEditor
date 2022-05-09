package cz.cuni.mff.java.project.grapheditor.algorithms;

import java.util.*;

/**
 * A helper class that stores information about path, found during BFS or DFS.
 */
public class Path {
    String initial;
    List<List<Integer>> edges;

    /**
     * <p>A class constructor</p>
     * @param initial av path which may look like '1 2 4' where numbers
     * indicate ids of graph vertices
     */
    public Path(String initial) {
        this.initial = initial;
        this.edges = new ArrayList<>();
    }

    /**
     * <p>Returns the list of edges that form a path</p>
     * @return the list of edges that form a path
     */
    public List<List<Integer>> result() {
        return this.edges;
    }

    /**
     * <p>Checks if it is possible to build a path</p>
     * @return true if the information about found path allows to form
     * the list of edges; otherwise false
     */
    public Boolean buildResult() {
        if (this.initial.equals("unreachable"))
            return false;

        buildPath();

        return true;
    }

    /**
     * <p>Converts a path from string to the list of edges</p>
     */
    private void buildPath() {
        String[] tmp = this.initial.split("\\s+");

        for (int i = 0; i < tmp.length - 1; i++) {
            Integer v1 = Integer.parseInt(tmp[i]);
            Integer v2 = Integer.parseInt(tmp[i + 1]);
            List<Integer> tmpArr = new ArrayList<>();

            tmpArr.add(v1);
            tmpArr.add(v2);

            this.edges.add(tmpArr);
        }
    }
}