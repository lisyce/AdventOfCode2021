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
        File f = new File("src/day8/one_line_input.txt");
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
        s.close();

        for(List<String> key : myNotes.keySet()) {
            myNotes.replace(key ,myNotes.get(key).stream().filter(x -> !x.isEmpty()).collect(Collectors.toList()));

        }

//        //count 1, 4, 7, and 8 (part 1)
//        int count = 0;
//        for(List<String> key : myNotes.keySet()) {
//            count += myNotes.get(key).stream().filter(x -> x.length() ==  2|| x.length() == 3 || x.length() == 4 || x.length() == 7).count();
//        }
//        System.out.println(count);

        //decode

        for(List<String> key : myNotes.keySet()) {
            LinkedHashMap<String, String> decoder = new LinkedHashMap<>();
            decoder.put("a", "");
            decoder.put("b", "");
            decoder.put("c", "");
            decoder.put("d", "");
            decoder.put("e", "");
            decoder.put("f", "");
            decoder.put("g", "");

            //1. figure out which segments correspond to c and f (number 1)
            List<String> cf = findSegmentByLength(key, 2);
            //2. figure out which segments correspond to c, f, and a (number 7)
            List<String> cfa = findSegmentByLength(key, 3);
            //which is the unique char? that corresponds to "a"
            for(String str : cfa) {
                if(!cf.contains(str)) decoder.replace("a", str);
            }

            //3. which chunk corresponds to 4? (4 segments). the 2 new characters correspond to b and d but idk which
            List<String> bcdf = findSegmentByLength(key, 4);
            List<String> bd = new LinkedList<>();
            for(String str : bcdf) {
                if(!cf.contains(str)) bd.add(str);
            }

            //4. find one with six segments and all 5 of the previous jumbled values. remaining unknown is g.
            for(String str : key) {
                if (str.length() == 6 && str.contains(decoder.get("a")) && strContainsAllLetters(bcdf, str)) {
                    //string has all the right characters, find the new one (g)
                    List<String> abcdf = new LinkedList<>(bcdf);
                    abcdf.add(decoder.get("a"));
                    decoder.replace("g", findUniqueLetter(abcdf, str));
                }

            }

        }

    }

    public static String findUniqueLetter(List<String> nonUniques, String searchString) {
        for(String nonUnique : nonUniques) {
            if(searchString.contains(nonUnique)) searchString = searchString.replace(nonUnique, "");
        }
        return searchString;
    }

    public static boolean strContainsAllLetters(List<String> containedLetters, String searchString) {
        for(String str : containedLetters) if(!searchString.contains(str)) return false;
        return true;
    }

    public static List<String> findSegmentByLength(List<String> searchList, int len) {
        for(String str : searchList) if(str.length() == len) return Arrays.stream(str.split("")).collect(Collectors.toList());
        return new LinkedList<>();
    }

}
