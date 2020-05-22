import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractFindPathInputReaderTest {

    private AbstractFindPathInputReader inputReader;

    @Before
    public void createReader() {
        inputReader = new FindPathInputReaderFile();
    }

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void initGraphMatrixTest() {
        int[][] testOutputMatrix = inputReader.initGraphMatrix(4);
        int[][] expectedMatrix =   {{0, 0, 0, 0},
                                    {0, 0, 0, 0},
                                    {0, 0, 0, 0},
                                    {0, 0, 0, 0}};
        assertArrayEquals(testOutputMatrix, expectedMatrix);
    }

    @Test
    public void minDistanceTest() {
        int vertices = 7;
        int[] dist = {0, 0, 10, 1, 1, 2, 1};
        Boolean[] pathSet = {true, true, false, true, false, false, false};
        int returnedMinIndex = inputReader.minDistance(dist, pathSet, vertices);
        assertNotEquals(3, returnedMinIndex);
        assertNotEquals(4, returnedMinIndex);
        assertEquals(6, returnedMinIndex);
    }

    @Test
    public void connectVerticesTest() {
        char[][] map = {{'X','#','S'},
                        {'.','#','.'},
                        {'.','.','.'}};
        int rows = 3, columns = 3;
        int[][] graphMatrix = inputReader.initGraphMatrix(rows*columns);
        int[][] returnedGraphMatrix = inputReader.connectVertices(rows, columns, map, graphMatrix);
        int[][] expectedGraphMatrix = {
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 1, 0}};
        assertArrayEquals(expectedGraphMatrix, returnedGraphMatrix);
    }

    @Test
    public void printStepsTest(){
        char[][] map = {
                 {'.','.','S'},
                 {'X','#','.'},
                 {'.','.','.'}
        };
        int columns = 3;
        int endIndex = 3, startIndex = 2;
        int[] parent = {1,2,-1,0,-1,2,7,8,5};
        inputReader.printSteps(endIndex, startIndex, parent, columns, map);
        char[][] falseMapWithTrace = {
                {'.','.','S'},
                {'X','#','0'},
                {'0','0','0'}
        };
        assertNotEquals(falseMapWithTrace, map);
        char[][] expectedMapWithTrace = {
                {'0','0','S'},
                {'X','#','.'},
                {'.','.','.'}
        };
        assertArrayEquals(expectedMapWithTrace, map);
    }

    @Test
    public void dijkstraTest(){
        char[][] map = {
                {'X','#','S'},
                {'.','#','.'},
                {'.','.','.'}};
        int columns = 3;
        int vertices = 9;
        int startIndex = 2, endIndex = 0;
        int[][] graphMatrix = {
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 1, 0}};
        char[] resultCharArray = inputReader.dijkstra(columns, map, graphMatrix, vertices, startIndex, endIndex);
        String resultString = new String(resultCharArray).trim();
        String expectedResult = "d,d,l,l,u,u";
        assertEquals(expectedResult, resultString);
    }

    @Test
    public void exceptionInDijkstraTest(){
        char[][] map = {
                {'X','#','S'},
                {'.','#','#'},
                {'.','.','.'}};
        int columns = 3;
        int vertices = 9;
        int startIndex = 2, endIndex = 0;
        int[][] graphMatrix = {
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1, 0}};
        exit.expectSystemExitWithStatus(1);
        inputReader.dijkstra(columns, map, graphMatrix, vertices, startIndex, endIndex);
    }

    @Test
    public void handleMapTestSmall(){
        List<String> testMaze = new ArrayList<String>();
        testMaze.add(".....");
        testMaze.add("S#...");
        testMaze.add("#X#..");
        testMaze.add("...#.");
        testMaze.add(".....");
        int rows = testMaze.size();
        int columns = testMaze.get(0).length();
        String resultString = inputReader.handleMap(rows, columns, testMaze);
        String expectedString = "u,r,r,d,r,d,r,d,d,l,l,l,u,u";
        assertEquals(expectedString, resultString);
    }

    @Test
    public void handleMapTestLarge(){
        List<String> testMaze = new ArrayList<String>();
        testMaze.add("..................................");
        testMaze.add("S...#......................#......");
        testMaze.add("....#......................#......");
        testMaze.add("...........................#......");
        testMaze.add("..................................");
        testMaze.add("..................................");
        testMaze.add("............#.....................");
        testMaze.add("..........#.......................");
        testMaze.add("........#.........................");
        testMaze.add(".......................#..........");
        testMaze.add("...................#..#X#.....#...");
        testMaze.add("...................#..........#...");
        testMaze.add("...................#..........#...");
        testMaze.add("..................................");
        int rows = testMaze.size();
        int columns = testMaze.get(0).length();
        String resultString = inputReader.handleMap(rows, columns, testMaze);
        char[] expectedPath = {
                 'd', ',', 'd', ',', 'd', ',', 'd', ',', 'd', ',', 'd', ',', 'd', ',', 'd', ',',
                 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',',
                 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'r', ',',
                 'r', ',', 'r', ',', 'r', ',', 'r', ',', 'd', ',', 'd', ',', 'r', ',', 'r', ',',
                 'r', ',', 'u'
        };
        String expectedString = new String(expectedPath).trim();
        assertEquals(expectedString, resultString);
    }
}