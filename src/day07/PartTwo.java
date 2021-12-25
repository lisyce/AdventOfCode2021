package day07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class PartTwo {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/day07/input.txt"));
        int[] horizontalPositions = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();

        //brute force it baby
        int max = Arrays.stream(horizontalPositions).summaryStatistics().getMax();
        int min = Arrays.stream(horizontalPositions).summaryStatistics().getMin();
        System.out.printf("Max: %d, Min: %d\n", max, min);

        int efficientPos = -1;
        long efficientPosCost = -1;
        for(int i=min; i<=max; i++) {
            //calculate the cost of this position
            int finalI = i;

            long cost = Arrays.stream(horizontalPositions).map(x -> calculateFuelCost(x - finalI)).sum();

            if(cost < efficientPosCost || efficientPosCost == -1) {
                efficientPosCost = cost;
                efficientPos = i;
            }
        }

        System.out.printf("Most efficient position: %d. Cost: %d fuel.", efficientPos, efficientPosCost);

    }

    public static int calculateFuelCost(int distance) {
        distance = Math.abs(distance);
        int costPerStep = 1;
        int totalCost = 0;
        for(int i=0; i<distance; i++) {
            totalCost += costPerStep;
            costPerStep++;
        }
        return totalCost;
    }



}
