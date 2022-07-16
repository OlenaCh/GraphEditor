package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;
import cz.cuni.mff.java.project.grapheditor.algorithms.DFS;
import cz.cuni.mff.java.project.grapheditor.algorithms.BFS;
import cz.cuni.mff.java.project.grapheditor.algorithms.MinimumSpanningTree;
import cz.cuni.mff.java.project.grapheditor.algorithms.ShortestCycle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.stage.Stage;

/**
 * Starts algorithm execution.
 */
public class RunButtonHandler implements EventHandler<ActionEvent> {
    private Editor editor;
    private ChoiceBox startVertex;
    private ChoiceBox endVertex;
    private CheckBox manualMode;
    private Stage stage;
    private Integer algorithm;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     * @param startVertex the choice box that contains the start vertex of path
     * @param endVertex the choice box that contains the end vertex of path
     * @param manualMode an indicator if algorithm should be run manually
     * @param stage the stage of argotithm form
     * @param algorithm the type of algorithm to run
     */
    public RunButtonHandler(
        Editor editor,
        ChoiceBox startVertex,
        ChoiceBox endVertex,
        CheckBox manualMode,
        Stage stage,
        Integer algorithm
    ) {

        this.editor = editor;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.manualMode = manualMode;
        this.stage = stage;
        this.algorithm = algorithm;
    }

    /**
     * <p>Overrides handle(). Closes an algorithm form.
     * Starts algorithm execution.</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(ActionEvent event) {
        Integer start = null;
        Integer end = null;

        if (this.startVertex != null)
            start = Integer.parseInt("" + this.startVertex.getValue());

        if (this.endVertex != null)
            end = Integer.parseInt("" + this.endVertex.getValue());

        if (this.manualMode != null)
            this.editor.manualMode = this.manualMode.isSelected();
        else
            this.editor.manualMode = false;

        switch (this.algorithm) {
            case 0:
                this.editor.algorithm = new BFS(this.editor.graph, start, end);
                break;
            case 1:
                this.editor.algorithm = new DFS(this.editor.graph, start, end);
                break;
            case 2:
                this.editor.algorithm =
                    new MinimumSpanningTree(this.editor.graph);
                break;
            case 3:
                this.editor.algorithm = new ShortestCycle(this.editor.graph);
                break;
            default:
                return;
        }

        this.stage.close();

        if (this.editor.manualMode) {
            this.editor.algorithmRunning = this.editor.algorithm.running();
            this.editor.algorithmStatus.setText(
                "Algorithm running... Press 'Slash' key."
            );
        }
        else {
            this.editor.algorithm.search();
            this.editor.displayResult(this.editor.algorithm.result());
        }
    }
}
