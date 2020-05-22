//Source of Dijsktra's shortest path algorithm implementation: https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/

import java.util.List;

public abstract class AbstractFindPathInputReader {
    public abstract String readInput();

    public int minDistance(int[] dist, Boolean[] pathSet, int vertices)
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;
        for (int v = 0; v < vertices; v++)
            if (!pathSet[v] && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        return min_index;
    }

    public void printPathInMap(int rows, int columns, char[][] map){
        for (int row=0; row < rows; row++){
            for (int column = 0; column < columns; column++){
                System.out.print(map[row][column]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public int[][] initGraphMatrix(int vertices){
        int[][] graphMatrix = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++){ //initialize adjacency matrix of graph
            for (int j = 0; j < vertices; j++){
                graphMatrix[i][j] = 0;
            }
        }
        return graphMatrix;
    }

    public int[][] connectVertices(int rows, int columns, char[][] map, int[][] graphMatrix){
        //System.out.println(rows+" "+columns);
        for (int row=0; row < rows; row++){
            for (int column = 0; column < columns; column++){
                if ('#' != map[row][column]) {
                    if ((row + 1 < rows) && ('#' != map[row + 1][column])){

                        graphMatrix[row*columns + column][(row + 1)*columns + column] = 1;
                        graphMatrix[(row + 1)*columns + column][row*columns + column] = 1;
                    }
                    if ((column + 1 < columns) && ('#' != map[row][column + 1])){
                        graphMatrix[row*columns + column][row*columns + column + 1] = 1;
                        graphMatrix[row*columns + column + 1][row*columns + column] = 1;
                    }
                }
            }
        }
        return graphMatrix;
    }

    public char[] dijkstra(int columns, char[][] map, int[][] graphMatrix,
                           int vertices, int startIndex, int endIndex){
        int[] dist = new int[vertices];
        Boolean[] pathSet = new Boolean[vertices];
        int[] parent = new int[vertices];

        for (int vertex = 0; vertex < vertices; vertex++){
            dist[vertex] = Integer.MAX_VALUE;
            pathSet[vertex] = false;
            parent[vertex] = -1;
        }

        dist[startIndex] = 0;
        int nearestVertex;

        for (int vertex = 0; vertex < vertices - 1; vertex++){
            nearestVertex = minDistance(dist, pathSet, vertices);
            pathSet[nearestVertex] = true;
            //System.out.println("nearest vertex: "+nearestVertex);
            for (int neighborVertex = 0; neighborVertex < vertices; neighborVertex++){
                if (!pathSet[neighborVertex]
                        && graphMatrix[nearestVertex][neighborVertex] != 0
                        && dist[nearestVertex] != Integer.MAX_VALUE
                        && (dist[nearestVertex] + graphMatrix[nearestVertex][neighborVertex] < dist[neighborVertex])) {
                    //System.out.println(neighborVertex+" original distance "+dist[neighborVertex]);
                    dist[neighborVertex] = dist[nearestVertex] + graphMatrix[nearestVertex][neighborVertex];
                    //System.out.println(neighborVertex+" new distance "+dist[neighborVertex]+" parent "+nearestVertex);
                    parent[neighborVertex] = nearestVertex;
                }
            }
        }
        try{
            if (dist[endIndex] == Integer.MAX_VALUE){
                throw new ImpossiblePathException();
                //System.out.println("Error: Impossible to create a path from start to finish.");
            }
        } catch (ImpossiblePathException e) {
        }

        return printSteps(endIndex, startIndex, parent, columns, map);
    }

    public char[] printSteps(int endIndex, int startIndex,
                             int[] parent, int columns, char[][] map){
        int nearestVertex = endIndex;
        int stepCounter = 0;
        while(parent[nearestVertex] != -1){
            //System.out.println(nearestVertex+" "+parent[nearestVertex]);
            nearestVertex = parent[nearestVertex];
            stepCounter++;
        }
        //System.out.println(stepCounter);
        stepCounter++;
        int[] path = new int[stepCounter];
        int index = 0;
        nearestVertex = endIndex;
        while(parent[nearestVertex] != -1){
            path[stepCounter - index - 1] = nearestVertex;
            //System.out.println(nearestVertex+" | "+(stepCounter - index - 1));
            nearestVertex = parent[nearestVertex];
            index++;
        }

        path[0] = startIndex;
        char[] pathString = new char[stepCounter];
        char[] resultCharArray = new char[stepCounter*2];
        int currentPositionX, currentPositionY, nextPositionX, nextPositionY;
        for (index = 0; index < stepCounter; index++) {
            if (index < stepCounter - 1){
                currentPositionX = path[index] / columns;
                currentPositionY = path[index] % columns;
                nextPositionX = path[index+1] / columns;
                nextPositionY = path[index+1] % columns;

                if (currentPositionX > nextPositionX){
                    pathString[index] = 'u';
                }
                if (currentPositionX < nextPositionX){
                    pathString[index] = 'd';
                }
                if (currentPositionY > nextPositionY){
                    pathString[index] = 'l';
                }
                if (currentPositionY < nextPositionY){
                    pathString[index] = 'r';
                }
                //System.out.print(pathString[index]);
                resultCharArray[index*2] = pathString[index];
                if (index != stepCounter - 2) {
                    //System.out.print(",");
                    resultCharArray[index*2 + 1] = ',';
                }
                if (path[index] != startIndex)
                    map[currentPositionX][currentPositionY] = '0';
            }
        }
        //System.out.println();
        /*for (int i = 0; i < 2*stepCounter; i++){
            System.out.print(resultCharArray[i]);
        }*/
        return resultCharArray;
    }

    public String handleMap(int rows, int columns, List<String> stringArrayList){
        int startIndex = 0, endIndex = 0;
        //int rows = stringArrayList.size(), columns = stringArrayList.get(0).length();
        //System.out.println(stringArrayList.toString()+rows+columns);

        char[][] map = new char[rows][columns];
        for (int row=0; row < rows; row++){
            for (int column = 0; column < columns; column++){
                map[row][column] = stringArrayList.get(row).charAt(column);
                if ('S' == map[row][column]) {
                    //System.out.println(row+"S"+column);
                    startIndex = row*columns + column;
                } else if ('X' == map[row][column]){
                    //System.out.println(row+"X"+column);
                    endIndex = row*columns + column;
                }
            }
        }
        //System.out.println(startIndex+" "+endIndex);
        int vertices = rows*columns;
        int[][] graphMatrix = initGraphMatrix(vertices);
        connectVertices(rows, columns, map, graphMatrix);
        char[] foundCharPath = dijkstra(columns, map, graphMatrix, vertices, startIndex, endIndex);
        printPathInMap(rows, columns, map);
        return new String(foundCharPath).trim();
    }
}