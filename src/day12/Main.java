package day12;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        //oh boy it's recursion time

        //parse input
        File f = new File("src/day12/test_input.txt");
        List<List<String>> allCaves = Files.readAllLines(f.toPath()).stream().map(l -> 
                Arrays.stream(l.split("-")).collect(Collectors.toList())).collect(Collectors.toList());

        PathFinder pathFinderOne = new PathFinder(allCaves);
        pathFinderOne.navigateCavePartOne();
//        pathFinderOne.printValidPaths();
//        System.out.println();
        System.out.println(pathFinderOne.getPathNum() + " total paths found by pathfinder one.");

        PathFinder pathFinderTwo = new PathFinder(allCaves);
        pathFinderTwo.navigateCavePartTwo();
        pathFinderTwo.printValidPaths();
        System.out.println();
        System.out.println(pathFinderTwo.getPathNum() + " total paths found by pathfinder two.");

    }
}
