package cz.cuni.mff.java.project.grapheditor;

import cz.cuni.mff.java.project.grapheditor.editor.Editor;
import javafx.application.Application;

/**
 * The starting point of the whole program.
 */
public class Main {
    public static void main(String[] args) {
        javafx.application.Application.launch(Editor.class);
    }
}


// mvn clean javafx:run
// mvn javadoc:javadoc
