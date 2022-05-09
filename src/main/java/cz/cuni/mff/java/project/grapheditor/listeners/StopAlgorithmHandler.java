package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Handles Stop/Clear status (Algorithms menu).
 */
public class StopAlgorithmHandler implements EventHandler<ActionEvent> {
    private Editor editor;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public StopAlgorithmHandler(Editor editor) {
        this.editor = editor;
    }

    /**
     * <p>Overrides handle()</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(ActionEvent event) {
        this.editor.clearAlgorithmRelatedData();
        this.editor.drawGraph();
    }
}
