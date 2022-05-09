package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Handles Open being selected (File menu).
 */
public class OpenFileHandler implements EventHandler<ActionEvent> {
    private Editor editor;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public OpenFileHandler(Editor editor) {
        this.editor = editor;
    }

    /**
     * <p>Overrides handle()</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(ActionEvent event) {
        Object result =
            this.editor.fileChooser.showOpenDialog(this.editor.primaryStage);

        if (result != null) {
            this.editor.newGraph();
            this.editor.graph.fromFile(result.toString());
            this.editor.fileRead = true;
            this.editor.drawGraph();
        }
    }
}
