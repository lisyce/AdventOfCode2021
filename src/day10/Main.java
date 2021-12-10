package day10;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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

        List<String> lines = Files.readAllLines(new File("src/day10/main_input.txt").toPath());
        List<Character> corruptedChars = new LinkedList<>();

        //part 1
        for(String line : lines) {
            List<Character> c = lineIsCorrupted(line, false);
            if(!Objects.equals(c, null)) corruptedChars.add(c.get(0));
        }

        int corruptionScore = corruptedChars.stream().mapToInt(c ->
            c == ')' ? 3 : c == ']' ? 57 : c == '}' ? 1197 : 25137
        ).sum();
        System.out.println("Corruption Score: " + corruptionScore + "\n");

        //part 2
        LinkedList<String> incompleteLines = lines.stream().filter(l -> Objects.equals(null, lineIsCorrupted(l, false)))
                .collect(Collectors.toCollection(LinkedList::new));

        BigInteger[] autocompleteScores = incompleteLines.stream().map(l -> {
            BigInteger totalScore = BigInteger.ZERO;
            int[] closingBracketScores = lineIsCorrupted(l, true).stream()
                    .mapToInt(c -> c == ')' ? 1 : c == ']' ? 2 : c == '}' ? 3 : 4).toArray();

            for(int i : closingBracketScores) {
                totalScore = totalScore.multiply(BigInteger.valueOf(5));
                totalScore = totalScore.add(BigInteger.valueOf(i));
            }
            return totalScore;
        }).toArray(BigInteger[]::new);


        Arrays.sort(autocompleteScores, BigInteger::compareTo);
        System.out.println(Arrays.toString(autocompleteScores));

        BigInteger middleScore = autocompleteScores[autocompleteScores.length/2];
        System.out.println("Middle Score: " + middleScore); //85929366 is too low
    }

    /**
     * checks to see if a line is corrupted
     * @param line line to check
     * @param autocompleteLine whether return the characters necessary to complete the string
     * @return if not autocomplete: corrupted symbol, null if line is not corrupted (but potentially incomplete)
     *         if autocomplete: List of chars needed to finish line
     */
    public static List<Character> lineIsCorrupted(String line, boolean autocompleteLine) {
        List<Character> traversingChars = new LinkedList<>();
        for(char c : line.toCharArray()) {
            //it's an opening bracket
            if(matchingBrackets.containsKey(c)) traversingChars.add(c);
            else { //it's a closing char
                //get the key in matchingBrackets from the value (coulda been done with a switch tho lol)
                Character matchingOpeningChar = matchingBrackets.entrySet().stream().filter(x -> x.getValue().equals(c)).map(Map.Entry::getKey).findFirst().get();
                if(traversingChars.get(traversingChars.size() - 1)  != matchingOpeningChar && !autocompleteLine) return new LinkedList<>(List.of(c));
                traversingChars.remove(traversingChars.size()-1);
            }
        }

        if(!autocompleteLine) return null; //return null if the line is fine and we aren't autocompleting

        //autocomplete the line
        LinkedList<Character> autocompletedChars = new LinkedList<>();
        traversingChars.stream().map(x -> matchingBrackets.get(x)).forEach(x -> autocompletedChars.add(0, x));
        return autocompletedChars;
    }
}