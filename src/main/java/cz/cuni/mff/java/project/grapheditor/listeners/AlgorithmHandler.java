package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.stage.Stage;

/**
 * Handles an algorithm form and starts algorithm execution.
 * Does nothing if algorithm is already running.
 */
public class AlgorithmHandler implements EventHandler<ActionEvent> {
    private Editor editor;
    private Boolean dfs;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     * @param dfs if set to true DFS will be run, otherwise - BFS
     */
    public AlgorithmHandler(Editor editor, Boolean dfs) {
        this.editor = editor;
        this.dfs = dfs;
    }

    /**
     * <p>Overrides handle(). Creates a new form that allows user to define
     * start and end nodes of serach path and if the manual mode of algorithm run
     * should be applied</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(ActionEvent event) {
        if (this.editor.algorithmRunning) { return; }

        Pane pane = new Pane();

        pane.getChildren().add(createLabel("Start:", 20, 20));
        ChoiceBox start = createChoiceBox(20, 40);
        pane.getChildren().add(start);

        pane.getChildren().add(createLabel("End:", 20, 80));
        ChoiceBox end = createChoiceBox(20, 100);
        pane.getChildren().add(end);

        CheckBox manualMode = new CheckBox("Manual mode");
        manualMode.setLayoutX(20);
        manualMode.setLayoutY(140);
        pane.getChildren().add(manualMode);

        Button button = new Button("Run");
        button.setLayoutX(110);
        button.setLayoutY(170);
        pane.getChildren().add(button);

        Stage stage = new Stage();
        stage.setTitle("Algorithm settings:");
        stage.setScene(new Scene(pane, 250, 220));
        stage.show();

        button.setOnAction(
            new RunButtonHandler(
                this.editor, start, end, manualMode, stage, this.dfs
            )
        );
    }

    /**
     * <p>Creates a new label</p>
     * @param text Label's text
     * @param x value for X axis
     * @param y value for Y axis
     * @return a newly created label
     */
    private Label createLabel(String text, Integer x, Integer y) {
        Label label = new Label(text);

        label.setLayoutX(x);
        label.setLayoutY(y);

        return label;
    }

    /**
     * <p>Creates a new choice box, filled with the ids of existing nodes</p>
     * @param x value for X axis
     * @param y value for Y axis
     */
    @SuppressWarnings("unchecked")
    private ChoiceBox createChoiceBox(Integer x, Integer y) {
        ChoiceBox cb = new ChoiceBox(
            FXCollections.observableArrayList(
                this.editor.graph.vertices()
            )
        );

        cb.setLayoutX(x);
        cb.setLayoutY(y);

        return cb;
    }
}
