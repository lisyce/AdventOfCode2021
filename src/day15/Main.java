package day15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static int boardSize = -1;

    public static void main(String[] args) throws IOException {
        //parse board
        File f = new File("src/day15/test_input.txt");
        int[][] board = Files.readAllLines(f.toPath()).stream().map(x ->
                Arrays.stream(x.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
        boardSize = board.length;

        //explore board

    }


    //VERY BROKEN, TRY AGAIN LATER FROM SCRATCH
//    public static int explore(int totalRiskLevel, int currentX, int currentY, int[][] board, int smallestRisk) {
//        //add the current position to total risk level
//        if(currentX != 0 && currentY != 0) totalRiskLevel += board[currentY][currentX];
//
//        //base case: we are in bottom right corner
//        if(currentX == boardSize-1 && currentY == boardSize-1) {
//            smallestRisk = totalRiskLevel;
//            System.out.println(totalRiskLevel);
//        }
//        //recursive case: we haven't exceeded the smallest risk yet
//        else if(totalRiskLevel < smallestRisk) {
//
//            //explore right
//            if(currentY == boardSize-1 || (currentX < boardSize-1 && board[currentY][currentX+1] <= board[currentY+1][currentX])) {
//                int thisSmallestRisk = explore(totalRiskLevel, currentX+1, currentY, board, smallestRisk);
//                if(thisSmallestRisk < smallestRisk) smallestRisk = thisSmallestRisk;
//            }
//            //explore down
//            else if(currentX == boardSize-1 || (currentY < boardSize-1 && board[currentY+1][currentX] <= board[currentY][currentX+1])) {
//                int thisSmallestRisk = explore(totalRiskLevel, currentX, currentY+1, board, smallestRisk);
//                if(thisSmallestRisk < smallestRisk) smallestRisk = thisSmallestRisk;
//            }
//
//        }
//
//
//        return smallestRisk;
//    }


    public static void printBoard(int[][] board) {
        for(int[] row : board) {
            for(int i : row) System.out.print(i);
            System.out.println();
        }
    }
}
