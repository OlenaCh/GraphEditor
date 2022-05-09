package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Handles New being selected (File menu).
 */
public class NewGraphHandler implements EventHandler<ActionEvent> {
    private Editor editor;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public NewGraphHandler(Editor editor) {
        this.editor = editor;
    }

    /**
     * <p>Overrides handle()</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(ActionEvent event) {
        this.editor.newGraph();
        this.editor.drawGraph();
    }
}
