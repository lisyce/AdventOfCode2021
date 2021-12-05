package day5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Main {

    static final int[][] board = new int[1000][1000];
    public static void main(String[] args) throws IOException {
        partTwo();
    }

    public static void partOne() throws IOException {
        File f = new File("src/five/input.txt");

        List<String> lines = Files.readAllLines(f.toPath());
        for(String line : lines) {
            int[] points = Arrays.stream(line.replaceAll(" ", "").split("->|,")).map(String::strip).mapToInt(Integer::parseInt).toArray();
            populateLine(board, new int[]{points[0], points[1]}, new int[]{points[2], points[3]}, false);
        }

        int dangerousAreas = (int) Arrays.stream(board).flatMapToInt(Arrays::stream).filter(y -> y > 1).count();


        printBoard(board);
        System.out.println();
        System.out.println(dangerousAreas);
    }

    public static void partTwo() throws IOException {
        File f = new File("src/five/input.txt");

        List<String> lines = Files.readAllLines(f.toPath());
        for(String line : lines) {
            int[] points = Arrays.stream(line.replaceAll(" ", "").split("->|,")).map(String::strip).mapToInt(Integer::parseInt).toArray();
            populateLine(board, new int[]{points[0], points[1]}, new int[]{points[2], points[3]}, true);
        }

        int dangerousAreas = (int) Arrays.stream(board).flatMapToInt(Arrays::stream).filter(y -> y > 1).count();

        System.out.println();
        System.out.println("dangerous: " + dangerousAreas);
    }

    public static void printBoard(int[][] boardToPrint) {
        for(int[] row : boardToPrint) {
            Arrays.stream(row).forEach(System.out::print);
            System.out.println();
        }
    }

    public static void populateLine(int[][] boardToPopulate, int[] p1, int[] p2, boolean considerDiagonals) {
        //if we aren't considering diagonals and this is a diagonal, do nothing
        if(!considerDiagonals && !(p1[0] == p2[0] || p1[1] == p2[1])) return;

        //determine horizontal or vertical line
        //horizontal
        if(p1[1] == p2[1]) {
            //go to the y then fill sideways
            int y = p1[1];
            //start at the x that's furthest left
            int x = Math.min(p1[0], p2[0]);
            for(int i=x; i<=Math.max(p1[0], p2[0]); i++) {
                boardToPopulate[y][i]++;
            }
        }

        //vertical
        else if(p1[0] == p2[0]) {
            int x = p1[0];
            int y = Math.min(p1[1], p2[1]);
            for(int i=y; i<=Math.max(p1[1], p2[1]); i++) {
                boardToPopulate[i][x]++;
            }
        }

        //diagonals
        //start at top left or top right
        else {
            int yDirection = p1[1] > p2[1] ? -1 : 1;
            int y = p1[1];

            //left to right
            if(p1[0] < p2[0]) {
                for(int x=p1[0]; x<=p2[0]; x++) {
                    boardToPopulate[y][x]++;
                    y+=yDirection;
                }
            } else { //right to left
                for(int x=p1[0]; x>=p2[0]; x--) {
                    boardToPopulate[y][x]++;
                    y+=yDirection;
                }
            }

        }

    }
}
