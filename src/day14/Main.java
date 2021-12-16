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
        File f = new File("src/day14/test_input.txt");
        List<String> allLines = new LinkedList<>(Files.readAllLines(f.toPath()));

        String polymer = allLines.get(0);
        Map<String, String> insertionRules = allLines.stream().filter(l -> allLines.indexOf(l) > 1)
                .map(l -> l.replaceAll("\\s", "").split("->"))
                .collect(Collectors.toMap(x -> x[0], x -> x[1]));

        //new solution: write to a file
        for(int step=0; step<40; step++) {
            System.out.println(step);
            List<String> insertions = new LinkedList<>();
            for(int i=0; i<polymer.length()-1; i++) {
                String pair = polymer.substring(i, i+2);
                insertions.add(insertionRules.get(pair));
            }

            StringBuilder newPolymer = new StringBuilder();
            for(int i=0; i<polymer.length()-1; i++) {
                newPolymer.append(polymer.charAt(i));
                newPolymer.append(insertions.get(i));
            }
            newPolymer.append(polymer.charAt(polymer.length()-1));
            polymer = newPolymer.toString();

            if(step == 9 || step == 39) {
                Map<Character, BigInteger> elementCount = new HashMap<>();
                for(int i=0; i<polymer.length(); i++) {
                    char element = polymer.charAt(i);
                    if(!elementCount.containsKey(element)) elementCount.put(element, BigInteger.ONE);
                    else elementCount.replace(element, elementCount.get(element).add(BigInteger.ONE));
                }

                BigInteger mostCommonCount = elementCount.values().stream().max(BigInteger::compareTo).get();
                BigInteger leastCommonCount = elementCount.values().stream().min(BigInteger::compareTo).get();
                System.out.printf("Polymer length after step %d: %s\n", (step+1), polymer.length());
                System.out.printf("Most - least common element after step %d: %s", (step + 1), (mostCommonCount.subtract(leastCommonCount)));
                System.out.println();
            }

        }



    }
}
