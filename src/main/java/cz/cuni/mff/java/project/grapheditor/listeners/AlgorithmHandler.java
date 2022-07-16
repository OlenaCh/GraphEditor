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
    private final Integer X = 20;

    private Editor editor;
    private Integer algorithm;

    private Pane pane;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     * @param algorithm the type of algorithm to run
     */
    public AlgorithmHandler(Editor editor, Integer algorithm) {
        this.editor = editor;
        this.algorithm = algorithm;
    }

    /**
     * <p>Overrides handle(). Creates a new form that allows user to define
     * start and end nodes of serach path and if the manual mode of algorithm
     * run should be applied</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(ActionEvent event) {
        if (this.editor.algorithmRunning ||
            this.editor.graph.vertices().size() == 0) {

            return;
        }

        this.pane = new Pane();

        ChoiceBox start = buildChoiceBoxElement("Start:", 20, 40);
        ChoiceBox end = buildChoiceBoxElement("End:", 80, 100);
        CheckBox manualMode = buildCheckboxElement();

        if (this.algorithm == 3)
            this.pane.getChildren().add(
                createLabel(
                    "Due to the logic of algorithm\nthe manual mode is not available",
                    X,
                    20
                )
            );

        Button button = buildRunButton();

        Stage stage = new Stage();
        stage.setTitle("Algorithm settings:");
        stage.setScene(new Scene(this.pane, 250, sceneWidth()));
        stage.show();

        button.setOnAction(
            new RunButtonHandler(
                this.editor, start, end, manualMode, stage, this.algorithm
            )
        );
    }

    /**
     * <p>Builds a new element that contains a label and a choice box</p>
     * @param label the value for choice box label
     * @param y1 value for Y axis (for label)
     * @param y2 value for Y axis (for choice box)
     * @return created choice box
     */
    private ChoiceBox buildChoiceBoxElement(
        String label, Integer y1, Integer y2
    ) {

        if (this.algorithm != 0 && this.algorithm != 1)
            return null;

        this.pane.getChildren().add(createLabel(label, X, y1));

        ChoiceBox element = createChoiceBox(X, y2);

        this.pane.getChildren().add(element);

        return element;
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
     * @return created choice box
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

    /**
     * <p>Builds a new element that contains a label and a checkbox</p>
     * @return created checkbox
     */
    private CheckBox buildCheckboxElement() {
        if (this.algorithm == 3)
            return null;

        CheckBox manualMode = new CheckBox("Manual mode");

        manualMode.setLayoutX(X);
        manualMode.setLayoutY(
            this.algorithm == 0 || this.algorithm == 1 ? 140 : 20
        );

        this.pane.getChildren().add(manualMode);

        return manualMode;
    }

    /**
     * <p>Builds Run button</p>
     * @return created Run button
     */
    private Button buildRunButton() {
        Button button = new Button("Run");

        button.setLayoutX(110);
        button.setLayoutY(
            this.algorithm == 0 || this.algorithm == 1 ? 170 : 70
        );

        this.pane.getChildren().add(button);

        return button;
    }

    /**
     * <p>Returns the width of scene</p>
     * @return the width of scene
     */
    private Integer sceneWidth() {
        if (this.algorithm == 0 || this.algorithm == 1)
            return 220;

        return 120;
    }
}
