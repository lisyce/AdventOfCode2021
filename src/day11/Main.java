package day11;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Main {
    static int totalFlashes = 0;

    public static void main(String[] args) throws IOException {
        File f = new File("src/day11/main_input.txt");
        int[][] board = Files.readAllLines(f.toPath()).stream().map(x -> Arrays.stream(x.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);

        int step = 1;
        while(true) {

            //flash all octopi: increase all the ones around you. go on a train until that path can't flash anymore
            for(int row=0; row < board.length; row++) {
                for(int o=0; o < board[row].length; o++) {
                    increaseOctopusEnergy(board, o, row);
                }
            }
            //fix any that flashed to become 0
            for(int row=0; row < board.length; row++) {
                for(int o=0; o < board[row].length; o++) {
                    if(board[row][o] == -1) board[row][o] = 0;
                }
            }

            if(step == 100) System.out.println("Part One: " + totalFlashes + " total flashes");

            if(partTwo(board, step)) break;

            step++;
        }

    }

    public static boolean partTwo(int[][] board, int step) {
        //was the flash simultaneous?
        boolean simultaneous;
        for(int[] row : board) {
            simultaneous = Arrays.stream(row).allMatch(x -> x==0);
            if(!simultaneous) return false;
        }
        System.out.println("Part Two: whole board flashed at step " + step);
        return true;
    }

    public static void increaseOctopusEnergy(int[][] board, int x, int y) {
        //increase energy of this particular octopus if it hasn't already flashed this turn
        if(board[y][x] != -1) board[y][x]++;

        //recursive case: this octopus can flash, so flash all octopi around it
        if(!(board[y][x] <= 9)){
            //set current location to -1 bc it flashed
            board[y][x] = -1;
            totalFlashes++;

            //top left
            if(x != 0 && y != 0) increaseOctopusEnergy(board, x-1, y-1);
            //top center
            if(y != 0) increaseOctopusEnergy(board, x, y-1);
            //top right
            if(y != 0 && x != board[0].length-1) increaseOctopusEnergy(board, x+1, y-1);
            //left
            if(x != 0) increaseOctopusEnergy(board, x-1, y);
            //right
            if(x != board[0].length-1) increaseOctopusEnergy(board, x+1, y);
            //bottom left
            if(y != board.length-1 && x != 0) increaseOctopusEnergy(board, x-1, y+1);
            //bottom center
            if(y != board.length-1) increaseOctopusEnergy(board, x, y+1);
            //bottom right
            if(y != board.length-1 && x != board[0].length-1) increaseOctopusEnergy(board, x+1, y+1);
        }

    }

    public static void printBoard(int[][] board) {
        for(int[] row : board) {
            for(int i : row) System.out.print(i == 10 ? "*" : i);
            System.out.println();
        }
    }
}
