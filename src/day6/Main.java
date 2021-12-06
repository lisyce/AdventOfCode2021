package day6;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Main {

    //store key/value pairs: timer/number of fish with that timer
    public static void main(String[] args) throws IOException {
        Scanner s  = new Scanner(new File("src/day6/input.txt"));
        int[] initialFish = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();

        //convert to a map
        Map<Integer, BigInteger> fishByTimer = new LinkedHashMap<>();
        for(int i=0; i<9; i++) {
            fishByTimer.put(i, BigInteger.ZERO);

            //how many fish with a timer of i?
            long finalI = i;
            fishByTimer.replace(i, new BigInteger(String.valueOf(Arrays.stream(initialFish).filter(x -> x == finalI).count())));
        }

        //simulate cycles
        for(int i=0; i<256; i++) {
            Map<Integer, BigInteger> thisCyclesInitialFish = new LinkedHashMap<>(fishByTimer);
            BigInteger newFish = BigInteger.ZERO;
            for(Integer key : thisCyclesInitialFish.keySet()) {
                if(key == 0) {
                    newFish = newFish.add(thisCyclesInitialFish.get(0));
                    fishByTimer.replace(8, thisCyclesInitialFish.get(key));
                    fishByTimer.replace(key, BigInteger.ZERO);
                } else {
                    fishByTimer.replace(key-1, thisCyclesInitialFish.get(key));
                }
            }
            BigInteger totalSixFish = fishByTimer.get(6).add(newFish);
            fishByTimer.replace(6, totalSixFish);

        }

        BigInteger total = BigInteger.ZERO;
        for(Integer key : fishByTimer.keySet()) total = total.add(fishByTimer.get(key));
        System.out.println(total);

    }
}
