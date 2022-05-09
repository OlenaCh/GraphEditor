package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles KEY_PRESSED events.
 * Does nothing if algorithm is already running.
 */
public class KeyBoardPressHandler implements EventHandler<KeyEvent> {
    private Editor editor;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public KeyBoardPressHandler(Editor editor) {
        this.editor = editor;
    }

    /**
     * <p>Overrides handle(). Is called when any keyboard button is pressed.
     * Handles Shift, Control, Delete keys.</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(KeyEvent event) {
        if (!this.editor.algorithmRunning) {
            if (event.getCode() == KeyCode.SHIFT) {
                this.editor.shiftPressed = true;
                return;
            }

            if (event.getCode() == KeyCode.CONTROL) {
                this.editor.ctrlPressed = true;
                return;
            }

            if (event.getCode() == KeyCode.DELETE && this.editor.selected > 0) {
                this.editor.graph.deleteVertex(this.editor.selected);
                this.editor.pos.remove(this.editor.selected);
                this.editor.selected = 0;
                this.editor.drawGraph();
            }
        }
    }
}
