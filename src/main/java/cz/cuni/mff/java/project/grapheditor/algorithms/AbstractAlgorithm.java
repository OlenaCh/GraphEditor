package cz.cuni.mff.java.project.grapheditor.algorithms;

import java.util.*;

/**
 * An abstract parent class that implements basic interface for all algorithms.
 */
public abstract class AbstractAlgorithm {
    public abstract void search();
    public abstract Path result();

    public abstract void executeSearchStep();

    public abstract List<Integer> visitedVertices();
    public abstract List<Integer> frontierVertices();

    public abstract Boolean running();
}