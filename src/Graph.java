import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    int[][] capacity;  // Adjacency  matrix
    int[][] residual;
    String graphName;  //The file from which the graph was created.

    public Graph() {
        this.vertexCt = 0;
        this.graphName = "";
    }

    public int addFlow(int[] path) { // parameters: path to add flow along, amount of flow?
        int flow = pathFlow(path);
        for (int i = 0; i < path.length - 1; i++) {
            residual[path[i]][path[i+1]] -= flow;
            residual[path[i+1]][path[i]] += flow;
        }
        return flow;
    }

    public int pathFlow(int[] path) {
        if (path.length < 2) return 0;
        int flow = residual[path[0]][path[1]];
        for (int i = 0; i < path.length - 1; i++) {
            if (residual[path[i]][path[i+1]] < flow) {
                flow = residual[path[i]][path[i + 1]];
            }
        }
        return flow;
    }

    public int[] shortestPath() {
        Queue<int[]> queue = new Queue<>();
        int[] startPath = { 0 };
        queue.enqueue(startPath);
        // while loop for bfs - look for shortest path
        while (!queue.isEmpty()) {
            // curr: most recently dequeued
            int[] curr = queue.dequeue();
            // visited: anything in the current path is true, anything that's not is false
            boolean[] visited = new boolean[vertexCt];
            // node we're about to check edges from
            int lastStep = curr[curr.length - 1];
            if (lastStep == vertexCt - 1) {
                return curr;
            }
            // mark stuff we've visited true
            for (int i : curr) {
                visited[i] = true;
            }
            // finding all eligible next steps and queuing them
            for (int i = 0; i < vertexCt; i++) {
                // check that it's eligible (there's an edge between the last thing in the current path,
                if (residual[lastStep][i] != 0 && !(visited[i])) {
                    // copying current path
                    int[] next = new int[curr.length + 1];
                    for (int j = 0; j < curr.length; j++) {
                        next[j] = curr[j];
                    }
                    next[curr.length] = i;
                    // set i as visited
                    visited[i] = true;
                    queue.enqueue(next);
                }
            }
        }
        return new int[] {};
    }

    public String printPath(int[] path) {
        StringBuilder sb = new StringBuilder();
        for (int i : path) {
            sb.append(i);
            sb.append(" ");
        }
        return sb.toString();
    }

//    public int[] shortestPath(int start, int end) {
//        // if we reached the end we're done
//        Queue<int[]> queue = new Queue<>();
//        if (start == end) return new int[] {};
//        int[] startPath = {start};
//        queue.enqueue(startPath);
//        int whileLoop = 0;
//        int forLoop = 0;
//        while (!queue.isEmpty()) {
//            boolean[] visited = new boolean[vertexCt];
//            for (int i = 0; i < vertexCt; i++) {
//                visited[i] = false;
//            }
//            int[] curr = queue.dequeue();
//            System.out.print("dequeued ");
//            for (int i : curr) {
//                System.out.print(i + " ");
//            }
//            System.out.println();
//            //System.out.println("for loop not started");
//            for (int i = 0; i < vertexCt; i++) {
//                System.out.print("FOR LOOP: i = " + i + ", vertex count = " + vertexCt + ", while: " + whileLoop + ", for: " + forLoop + ", ");
//                // if there's an edge
//                if (residual[start][i] != 0 &! visited[i]) {
//                    System.out.print("yes, ");
//                    // add the partial path to the queue
//                    int[] next = new int[curr.length + 1];
//                    for (int j = 0; j < curr.length; j++) {
//                        next[j] = curr[j];
//                    }
//                    visited[i] = true;
//                    next[next.length - 1] = i;
//                    System.out.println("added " + i);
//                    if (i == end) {
//                        return next;
//                    }
//                    for (int j : next) {
//                        //System.out.print(j + " ");
//                    }
//                    //System.out.println();
//                    queue.enqueue(next);
//                }
//                System.out.println();
//                forLoop++;
//            }
//            whileLoop++;
//        }
//        return new int[] {};
//    }

    public void maxFlow() {
        this.maxFlow(0, vertexCt);
    }

    public int maxFlow(int start, int end) {
        int totalFlow = 0;
        int[] currentPath = shortestPath();
        while (currentPath.length > 0) {
            int pathFlow = addFlow(currentPath);
            totalFlow += pathFlow;
            System.out.println("Found flow " + pathFlow + ": " + printPath(currentPath));
            currentPath = shortestPath();
        }
        return totalFlow;
    }

//    public void printPaths(int start, int end, String previous) {
//        if (start == end) {
//            System.out.println(previous + ", " + start);
//        }
//        // for each node that start is connected to, run printAllPaths till we find the end
//        for (int i = 0; i < vertexCt; i++) {
//            if (capacity[start][i] != 0) {
//                printPaths(i, end, previous + ", " + start);
//            }
//        }
//    }

    public int getVertexCt() {
        return vertexCt;
    }

    public boolean addEdge(int source, int destination, int cap) {
        //System.out.println("addEdge " + source + "->" + destination + "(" + cap + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        capacity[source][destination] = cap;
        residual[source][destination] = cap;

        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nThe Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d", capacity[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toStringResidual() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nResidual Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d", residual[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            capacity = new int[vertexCt][vertexCt];
            residual = new int[vertexCt][vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                for (int j = 0; j < vertexCt; j++) {
                    capacity[i][j] = 0;
                    residual[i][j] = 0;
                }
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                if (!addEdge(v1, v2, cap))
                    throw new Exception();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Graph graph0 = new Graph();
        graph0.makeGraph("demands1.txt");
        int[] path1 = { 0, 2, 4, 5 };
        int[] path2 = { 0, 1, 3, 5 };
        int[] path3 = { 0, 1, 2, 3, 4, 5 };
        System.out.println(graph0);
//        System.out.println("Path flow for path1: " + graph0.pathFlow(path1));
//        System.out.println("Path flow for path2: " + graph0.pathFlow(path2));
//        System.out.println("Path flow for path3: " + graph0.pathFlow(path3));
//        graph0.addFlow(path1);
//        graph0.addFlow(path2);
//        System.out.println("Path flow for path3 after adding flow: " + graph0.pathFlow(path3));
        graph0.maxFlow();
        System.out.println(graph0.toStringResidual());

//        Graph graph1 = new Graph();
//        graph1.makeGraph("demands2.txt");
//        System.out.println(graph1);
//        graph1.maxFlow();
//
//        Graph graph2 = new Graph();
//        graph2.makeGraph("demands3.txt");
//        System.out.println(graph2);
//
//        Graph graph3 = new Graph();
//        graph3.makeGraph("demands4.txt");
//        System.out.println(graph3);
//
//        Graph graph4 = new Graph();
//        graph4.makeGraph("demands5.txt");
//        System.out.println(graph4);
//
//        Graph graph5 = new Graph();
//        graph5.makeGraph("demands6.txt");
//        System.out.println(graph5);
    }
    private class Path {
        private int flow;
        ArrayList<Integer> nodes;

        public Path(ArrayList<Integer> nodes) {
            this.nodes = nodes;
        }

        public int getFlow() {
            return flow;
        }
    }
}