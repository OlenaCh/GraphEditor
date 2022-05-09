package cz.cuni.mff.java.project.grapheditor.listeners;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;
import cz.cuni.mff.java.project.grapheditor.editor.PointD;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.input.MouseEvent;

/**
 * Handles MOUSE_CLICKED events.
 * Does nothing if algorithm is already running.
 */
public class MouseDragHandler implements EventHandler<MouseEvent> {
    private Editor editor;
    private Boolean dragCompleted;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public MouseDragHandler(Editor editor, Boolean dragCompleted) {
        this.editor = editor;
        this.dragCompleted = dragCompleted;
    }

    /**
     * <p>Overrides handle(). Is called when any mouse left button is clicked.</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(MouseEvent event) {
        if (this.editor.algorithmRunning)
            return;

        if (this.dragCompleted) {
            this.editor.dragging = false;
            return;
        }

        if (this.editor.dragging
            && inEditorRange(event.getSceneX())
            && inEditorRange(event.getSceneY())) {

            this.editor.pos.put(
                this.editor.selected,
                new PointD(event.getSceneX(), event.getSceneY())
            );
            this.editor.drawGraph();
        }
    }

    /**
     * <p>Checks if coordinates are not outside the editor window</p>
     * @param val the value of coordinate to check
     * @return true if the coordinate is located inside the form; otherwise, false
     */
    private Boolean inEditorRange(double val) {
        return val >= 20 && val <= 850;
    }
}
