package cz.cuni.mff.java.project.grapheditor.graphs;

import cz.cuni.mff.java.project.grapheditor.editor.PointD;

import java.io.*;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;

import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Implements a graph with IO interface.
 */
public class Graph extends AbstractGraph implements GraphIOInterface {
    /**
     * <p>Keeps the information about the positions of vertices.</p>
     * <p> Is updated in two cases: when graph data is read from file and
     * when graph data is stored to file.</p>
     * <p>Consequently, cannot be used to track the dynamic changes in the
     * positions of vertices.</p>
     */
    private Map<Integer, PointD> position;

    /**
     * <p>A class constructor</p>
     */
    public Graph() {
        this.position = new HashMap<>();
    }

    /**
     * <p>Reads a graph from a .xml file</p>
     * @param filename the name of file to read a graph from
     */
    @Override
    public void fromFile(String filename) {
        try {
            SAXBuilder sb = new SAXBuilder();
            Document document = sb.build(filename);
            Element root = document.getRootElement();

            buildGraph(root.getChild("vertices", root.getNamespace()));
        }
        catch (IOException | JDOMException e) {
            System.out.println("The file could not be read: " + e.getMessage());
        }
    }

    /**
     * <p>Builds a graph from the xml data.</p>
     * <p>Required xml tree format is displayed below.</p>
     * <p>graph</p>
     * <p>..vertices</p>
     * <p>....vertex</p>
     * <p>......id</p>
     * <p>......position</p>
     * <p>........x</p>
     * <p>........y</p>
     * <p>......neighbors</p>
     * <p>........neighbor</p>
     * @param vertices the parent node that contains all graph vertices
     */
    private void buildGraph(Element vertices)
        throws IOException, JDOMException {

        if (vertices == null)
            return;

        for (Element vertex : vertices.getChildren()) {
            Integer id = null;
            Double x = null;
            Double y = null;
            List<Integer> edges = new ArrayList<Integer>();

            for (Element detail : vertex.getChildren()) {
                if ((detail.getName()).equals("id")) {
                    id = Integer.parseInt(detail.getText());

                    continue;
                }

                if ((detail.getName()).equals("position")) {
                    for (Element coordinate : detail.getChildren()) {
                        if ((coordinate.getName()).equals("x"))
                            x = Double.parseDouble(coordinate.getText());

                        if ((coordinate.getName()).equals("y"))
                            y = Double.parseDouble(coordinate.getText());
                    }

                    continue;
                }

                if ((detail.getName()).equals("neighbors")) {
                    for (Element neighbor : detail.getChildren())
                        edges.add(Integer.parseInt(neighbor.getText()));
                }
            }

            if (id != null && x != null && y != null) {
                this.vertex.put(id, edges);
                this.position.put(id, new PointD(x, y));
            }
            else {
                this.position.clear();

                throw new IOException();
            }
        }
    }

    /**
     * <p>Gets the positions of vertices</p>
     * @return positions of vertices
     */
    public Map<Integer, PointD> getVerticesPositions() {
        Map<Integer, PointD> copy = new HashMap<>();

        for (Map.Entry<Integer, PointD> entry : this.position.entrySet()) {
            PointD tmp = entry.getValue();

            copy.put(entry.getKey(), new PointD(tmp.getX(), tmp.getY()));
        }

        return copy;
    }

    /**
     * <p>Writes a graph to a .xml file</p>
     * @param filename the name of file to store a graph in
     */
    @Override
    public void toFile(String filename) {
        try(FileWriter fileWriter = new FileWriter(filename)) {
            Document document = new Document();

            document.setRootElement(new Element("graph"));
            document.getRootElement().addContent(store());

            XMLOutputter xmlOutputter = new XMLOutputter();

            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(document, fileWriter);
        }
        catch (IOException e) {
            System.out.println(
                "The file could not be write to: " + e.getMessage()
            );
        }
    }

    /**
     * <p>Builds the xml data to store in file.</p>
     * <p>Required xml tree format is displayed below.</p>
     * <p>graph</p>
     * <p>..vertices</p>
     * <p>....vertex</p>
     * <p>......id</p>
     * <p>......position</p>
     * <p>........x</p>
     * <p>........y</p>
     * <p>......neighbors</p>
     * <p>........neighbor</p>
     * @return the parent node that contains all graph vertices
     */
    private Element store() {
        Element vertices = new Element("vertices");

        for (Map.Entry<Integer, List<Integer>> entry : this.vertex.entrySet()) {
            PointD p = this.position.get(entry.getKey());

            Element vertex = new Element("vertex");
            vertex.addContent(
                new Element("id").setText(entry.getKey().toString())
            );

            Element position = new Element("position");
            position.addContent(new Element("x").setText(p.getX().toString()));
            position.addContent(new Element("y").setText(p.getY().toString()));
            vertex.addContent(position);

            Element neighbors = new Element("neighbors");
            for (Integer neighbor : entry.getValue())
                neighbors.addContent(
                    new Element("neighbor").setText(neighbor.toString())
                );
            vertex.addContent(neighbors);

            vertices.addContent(vertex);
        }

        return vertices;
    }

    /**
     * <p>Sets the positions of vertices</p>
     * @param copy positions of vertices
     */
    public void setVerticesPositions(Map<Integer, PointD> copy) {
        this.position.clear();

        for (Map.Entry<Integer, PointD> entry : copy.entrySet()) {
            PointD tmp = entry.getValue();

            this.position.put(
                entry.getKey(), new PointD(tmp.getX(), tmp.getY())
            );
        }
    }
}
