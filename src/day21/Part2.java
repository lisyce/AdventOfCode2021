package day21;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;

@SuppressWarnings("DuplicatedCode")
public class Part2 {
    public static void main(String[] args) throws IOException {

        File f = new File("src/day21/input.txt");
        int playerOnePos = Integer.parseInt(Files.readAllLines(f.toPath()).get(0).split(": ")[1]);
        int playerTwoPos = Integer.parseInt(Files.readAllLines(f.toPath()).get(1).split(": ")[1]);

        int[] playerOneData = new int[]{playerOnePos, 0}; //pos, score
        int[] playerTwoData = new int[]{playerTwoPos, 0};
        BigInteger[] universeWins = new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};

//        for(int i=1; i<=3; i++) simulateUniverse(playerOneData, playerTwoData, universeWins, i, 1);
        for(int i=1; i<=3; i++) rollDie(playerOneData, playerTwoData, universeWins, 1, 1, i);

        System.out.println(universeWins[0]);
        System.out.println(universeWins[1]);

    }

    //idea: if next player wins in 2/3 universes or in 1/3 universes, also don't simulate those DONE
    public static void rollDie(int[] playerOneData, int[] playerTwoData, BigInteger[] universeWins, int playerTurn, int rollOnTurn, int dieRoll) {
        BigInteger universesSimulated = universeWins[0].add(universeWins[1]);
        System.out.println(universesSimulated + " universes simulated");

        int[] newPlayerOneData = playerOneData.clone();
        int[] newPlayerTwoData = playerTwoData.clone();
        int[] playersData = playerTurn == 1 ? newPlayerOneData : newPlayerTwoData;

        int nextPlayer = playerTurn == 1 ? 2 : 1;
        int[] nextPlayersData = nextPlayer == 1 ? newPlayerOneData : newPlayerTwoData;

        boolean playerWins = movePlayer(playersData, dieRoll, rollOnTurn);

        if(playerWins && rollOnTurn == 3) { //base case: it's end of this player's turn and they won
            universeWins[playerTurn-1] = universeWins[playerTurn-1].add(BigInteger.ONE);
        } else if(rollOnTurn < 3) { //it's still this player's turn. simulate their next dice roll
            for(int i=1; i<=3; i++) rollDie(newPlayerOneData, newPlayerTwoData, universeWins, playerTurn, rollOnTurn+1, i);
        } else if(nextPlayersData[1] >= 20) { //next player will win no matter what. this player's turn over
            universeWins[nextPlayer-1] = universeWins[nextPlayer-1].add(new BigInteger(String.valueOf(3)));
        } else if(nextPlayersData[1] >= 19) { //next player will win if they roll 2 or 3. simulate them rolling a 1. this player turn over
            universeWins[nextPlayer-1] = universeWins[nextPlayer-1].add(BigInteger.TWO);
            rollDie(newPlayerOneData, newPlayerTwoData, universeWins, nextPlayer, 1, 1);
        } else if(nextPlayersData[1] >= 18) { //next player will win if they roll a 3. simulate the rolls for 1 and 2
            universeWins[nextPlayer-1] = universeWins[nextPlayer-1].add(BigInteger.ONE);
            for(int i=1; i<=2; i++) rollDie(newPlayerOneData, newPlayerTwoData, universeWins, nextPlayer, 1, i);
        } else { //this player's turn is over and they haven't won. start the next player's turn
            for(int i=1; i<=3; i++) rollDie(newPlayerOneData, newPlayerTwoData, universeWins, nextPlayer, 1, i);
        }
    }

//    public static void simulateUniverse(int[] playerOneData, int[] playerTwoData, BigInteger[] universeWins, int dieRoll, int playerTurn) {
//        int[] newPlayerOneData = playerOneData.clone();
//        int[] newPlayerTwoData = playerTwoData.clone();
//        int[] playersData = playerTurn == 1 ? newPlayerOneData : newPlayerTwoData;
//        int[] nextPlayersData = playerTurn == 1 ? newPlayerTwoData : newPlayerOneData;
//        //take this player's turn
//        boolean playerWins = movePlayer(playersData, dieRoll);
//
//
//        if(playerWins) { //base case, this player won
//            universeWins[playerTurn-1] = universeWins[playerTurn-1].add(BigInteger.ONE);
//        } else if(nextPlayersData[0] >= 20) { //next player will win no matter what
//            int nextPlayer = playerTurn == 1 ? 2 : 1;
//            universeWins[nextPlayer-1] = universeWins[nextPlayer-1].add(new BigInteger(String.valueOf(3)));
//        } else { //recursive case: next player's turn (3 universes)
//
//            for(int i=1; i<=3; i++) {
//                simulateUniverse(newPlayerOneData, newPlayerTwoData, universeWins, i, playerTurn == 1 ? 2 : 1);
//            }
//        }
//
//    }
//
    public static boolean movePlayer(int[] playerData, int increaseBy, int rollOnTurn) {
        playerData[0] += increaseBy;
        if(playerData[0] > 10) playerData[0] = playerData[0] % 10;
        if(rollOnTurn == 3) playerData[1] += playerData[0];
        return playerData[1] >= 21;
    }
}
