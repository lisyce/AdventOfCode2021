package day10;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static Map<Character, Character> matchingBrackets = new LinkedHashMap<>(Map.of('{', '}', '(', ')', '[', ']', '<', '>'));

    public static void main(String[] args) throws IOException {
        /*
        as you traverse line, find opening char then keep going if char is opening, advance to look for closing.
        if closing, find the substring that is the complete chunk by traversing back left until find opening char. delete that substring
        (prob by index).

        as traverse, add each opening symbol to a new LinkedList. if it's a closing symbol, find farthest right opening symbol in linkedlist
        if that opening symbol doesn't exist, line is corrupted. stop traversing

        if you make it to end of the line, it's incomplete but not corrupted
         */

        List<String> lines = Files.readAllLines(new File("src/day10/test_input.txt").toPath());
        List<Character> corruptedChars = new LinkedList<>();

        //part 1
        for(String line : lines) {
            Character c = lineIsCorrupted(line);
            if(!Objects.equals(c, null)) corruptedChars.add(c);
        }

        int corruptionScore = corruptedChars.stream().mapToInt(c ->
            c == ')' ? 3 : c == ']' ? 57 : c == '}' ? 1197 : c == '>' ? 25137 : 0
        ).sum();
        System.out.println("Corruption Score: " + corruptionScore + "\n");

        //part 2
        LinkedList<String> uncorruptedLines = lines.stream().filter(l -> !Objects.equals(null, lineIsCorrupted(l)))
                .collect(Collectors.toCollection(LinkedList::new));
        uncorruptedLines.forEach(System.out::println);

    }

    /**
     * checks to see if a line is corrupted
     * @param line line to check
     * @return corrupted symbol, null if line is not corrupted (but potentially incomplete)
     */
    public static Character lineIsCorrupted(String line) {
        List<Character> traversingChars = new LinkedList<>();
        for(char c : line.toCharArray()) {
            //it's an opening bracket
            if(matchingBrackets.containsKey(c)) traversingChars.add(c);
            else { //it's a closing char
                Character matchingOpeningChar = matchingBrackets.entrySet().stream().filter(x -> x.getValue().equals(c)).map(Map.Entry::getKey).findFirst().get();
                if(traversingChars.get(traversingChars.size() - 1)  != matchingOpeningChar) return c;
                traversingChars.remove(traversingChars.size()-1);
            }
        }

        return null; //return null if the line is fine
    }
}
