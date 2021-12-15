package day13;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day13/main_input.txt");
        //fill a spot on the board with a 0 if no dot and a 1 if there's a dot

        //parse down file
        List<String> allLines = Files.readAllLines(f.toPath());
        int blankLineIndex = allLines.indexOf("");
        List<int[]> coordinates = allLines.stream().filter(x -> allLines.indexOf(x) < blankLineIndex)
                .map(x -> Arrays.stream(x.split(",")).mapToInt(Integer::parseInt).toArray()).collect(Collectors.toList());

        List<String[]> foldingDirections = allLines.stream().filter(x -> allLines.indexOf(x) > blankLineIndex)
                .map(x -> x.substring(11).split("=")).collect(Collectors.toList());

        int[][] board = generateBoardFromCoords(coordinates);

        //fold board
        boolean isFirstLoop = true;
        int visibleDotsAfterFirstFold = -1; //default error value

        for(String[] direction : foldingDirections) {
            String dimension = direction[0];
            int foldLine = Integer.parseInt(direction[1]);
            if(dimension.equals("y")) { //fold up
                for(int[] coord : coordinates) {
                    if(coord[1] > foldLine) { //below fold line
                        coord[1] = coord[1] - 2 * (coord[1] - foldLine);
                    }
                }
            } else { //fold left
                for(int[] coord : coordinates) {
                    if(coord[0] > foldLine) { //to the right of fold line
                        coord[0] = coord[0] - 2 * (coord[0] - foldLine);
                    }
                }
            }

            //count visible dots after first loop
            if(isFirstLoop) {
                //remove duplicates in coordinates
                List<int[]> duplicates = new LinkedList<>();
                for(int i=0; i<coordinates.size(); i++) {
                    for(int j=i+1; j<coordinates.size(); j++) {
                        if(Arrays.equals(coordinates.get(i), coordinates.get(j))) {
                            duplicates.add(coordinates.get(i));
                        }
                    }
                }
                duplicates.forEach(coordinates::remove);
                visibleDotsAfterFirstFold = coordinates.size();
            }
            //resize board and update it
            board = generateBoardFromCoords(coordinates);
            isFirstLoop = false;
        }

        System.out.printf("%d dots are visible after the first fold.\n", visibleDotsAfterFirstFold);
        printBoard(board, true);
    }

    public static int[][] generateBoardFromCoords(List<int[]> coordinates) {
        //make board proper size to fit all data
        int xDimension = coordinates.stream().mapToInt(x -> x[0]).max().getAsInt() + 1;
        int yDimension = coordinates.stream().mapToInt(y -> y[1]).max().getAsInt() + 1;

        //fill up board
        int[][] board = new int[yDimension][xDimension];
        coordinates.forEach(x -> board[x[1]][x[0]] = 1);
        return board;
    }

    public static void printBoard(int[][] board, boolean visibilityOn) {
        String emptySpace = visibilityOn ? " " : ".";

        for(int[] row : board) {
            for(int i : row) System.out.print(i == 0 ? emptySpace : "#");
            System.out.println();
        }
    }

}
