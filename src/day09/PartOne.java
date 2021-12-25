
package day09;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;


public class PartOne {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day09/main_input.txt");
        Integer[][] board = Files.readAllLines(f.toPath()).stream().map(x ->
                Arrays.stream(x.split("")).map(Integer::parseInt).toArray(Integer[]::new)
        ).toArray(Integer[][]::new);

        //if you find a low point, you can skip the next one to the right as you read across
        int riskLevel = 0;
        for(int y=0; y<board.length; y++) {
            for(int x=0; x<board[y].length; x++) {
                if(isLowPoint(board, x, y)) {
                    riskLevel += board[y][x] + 1;
                }
            }
        }
        System.out.println(riskLevel); //1716 is too high

    }

    //DON'T TOUCH THIS, IK MY LOGIC IS NOT EFFICIENT BUT IT WORKS
    public static boolean isLowPoint(Integer[][] board, int x, int y) {
        int pointValue = board[y][x];
        if(pointValue == 0) return true;
        else if(pointValue == 9) return false;
        boolean lowPoint = true;

        if(x != 0 && x != board[0].length-1) {
            if(pointValue > board[y][x-1] || pointValue > board[y][x+1]) {
                lowPoint = false;
            }
        } else if(x == 0) {
            if(pointValue > board[y][1]) lowPoint = false;
        } else if(x == board[0].length-1) {
            if(pointValue > board[y][x-1]) lowPoint = false;
        }

        if(y != 0 && y != board.length-1) {
            if(pointValue > board[y-1][x] || pointValue > board[y+1][x]) lowPoint = false;
        } else if(y == 0) {
            if(pointValue > board[1][x]) lowPoint = false;
        } else if(y == board.length-1) {
            if(pointValue > board[y-1][x]) lowPoint = false;
        }

        return lowPoint;
    }
}