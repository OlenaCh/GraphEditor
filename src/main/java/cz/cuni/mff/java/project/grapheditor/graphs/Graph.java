package cz.cuni.mff.java.project.grapheditor.graphs;

import java.io.*;
import java.util.*;

/**
 * Implements a graph with IO interface.
 */
public class Graph extends AbstractGraph implements GraphIOInterface {
    /**
     * <p>Reads a graph from a .txt file</p>
     * @param filename the name of file to read a graph from
     */
    @Override
    public void fromFile(String filename) {
        try (
            BufferedReader br =
                new BufferedReader(new FileReader(new File(filename)))
        ) {

            List<String> data = new ArrayList<String>();
            String line;

            while((line = br.readLine()) != null)
                data.add(line);

            if (!data.isEmpty())
                build(data);
        }
        catch (IOException e) {
            System.out.println("The file could not be read: " + e.getMessage());
        }
    }

    /**
     * <p>Writes a graph to a .txt file</p>
     * @param filename the name of file to store a graph in
     */
    @Override
    public void toFile(String filename) {
        try {
            File file = new File(filename);
            file.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            List<String> data = store();

            for (String record : data)
                writer.write(record + "\n");

            writer.close();
        }
        catch (IOException e) {
            System.out.println(
                "The file could not be write to: " + e.getMessage()
            );
        }
    }

    /**
     * <p>Builds a graph from the lines of file.
     * Supports the following format:</p>
     * <p>1 5</p>
     * <p>2 5</p>
     * <p>3 5</p>
     * <p>4</p>
     * <p>5 1 2 3</p>
     * <p>where the first number on each line is vertex id and the rest -
     * ids of adjacent vertices</p>
     * @param data the lines that were read from file
     */
    private void build(List<String> data) {
        if (data.isEmpty())
            return;

        for (String str : data) {
            String[] verticesData = str.trim().split(" ");

            if (verticesData.length > 0) {
                Integer id = Integer.parseInt(verticesData[0]);
                List<Integer> edges = new ArrayList<Integer>();

                for (int j = 1; j < verticesData.length; j++)
                    edges.add(Integer.parseInt(verticesData[j]));

                this.vertex.put(id, edges);
            }
        }
    }

    /**
     * <p>Builds the array of strings to store in file.
     * Supports the following format:</p>
     * <p>1 5</p>
     * <p>2 5</p>
     * <p>3 5</p>
     * <p>4</p>
     * <p>5 1 2 3</p>
     * <p>where the first number on each line is vertex id and the rest -
     * ids of adjacent vertices</p>
     * @return the array of strings to store in file
     */
    private List<String> store() {
        List<String> graph = new ArrayList<String>();

        for (Integer v : this.vertex.keySet()) {
            String line = v + " ";

            for (Integer edge : this.vertex.get(v))
                line += edge + " ";

            graph.add(line);
        }

        return graph;
    }
}