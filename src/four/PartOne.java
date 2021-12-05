package four;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PartOne {

    static int[] drawnNumbers;
    static ArrayList<BingoBoard> allBoards;

    public static void main(String[] args) throws Exception {
        //parse the drawing numbers
        Scanner s  = new Scanner(new File("src/four/input.txt"));

        drawnNumbers = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();

        //parse data into BingoBoards
        allBoards = new ArrayList<>();

        while(s.hasNextLine()){
            s.nextLine();
            int[][] tempData = new int[5][5];

            for(int i=0; i<5; i++){
               tempData[i] = Arrays.stream(s.nextLine().split(" ")).filter(x -> !x.equals("")).mapToInt(Integer::parseInt).toArray();
            }
            allBoards.add(new BingoBoard(tempData));
        }

        //draw all numbers, breaking out of loop if a board wins
        int winningNum = markBoards();

        //find winning board and calculate its score
        System.out.println(findWinningBoard().calcScore() * winningNum);

    }

    public static int markBoards() {
        for(int drawnNumber : drawnNumbers) {
            for(BingoBoard board : allBoards) {
                if(board.markNumber(drawnNumber)) return drawnNumber;
            }
        }

        return -1;
    }

    public static BingoBoard findWinningBoard() throws Exception {
        for(BingoBoard board : allBoards) {
            if(board.calcScore() != -1) return board;
        }

        throw new Exception("Could not find a winning board!");
    }

}
