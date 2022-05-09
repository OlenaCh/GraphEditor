package cz.cuni.mff.java.project.grapheditor.editor;

import cz.cuni.mff.java.project.grapheditor.algorithms.AbstractAlgorithm;
import cz.cuni.mff.java.project.grapheditor.algorithms.Path;
import cz.cuni.mff.java.project.grapheditor.graphs.Graph;
import cz.cuni.mff.java.project.grapheditor.listeners.AlgorithmHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.KeyBoardPressHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.KeyBoardReleaseHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.MouseClickHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.MouseDragHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.NewGraphHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.OpenFileHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.SaveFileHandler;
import cz.cuni.mff.java.project.grapheditor.listeners.StopAlgorithmHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Pair;


/**
 * Implements the graph editor form.
 */
public class Editor extends Application {
    public Editor editor;

    public Stage primaryStage;
    public Label algorithmStatus;
    public FileChooser fileChooser;

    Scene scene;
    BorderPane borderPane;
    Pane wrapperPane;
    Canvas canvas;
    GraphicsContext drawingArea;

    public Graph graph;
    public int selected;
    public PointD clicked;
    public Map<Integer, PointD> pos;

    public AbstractAlgorithm algorithm;
    public boolean algorithmRunning = false;
    public boolean manualMode = true;
    List<List<Integer>> foundResult;

    public boolean shiftPressed = false;
    public boolean ctrlPressed = false;
    public boolean fileRead = false;
    public boolean dragging = false;

    /**
     * <p>Overrides start(). Configures and launches editor program</p>
     * @param primaryStage the main stage of program
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            this.editor = this;
            this.fileChooser = new FileChooser();
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Graph editor");
            this.primaryStage.setResizable(false);

            configureScene();
            newGraph();

            this.scene = new Scene(this.borderPane, 900, 900);
            this.scene.addEventFilter(
                KeyEvent.KEY_PRESSED, new KeyBoardPressHandler(this)
            );
            this.scene.addEventFilter(
                KeyEvent.KEY_RELEASED, new KeyBoardReleaseHandler(this)
            );

            this.primaryStage.setScene(this.scene);
            this.primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Configures the main scene</p>
     */
    private void configureScene() {
        this.borderPane = new BorderPane();
        borderPane.setTop(createMenuBar());

        this.wrapperPane = new Pane();
        borderPane.setCenter(wrapperPane);

        this.algorithmStatus = new Label();
        this.algorithmStatus.setLayoutX(10);
        this.wrapperPane.getChildren().add(this.algorithmStatus);

        this.canvas = new Canvas();
        this.drawingArea = this.canvas.getGraphicsContext2D();

        this.canvas.addEventFilter(
            MouseEvent.MOUSE_PRESSED, new MouseClickHandler(this)
        );
        this.canvas.addEventFilter(
            MouseEvent.MOUSE_DRAGGED, new MouseDragHandler(this, false)
        );
        this.canvas.addEventFilter(
            MouseEvent.MOUSE_RELEASED, new MouseDragHandler(this, true)
        );

        this.wrapperPane.getChildren().add(this.canvas);
        this.canvas.widthProperty().bind(wrapperPane.widthProperty());
        this.canvas.heightProperty().bind(wrapperPane.heightProperty());
    }

    /**
     * <p>Creates a menu bar</p>
     * @return a newly created manu bar
     */
    private MenuBar createMenuBar() {
        String[] items = new String[] { "File", "Algorithms" };
        List<LinkedHashMap<String, EventHandler<ActionEvent>>> subitems =
            this.menuSubitems();
        MenuBar menuBar = new MenuBar();

        menuBar.prefWidthProperty().bind(this.primaryStage.widthProperty());

        for (int i = 0; i < 2; i++) {
            Menu menu = new Menu(items[i]);

            for (String key : subitems.get(i).keySet()) {
                MenuItem item = new MenuItem(key);

                item.setOnAction(subitems.get(i).get(key));
                menu.getItems().add(item);
            }

            menuBar.getMenus().add(menu);
        }

        return menuBar;
    }

    /**
     * <p>Creates the sublists of the items of menu bar</p>
     * @return the list of sublists and events that are bound to items in those
     * sublists
     */
    private List<LinkedHashMap<String, EventHandler<ActionEvent>>> menuSubitems() {
        List<LinkedHashMap<String, EventHandler<ActionEvent>>> subitems =
            new ArrayList<LinkedHashMap<String, EventHandler<ActionEvent>>>();

        subitems.add(
            new LinkedHashMap<String, EventHandler<ActionEvent>>() {{
                put("New", new NewGraphHandler(editor));
                put("Open", new OpenFileHandler(editor));
                put("Save", new SaveFileHandler(editor));
                put("Quit", new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) { Platform.exit(); }
                });
            }}
        );

        subitems.add(
            new LinkedHashMap<String, EventHandler<ActionEvent>>() {{
                put("BFS", new AlgorithmHandler(editor, false));
                put("DFS", new AlgorithmHandler(editor, true));
                put("Stop/Clear status", new StopAlgorithmHandler(editor));
            }}
        );

        subitems.add(new LinkedHashMap<String, EventHandler<ActionEvent>>());

        return subitems;
    }

    /**
     * <p>Clears all graph and algorithm related data</p>
     */
    public void newGraph() {
        this.graph = new Graph();
        this.pos = new HashMap<>();
        this.selected = 0;

        clearAlgorithmRelatedData();
    }

    /**
     * <p>Draws a graph</p>
     */
    public void drawGraph() {
        if (this.fileRead) {
            buildGraphCoordinates();
            this.fileRead = false;
        }

        this.drawingArea.clearRect(
            0, 0, this.canvas.getWidth(), this.canvas.getHeight()
        );

        drawEdges();
        drawVertices();
    }

    /**
     * <p>Draws all edges</p>
     */
    private void drawEdges() {
        List<Integer> vertices = this.graph.vertices();

        for (Integer v : vertices) {
            List<Integer> neighbors = this.graph.neighbors(v);

            for (Integer n : neighbors) {
                drawEdge(this.pos.get(v), this.pos.get(n), edgeColor(v, n));
            }
        }
    }

    /**
     * <p>Draws a single edge</p>
     * @param x a point x of edge
     * @param y a point y of edge
     * @param color the color of edge
     */
    private void drawEdge(PointD x, PointD y, Color color) {
        this.drawingArea.setStroke(color);
        this.drawingArea.setLineWidth(2);
        this.drawingArea.strokeLine(
            x.getX() + 15, x.getY() + 15, y.getX() + 15, y.getY() + 15
        );
    }

    /**
     * <p>Defines an edge color</p>
     * @param v the first vertex of edge
     * @param n the second vertex of edge
     * @return the color of edge
     */
    private Color edgeColor(int v, int n) {
        if (!this.algorithmRunning && edgeOfFoundResult(v, n))
            return Color.RED;

        return Color.BLACK;
    }

    /**
     * <p>Draws all vertices</p>
     */
    private void drawVertices() {
        int radius = 12;
        List<Integer> visited = null;
        List<Integer> frontier = null;

        if (this.algorithmRunning) {
            visited = algorithm.visitedVertices();
            frontier = algorithm.frontierVertices();
        }

        for (Map.Entry<Integer, PointD> position : this.pos.entrySet()) {
            Integer key = position.getKey();
            PointD value = position.getValue();
            Color color = vertexColor(key, visited, frontier);

            drawCircle(value.getX(), value.getY(), color);
            drawText(
                value.getX() + 10.0,
                value.getY() + 20.0,
                color == Color.BLACK ? Color.WHITE : Color.BLACK,
                key.toString()
            );
        }
    }

    /**
     * <p>Draws the circle of vertex</p>
     * @param x a point x of vertex
     * @param y a point y of vertex
     * @param color the color of vertex
     */
    private void drawCircle(double x, double y, Color color) {
        this.drawingArea.setFill(color);
        this.drawingArea.setStroke(Color.BLACK);
        this.drawingArea.setLineWidth(2);
        this.drawingArea.fillOval(x, y, 30, 30);
        this.drawingArea.strokeOval(x, y, 30, 30);
    }

    /**
     * <p>Draws the text which is the id of vertex</p>
     * @param x a point x of text
     * @param y a point y of text
     * @param color the color of text
     * @param text text to draw
     */
    private void drawText(double x, double y, Color color, String text) {
        this.drawingArea.setFill(color);
        this.drawingArea.fillText(text, x, y);
    }

    /**
     * <p>Defines a vertex color</p>
     * @param key the id of vertex
     * @param visited visited vertices
     * @param frontier frontier vertices
     * @return the color of vertex
     */
    private Color vertexColor(
        Integer key,
        List<Integer> visited,
        List<Integer> frontier
    ) {

        if (!this.algorithmRunning) {
            if (key == this.selected)
                return Color.BLACK;

            return Color.WHITE;
        }

        if (visited != null && visited.contains(key))
            return Color.BLACK;

        if (frontier != null && frontier.contains(key))
            return Color.BLUE;

        return Color.WHITE;
    }

    /**
     * <p>Checks if an edge belongs to the found path</p>
     * @param a the first vertex of edge
     * @param b the second vertex of edge
     * @return true if an edge belongs to the found path; otherwise, false
     */
    private boolean edgeOfFoundResult(Integer a, Integer b) {
        if (this.foundResult.size() < 1) {
            return false;
        }

        List<Integer> tmp1 = new ArrayList<>();
        tmp1.add(a);
        tmp1.add(b);

        List<Integer> tmp2 = new ArrayList<>();
        tmp2.add(b);
        tmp2.add(a);

        for (List<Integer> edge : this.foundResult) {
            if (edge.equals(tmp1) || edge.equals(tmp2))
                return true;
        }

        return false;
    }

    /**
     * <p>Builds the map of the (x, y) coordinates of vertices</p>
     */
    private void buildGraphCoordinates() {
        Random rand = new Random();

        for (Integer v : this.graph.vertices()) {
            Integer id = v;
            Double x, y;

            do {
                x = (double)rand.nextInt(750);
                y = (double)rand.nextInt(750);
            } while (vertexPointExists(x, y) > 0);
            this.pos.put(id, new PointD(x, y));
        }
    }


    /**
     * <p>Checks if there is a vertex, located at certain coordinates (x, y)</p>
     * @param x the coordinate x
     * @param y the coordinate y
     * @return the id of found vertex; otherwise, 0
     */
    public Integer vertexPointExists(Double x, Double y) {
        for (Map.Entry<Integer, PointD> position : this.pos.entrySet()) {
            Integer key = position.getKey();
            PointD value = position.getValue();

            if (pointExists(value, x, y))
                return key;
        }

        return 0;
    }

    /**
     * <p>Checks if certain coordinates (x, y) can be used to draw a vertex</p>
     * @param p the coordinates of existing point
     * @param x the coordinate x (to check)
     * @param y the coordinate y (to check)
     * @return true if coordinates can be used to draw a vertex; otherwise, false
     */
    private boolean pointExists(PointD p, Double x, Double y) {
        double px = p.getX();
        double py = p.getY();

        return x >= px && x <= px + 60.0
               && y >= py && y <= py + 60.0;
    }

    /**
     * <p>Clears all data, related to the executed algorithm</p>
     */
    public void clearAlgorithmRelatedData() {
        this.algorithm = null;
        this.algorithmRunning = false;
        this.algorithmStatus.setText("");
        this.foundResult = new ArrayList<>();
    }

    /**
     * <p>Displays the result of executed algorithm</p>
     * @param result a found path to display
     */
    public void displayResult(Path result) {
        if (!result.buildResult()) {
            this.algorithmStatus.setText(
                "Algorithm finished running. No result found."
            );
            this.foundResult = new ArrayList<>();
        }
        else {
            this.algorithmStatus.setText(
                "Algorithm finished running. See result."
            );
            this.selected = 0;
            this.foundResult = result.result();
        }

        drawGraph();
    }
}
