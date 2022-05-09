package cz.cuni.mff.java.project.grapheditor.algorithms;

/**
 * A helper class that stores information about vertex parent during BFS and DFS.
 */
public class Node {
    public Integer val;
    public Node parent, next;

    public Node(Integer v) {
        this.val = v;
    }

    public Node(Integer v, Node p, Node n) {
        this.val = v;
        this.parent = p;
        this.next = n;
    }
}
