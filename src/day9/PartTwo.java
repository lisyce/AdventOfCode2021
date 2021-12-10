package day9;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PartTwo {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day9/main_input.txt");
        Integer[][] board = Files.readAllLines(f.toPath()).stream().map(x ->
                Arrays.stream(x.split("")).map(Integer::parseInt).toArray(Integer[]::new)
        ).toArray(Integer[][]::new);

        //will store basin sizes
        List<Integer> basins = new LinkedList<>();

        for(int y=0; y<board.length; y++) {
            for(int x=0; x<board[y].length; x++) {
                if(isLowPoint(board, x, y) && board[y][x] != -1) {
                    //next generation of points to expand outwards
                    List<Integer[]> nextGen = new LinkedList<>();
                    nextGen.add(new Integer[]{x, y});

                    //try to fill in basin until you can't anymore
                    int basinSize = 0;
                    //fill in current location
                    board[y][x] = -1;
                    basinSize++;

                    while(true) {
                        int couldntFill = 0;
                        //none of the generation could fill in adjacent titles, the basin is full
                        if(couldntFill == nextGen.size()) {
                            //add the basin size to the list
                            basins.add(basinSize);
                            break;
                        }

                        //loop through the generations
                        List<Integer[]> addToNextGen = new LinkedList<>();
                        for(Integer[] coord : nextGen) {
                            boolean thisCoordFilledSomething = false;

                            //try to add to the top
                            if(coord[1] != 0 && board[coord[1]-1][coord[0]] != -1 && board[coord[1]-1][coord[0]] != 9) {
                                int newY = coord[1] - 1;
                                Integer[] newLocation = new Integer[]{coord[0], newY};
                                addToNextGen.add(newLocation);
                                thisCoordFilledSomething = true;
                                board[newLocation[1]][newLocation[0]] = -1;
                                basinSize++;
                            }

                            //try to add to the left
                            if(coord[0] != 0 && board[coord[1]][coord[0]-1] != -1 && board[coord[1]][coord[0]-1] != 9) {
                                int newX = coord[0] - 1;
                                Integer[] newLocation = new Integer[]{newX, coord[1]};
                                addToNextGen.add(newLocation);
                                thisCoordFilledSomething = true;
                                board[newLocation[1]][newLocation[0]] = -1;
                                basinSize++;
                            }

                            //try to add to the bottom
                            if(coord[1] != board.length-1 && board[coord[1]+1][coord[0]] != -1 && board[coord[1]+1][coord[0]] != 9) {
                                int newY = coord[1] + 1;
                                Integer[] newLocation = new Integer[]{coord[0], newY};
                                addToNextGen.add(newLocation);
                                thisCoordFilledSomething = true;
                                board[newLocation[1]][newLocation[0]] = -1;
                                basinSize++;
                            }

                            //try to add to the right
                            if(coord[0] != board[0].length-1 && board[coord[1]][coord[0]+1] != -1 && board[coord[1]][coord[0]+1] != 9) {
                                int newX = coord[0] + 1;
                                Integer[] newLocation = new Integer[]{newX, coord[1]};
                                addToNextGen.add(newLocation);
                                thisCoordFilledSomething = true;
                                board[newLocation[1]][newLocation[0]] = -1;
                                basinSize++;
                            }

                            //if this coord couldn't fill anything around it, add it to the counter
                            if(!thisCoordFilledSomething) couldntFill++;

                        }

                        //clear the nextGen list and point it to the real next gen
                        nextGen.clear();
                        nextGen.addAll(addToNextGen);
                        addToNextGen.clear();

                    }

                }
            }
        }

        //find 3 largest basins
        int finalAnswer = 1;
        for(int i=0; i<3; i++) {
            int max = basins.stream().mapToInt(x -> x).summaryStatistics().getMax();
            basins.remove(Integer.valueOf(max));
            finalAnswer *= max;
        }
        System.out.println(finalAnswer);

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
