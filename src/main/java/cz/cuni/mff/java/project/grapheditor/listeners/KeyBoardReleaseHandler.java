package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles KEY_RELEASED events.
 * Does nothing if algorithm is already running.
 */
public class KeyBoardReleaseHandler implements EventHandler<KeyEvent> {
    private Editor editor;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public KeyBoardReleaseHandler(Editor editor) {
        this.editor = editor;
    }

    /**
     * <p>Overrides handle(). Is called when any keyboard button is released.
     * Handles Shift, Control, Slash keys.</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(KeyEvent event) {
        if (!this.editor.algorithmRunning) {
            if (event.getCode() == KeyCode.SHIFT)
                this.editor.shiftPressed = false;

            if (event.getCode() == KeyCode.CONTROL)
                this.editor.ctrlPressed = false;
        }

        if (this.editor.manualMode && event.getCode() == KeyCode.SLASH) {
            if (this.editor.algorithmRunning) {
                this.editor.algorithm.executeSearchStep();
                this.editor.drawGraph();
                this.editor.algorithmRunning = this.editor.algorithm.running();
            }
            else {
                this.editor.displayResult(this.editor.algorithm.result());
            }
        }
    }
}
