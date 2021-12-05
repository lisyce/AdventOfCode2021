package day4;

import java.util.Arrays;

public class BingoBoard {
    //set value to -1 if it gets marked
    private final int[][] dataSet = new int[5][5];
    private boolean isWinner = false;
    private boolean lastToWin = false;

    public BingoBoard(int[][] dataSet) {
        for(int i=0; i<dataSet.length; i++){
            System.arraycopy(dataSet[i], 0, this.dataSet[i], 0, dataSet[i].length);
        }
    }

    public void printBoard() {
        for(int[] row : dataSet) System.out.println(Arrays.toString(row));
        System.out.println();
    }

    public boolean markNumber(int number) {
        for(int i=0; i<dataSet.length; i++) {
            for(int j=0; j<dataSet[i].length; j++) {
                if(dataSet[i][j] == number) dataSet[i][j] = -1;
            }
        }

        return checkWinner();
    }

    public boolean checkWinner() {
        //horizontal
        for(int[] row : dataSet) {
            if(Arrays.stream(row).allMatch(x -> x == -1)) this.isWinner = true;
        }

        //vertical
        for(int i=0; i<5; i++) {
            int[] column = new int[5];
            for(int j=0; j<dataSet[i].length; j++) column[j] = dataSet[j][i];
            if(Arrays.stream(column).allMatch(x -> x == -1)) this.isWinner = true;
        }

        return isWinner;
    }

    public int calcScore() {
        if(!isWinner) return -1;

        int score = 0;

        for(int[] row : dataSet) {
            score += Arrays.stream(row).filter(x -> x != -1).sum();
        }
        return score;
    }

    public void setLastToWin(boolean lastToWin) {
        this.lastToWin = lastToWin;
    }

    public boolean isLastToWin() {
        return this.lastToWin;
    }
}
