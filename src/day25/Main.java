package day25;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day25/main_input.txt");
        char[][] board = Files.readAllLines(f.toPath()).stream().map(String::toCharArray).toArray(char[][]::new);

        int count = 1;
        while(true) {
            if(!simulateStep(board)) {
                System.out.println(count);
                break;
            }
            count++;
        }

    }

    //returns whether or not anything moved
    public static boolean simulateStep(char[][] board) {
        boolean eastMoved = simulateHerdMovement(board, 'E');
        boolean southMoved = simulateHerdMovement(board, 'S');
        return eastMoved || southMoved;
    }

    public static void printBoard(char[][] board) {
        System.out.println();
        for(char[] row : board) {
            for(char space : row) System.out.print(space);
            System.out.println();
        }
    }

    //returns whether not anything moved
    public static boolean simulateHerdMovement(char[][] board, char eastOrSouth) {
        LinkedList<int[]> newLocations = new LinkedList<>();
        LinkedList<int[]> oldLocations = new LinkedList<>();
        boolean movementOccurred = false;

        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                char symbol = board[i][j];
                if(symbol == '.') continue;

                //east herd
                if(symbol == '>' && eastOrSouth == 'E') {
                    //calculate next position
                    int nextX = j == board[i].length-1 ? 0 : j+1;
                    //is that space open?
                    if(board[i][nextX] == '.') {
                        movementOccurred = true;
                        newLocations.add(new int[]{nextX, i}); //x, y
                        oldLocations.add(new int[]{j, i});
                    }
                }

                //south herd
                else if(symbol == 'v' && eastOrSouth == 'S') {
                    int nextY = i == board.length-1 ? 0 : i+1;
                    if(board[nextY][j] == '.') {
                        movementOccurred = true;
                        newLocations.add(new int[]{j, nextY});
                        oldLocations.add(new int[]{j, i});
                    }
                }

            }
        }
        //actually perform the movement
        newLocations.forEach(x -> board[x[1]][x[0]] = eastOrSouth == 'E' ? '>' : 'v');
        oldLocations.forEach(x -> board[x[1]][x[0]] = '.');

        return movementOccurred;
    }
}
