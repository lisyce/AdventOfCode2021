package day14;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        //store polymer as a string to save memory
        File f = new File("src/day14/main_input.txt");
        List<String> allLines = new LinkedList<>(Files.readAllLines(f.toPath()));

        String polymer = allLines.get(0);
        Map<String, String> insertionRules = allLines.stream().filter(l -> allLines.indexOf(l) > 1)
                .map(l -> l.replaceAll("\\s", "").split("->"))
                .collect(Collectors.toMap(x -> x[0], x -> x[1]));

        Map<String, BigInteger> elementCounts = new HashMap<>();
        insertionRules.values().forEach(v -> {
            if(!elementCounts.containsKey(v)) elementCounts.put(v, BigInteger.ZERO);
        });

        //Key: pair, Value: index 0 is this loop's count, index 1 is last loop's count
        Map<String, BigInteger> pairCounts = insertionRules.keySet().stream().collect(Collectors.toMap(x -> x, x -> BigInteger.ZERO));
        //add original polymer pairs to pairCounts
        for(int i=0; i<polymer.length()-1; i++) {
            //add original polymer elements to elementCounts
            elementCounts.replace(String.valueOf(polymer.charAt(i)), elementCounts.get(String.valueOf(polymer.charAt(i))).add(BigInteger.ONE));

            String pair = polymer.substring(i, i + 2);
            if (!pairCounts.containsKey(pair)) pairCounts.put(pair, BigInteger.ONE);
            else pairCounts.replace(pair, pairCounts.get(pair).add(BigInteger.ONE));
        }
        //take care of last element
        String element = String.valueOf(polymer.charAt(polymer.length() - 1));
        elementCounts.replace(element, elementCounts.get(element).add(BigInteger.ONE));



        //loop through the steps
        for(int i=0; i<40; i++) {
            //copy the pair counts over
            Map<String, BigInteger> copiedPairCounts = pairCounts.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            //loop through all the copied pairs
            for(String copiedPair : copiedPairCounts.keySet()) {
                //if we don't have any of these in the old pair counts, don't do anything else
                BigInteger oldPairCounts = copiedPairCounts.get(copiedPair);
                if(oldPairCounts.equals(BigInteger.ZERO)) continue;

                //find the two new pairs
                String insertion = insertionRules.get(copiedPair);
                String pairOne = copiedPair.charAt(0) + insertion;
                String pairTwo = insertion + copiedPair.charAt(1);

                //update element counts
                elementCounts.replace(insertion, elementCounts.get(insertion).add(oldPairCounts));

                //update the actual pairCounts
                pairCounts.replace(copiedPair, pairCounts.get(copiedPair).subtract(oldPairCounts));
                pairCounts.replace(pairOne, pairCounts.get(pairOne).add(oldPairCounts));
                pairCounts.replace(pairTwo, pairCounts.get(pairTwo).add(oldPairCounts));

            }
            //if it's step 10 or 40, print out most - least
            if(i == 9 || i == 39) {
                BigInteger mostCommonCount = elementCounts.values().stream().max(BigInteger::compareTo).get();
                BigInteger leastCommonCount = elementCounts.values().stream().min(BigInteger::compareTo).get();
                System.out.printf("Most - least common element after step %d: %s\n", (i + 1), (mostCommonCount.subtract(leastCommonCount)));
            }

        }

        //Older solution: worked for part one, but Strings don't have enough capacity for part 2 and also it'd run for like *hours* if it even had capacity to
//        for(int step=0; step<40; step++) {
//            System.out.println(step);
//            List<String> insertions = new LinkedList<>();
//            for(int i=0; i<polymer.length()-1; i++) {
//                String pair = polymer.substring(i, i+2);
//                insertions.add(insertionRules.get(pair));
//            }
//
//            StringBuilder newPolymer = new StringBuilder();
//            for(int i=0; i<polymer.length()-1; i++) {
//                newPolymer.append(polymer.charAt(i));
//                newPolymer.append(insertions.get(i));
//            }
//            newPolymer.append(polymer.charAt(polymer.length()-1));
//            polymer = newPolymer.toString();
//
//            if(step == 9 || step == 39) {
//                Map<Character, BigInteger> elementCount = new HashMap<>();
//                for(int i=0; i<polymer.length(); i++) {
//                    char element = polymer.charAt(i);
//                    if(!elementCount.containsKey(element)) elementCount.put(element, BigInteger.ONE);
//                    else elementCount.replace(element, elementCount.get(element).add(BigInteger.ONE));
//                }
//
//                BigInteger mostCommonCount = elementCount.values().stream().max(BigInteger::compareTo).get();
//                BigInteger leastCommonCount = elementCount.values().stream().min(BigInteger::compareTo).get();
//                System.out.printf("Polymer length after step %d: %s\n", (step+1), polymer.length());
//                System.out.printf("Most - least common element after step %d: %s", (step + 1), (mostCommonCount.subtract(leastCommonCount)));
//                System.out.println();
//            }
//
//        }
    }
}
