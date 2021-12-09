package day9;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day9/test_input.txt");
        List<Map<Integer, Boolean>> board = Files.readAllLines(f.toPath()).stream().map(x ->
                Arrays.stream(x.split("")).map(Integer::parseInt).collect(Collectors.toMap(
                        k -> k, v -> false
                ))
        ).collect(Collectors.toCollection(LinkedList::new));

        //part 1
        //if you find a low point, you can skip the next one to the right as you read across
        int riskLevel = 0;
        for(int y=0; y<board.size(); y++) {
            for(int x=0; x<board.get(y).size(); x++) {
                if(isLowPoint(board, x, y)) {
                    riskLevel += board[y][x] + 1;
                }
            }
        }
        System.out.println("Risk level: " + riskLevel);

        //part 2
        List<Integer> basins = new LinkedList<>();
        //map whole board to whether or not an area was counted (the algorithm will
        //prob retrace some of its steps and we don't wanna double count)


    }

    //DON'T TOUCH THIS, IK MY LOGIC IS NOT EFFICIENT BUT IT WORKS
    public static boolean isLowPoint(List<Map<Integer, Boolean>> board, int x, int y) {
        int pointValue = board.get(y).keySet();
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
