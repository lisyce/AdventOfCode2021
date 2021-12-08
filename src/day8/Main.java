package day8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/*
 * 0: a,b,c,_,e,f,g
 * 1: _,_,c,_,_,f,_
 * 2: a,_,c,d,e,_,g
 * 3: a,_,c,d,_,f,g
 * 4: _,b,c,d,_,f,_
 * 5: a,b,_,d,_,f,g
 * 6: a,b,_,d,e,f,g
 * 7: a,_,c,_,_,f,_
 * 8: a,b,c,d,e,f,g
 * 9: a,b,c,d,_,f,g
 */

//left side always has 10, right side always has 4

public class Main {

    public static void main(String[] args) throws IOException {
        File f = new File("src/day8/input.txt");
        List<String> lines = Files.readAllLines(f.toPath());
        Map<List<String>, List<String>> myNotes = new LinkedHashMap<>();

        //TODO make parsing way cleaner
        Scanner s = new Scanner(f);
        while(s.hasNextLine()) {
            String[] twoHalves = s.nextLine().split("\\|");
            List<String> inputs = Arrays.stream(twoHalves[0].split(" ")).collect(Collectors.toList());
            List<String> outputs = Arrays.stream(twoHalves[1].split(" ")).collect(Collectors.toList());
            myNotes.put(inputs, outputs);
        }

        for(List<String> key : myNotes.keySet()) {
            myNotes.replace(key ,myNotes.get(key).stream().filter(x -> !x.isEmpty()).collect(Collectors.toList()));
        }

//        //count 1, 4, 7, and 8 (part 1)
//        int count = 0;
//        for(List<String> key : myNotes.keySet()) {
//            count += myNotes.get(key).stream().filter(x -> x.length() ==  2|| x.length() == 3 || x.length() == 4 || x.length() == 7).count();
//        }
//        System.out.println(count);

    }
}
