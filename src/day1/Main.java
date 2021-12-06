package day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner s = new Scanner(new File("src/day1/input.txt"));

        //part one
//        int total = 0;
//        int lastData = s.nextInt();
//        while (s.hasNextInt()){
//            int thisData = s.nextInt();
//            if(thisData > lastData) total++;
//            lastData = thisData;
//        }
//
//        System.out.println(total);

        //part two
        int totalIncreases = 0;
        int lastTotal = 0;
        int[] dataSet = new int[]{-1, s.nextInt(), s.nextInt()};

        while(s.hasNextInt()) {
            int thisTotal = 0;

            dataSet[0] = dataSet[1];
            dataSet[1] = dataSet[2];
            dataSet[2] = s.nextInt();

            for(int i : dataSet) thisTotal += i;

            if(lastTotal != 0 && thisTotal > lastTotal) totalIncreases++;

            lastTotal = thisTotal;
        }
        System.out.println(totalIncreases);
    }
}
