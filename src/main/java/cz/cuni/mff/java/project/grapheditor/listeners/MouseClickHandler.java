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
public class MouseClickHandler implements EventHandler<MouseEvent> {
    private Editor editor;

    /**
     * <p>A public constructor</p>
     * @param editor instance of Editor class
     */
    public MouseClickHandler(Editor editor) {
        this.editor = editor;
    }

    /**
     * <p>Overrides handle(). Is called when any mouse left button is clicked.</p>
     * @param event standard instance of ActionEvent
     */
    @Override
    public void handle(MouseEvent event) {
        if (!this.editor.algorithmRunning) {
            Integer ind = this.editor.vertexPointExists(
                event.getSceneX(), event.getSceneY()
            );

            if (this.editor.shiftPressed && ind >= 0) {
                this.editor.clicked =
                    new PointD(event.getSceneX() - 15, event.getSceneY() - 45);
                this.editor.selected = this.editor.graph.addVertex();
                this.editor.pos.put(this.editor.selected, this.editor.clicked);
                this.editor.drawGraph();
                return;
            }

            if (this.editor.ctrlPressed && this.editor.selected > 0 &&
                this.editor.selected != ind && ind > 0) {

                List<Integer> neighbors =
                    this.editor.graph.neighbors(this.editor.selected);

                if (neighbors.contains(ind))
                    this.editor.graph.disconnect(this.editor.selected, ind);
                else
                    this.editor.graph.connect(this.editor.selected, ind);

                this.editor.drawGraph();
                return;
            }

            if (!this.editor.dragging && ind > 0) {
                this.editor.dragging = true;
            }

            this.editor.selected = ind;
            this.editor.drawGraph();
        }
    }
}
