package cz.cuni.mff.java.project.grapheditor.graphs;

import java.util.*;

/**
 * Implements IO interface for graphs.
 */
interface GraphIOInterface {
    void fromFile(String filename);
    void toFile(String filename);
}

/**
 * A parent class that implements the basic representation of a graph.
 */
public class AbstractGraph {
    protected SortedMap<Integer, List<Integer>> vertex =
        new TreeMap<Integer, List<Integer>>();

    /**
     * <p>A class constructor</p>
     */
    public AbstractGraph() {}

    /**
     * <p>Adds a new vertex to the graph</p>
     * @return the id of added vertex
     */
    public Integer addVertex() {
        Integer id = 1;

        while (this.vertex.containsKey(id))
            ++id;

        this.vertex.put(id, new ArrayList<Integer>());

        return id;
    }

    /**
     * <p>Deletes a vertex</p>
     * @param id the id of vertex
     */
    public void deleteVertex(Integer id) {
        if (this.vertex.containsKey(id)) {
            for(Integer v : vertex.get(id))
                vertex.get(v).remove(id);

            this.vertex.remove(id);
        }
    }

    /**
     * <p>Returns all graph vertices</p>
     * @return the list of graph vertices
     */
    public List<Integer> vertices() {
        return new ArrayList<Integer>(this.vertex.keySet());
    }

    /**
     * <p>Returns adjacent vertices for a vertex</p>
     * @param id the id of vertex
     * @return the list of adjacent vertices
     */
    public List<Integer> neighbors(Integer id) {
        return this.vertex.get(id);
    }

    /**
     * <p>Adds a new edge</p>
     * @param i the first vertex of edge
     * @param j the second vertex of edge
     */
    public void connect(Integer i, Integer j) {
        this.vertex.get(i).add(j);
        this.vertex.get(j).add(i);
    }

    /**
     * <p>Deletes an edge</p>
     * @param i the first vertex of edge
     * @param j the second vertex of edge
     */
    public void disconnect(Integer i, Integer j) {
        this.vertex.get(i).remove(j);
        this.vertex.get(j).remove(i);
    }

    /**
     * <p>Checks if some edge exists</p>
     * @param i the first vertex of edge
     * @param j the second vertex of edge
     * @return true if the edge exists; otherwise, false
     */
    public boolean areConnected(Integer i, Integer j) {
        return this.vertex.get(i).contains(j) && this.vertex.get(j).contains(i);
    }
}