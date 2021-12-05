package day4;

import java.io.File;
import java.util.*;


public class Main {

    static int[] drawnNumbers;
    static final ArrayList<BingoBoard> allBoards = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        //parse the drawing numbers
        Scanner s  = new Scanner(new File("src/four/input.txt"));

        drawnNumbers = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();

        //parse data into BingoBoards

        while(s.hasNextLine()){
            s.nextLine();
            int[][] tempData = new int[5][5];

            for(int i=0; i<5; i++){
               tempData[i] = Arrays.stream(s.nextLine().split(" ")).filter(x -> !x.equals("")).mapToInt(Integer::parseInt).toArray();
            }
            allBoards.add(new BingoBoard(tempData));
        }

        partOne();
        partTwo();

    }

    public static void partOne() throws Exception {
        //draw all numbers, breaking out of loop if a board wins
        int winningNum = markBoards(true);

        //find winning board and calculate its score
        System.out.println(findWinningBoard().calcScore() * winningNum);
    }

    public static void partTwo() {

        int endNum = markBoards(false);
        //find last winning board
        BingoBoard winningBoard = (BingoBoard) allBoards.stream().filter(BingoBoard::isLastToWin).toArray()[0];
        System.out.println(endNum * winningBoard.calcScore());

    }


    public static int markBoards(boolean stopAtFirstWin) {
        for(int drawnNumber : drawnNumbers) {
            for(BingoBoard board : allBoards) {
                if(board.markNumber(drawnNumber) && stopAtFirstWin) return drawnNumber;
            }
            //see if one board is still a loser
            if(!stopAtFirstWin) {
                int loserCount = (int) allBoards.stream().filter(x -> !x.checkWinner()).count();

                //if we've got one board remaining, set it as the lsat to win
                if(loserCount == 1) {
                    for(BingoBoard board : allBoards) {
                        if(!board.checkWinner()) board.setLastToWin(true);
                    }
                }

                if(loserCount == 0) {
                    return drawnNumber;
                }
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
