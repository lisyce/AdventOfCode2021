package day14;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

        for(int step=0; step<4; step++) {
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

        }


    }
}
