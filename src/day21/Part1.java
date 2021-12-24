package day21;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Part1 {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day21/input.txt");
        int playerOnePos = Integer.parseInt(Files.readAllLines(f.toPath()).get(0).split(": ")[1]);
        int playerTwoPos = Integer.parseInt(Files.readAllLines(f.toPath()).get(1).split(": ")[1]);

        int[] playerOneData = new int[]{playerOnePos, 0}; //pos, score
        int[] playerTwoData = new int[]{playerTwoPos, 0};
        long[] gameData = new long[]{0, 0}; //die #, count of rolls

        while(true) {
            if(takePlayerTurn(playerOneData, gameData, 1000)) break;
            if(takePlayerTurn(playerTwoData, gameData, 1000)) break;
        }
        int losingScore = Math.min(playerOneData[1], playerTwoData[1]);
        System.out.println("Part 1: " + losingScore * gameData[1]);

    }

    //returns whether they win or not
    public static boolean takePlayerTurn(int[] playerData, long[] gameData, int winScore) {
        int moveBy = rollDeterministicDie(gameData);
        increasePlayerPos(playerData, moveBy);

        return playerData[1] >= winScore;
    }

    public static void increasePlayerPos(int[] playerData, int increaseBy) {
        playerData[0] += increaseBy;
        if(playerData[0] > 10) playerData[0] = playerData[0] % 10;
        playerData[1] += playerData[0];
    }

    //return sum of the rolls; (original times 3 +3)
    public static int rollDeterministicDie(long[] gameData) {
        int sum = 0;
        for(int i=0; i<3; i++) {
            if(gameData[0] == 100) gameData[0] = 0;
            gameData[0]++;
            sum += gameData[0];
        }
        gameData[1] += 3;
        return sum >= 10 ? sum % 10 : sum;
    }


}
