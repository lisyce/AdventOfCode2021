package day12;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PathFinder {

    private int pathNum;
    private final List<List<String>> caveSystem;
    private final List<String> validPaths = new LinkedList<>();

    public PathFinder(List<List<String>> caveSystem) {
        pathNum = 0;
        this.caveSystem = caveSystem;
    }

    public int getPathNum() {
        return pathNum;
    }

    public void printValidPaths() {
        validPaths.forEach(System.out::println);
    }

    /*
    we always know our target is the "end" and we always start at the "start"
    we need to know all the small caves we've visited so we don't get stuck in a loop if we only know the previous one
    then leave and come back 2 turns later
    we can't go back to the start and we can't leave the end
     */

    /*
    issue: we can visit cave b on one path, but a different path thinks it's been there
    solution: branch off with a new Map of small caves visited
     */

    public void navigateCavePartOne() {
        navigateCave(new LinkedHashMap<>(), "start", "", false);
    }

    public void navigateCavePartTwo() {
        navigateCave(new LinkedHashMap<>(), "start", "", true);
    }

    private void navigateCave(Map<String, Integer> previousCaves, String currentCave, String path, boolean doubleSmallCaves) {
        Map<String, Integer> smallCavesVisited = new LinkedHashMap<>(previousCaves);

        //possible next moves
        List<String> connectedCaves = new LinkedList<>();
        for(List<String> cavePair : caveSystem) {
            if(cavePair.contains(currentCave)) {
                connectedCaves.add(cavePair.indexOf(currentCave) == 0 ? cavePair.get(1) : cavePair.get(0));
            }
        }

        //does this path have a valid next cave to explore?
        boolean canExploreFurther = false;
        for(String connectedCave : connectedCaves) {
            if(!smallCavesVisited.containsKey(connectedCave)) {
                canExploreFurther = true;
                break;
            } else if(smallCavesVisited.get(connectedCave) == 1 && doubleSmallCaves) {
                canExploreFurther = true;
                break;
            }
        }

        //base case: path is complete
        if(currentCave.equals("end")) {
            path += "end";
            this.pathNum++;
            validPaths.add(path);
        } else if (canExploreFurther) { //recursive case: we have more of the path to explore

            if(currentCave.equals(currentCave.toLowerCase())) { //we are currently in a small cave
                if(!smallCavesVisited.containsKey(currentCave)) smallCavesVisited.put(currentCave, 1);
                else if(smallCavesVisited.get(currentCave) == 1 && doubleSmallCaves) smallCavesVisited.replace(currentCave, 2);
            }
            path += currentCave + ",";

            //we can only visit one small cave twice!!!!
            boolean visitedSmallCaveTwice = false;
            for(String key : smallCavesVisited.keySet()) {
                if(smallCavesVisited.get(key) == 2) {
                    visitedSmallCaveTwice = true;
                    break;
                }
            }

            //explore valid, connected caves
            for(String s : connectedCaves) {
                if(!smallCavesVisited.containsKey(s)) {
                    navigateCave(smallCavesVisited, s, path, doubleSmallCaves);
                } else if(smallCavesVisited.get(s) == 1 && doubleSmallCaves && !s.equals("start")) {
                    if(!visitedSmallCaveTwice) navigateCave(smallCavesVisited, s, path, true);
                }
            }
        }
    }
}
