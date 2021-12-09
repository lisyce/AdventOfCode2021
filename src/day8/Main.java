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
        File f = new File("src/day8/main_input.txt");
        List<String> lines = Files.readAllLines(f.toPath());

        //cleaner parsing with more streams (same thing as below but nicer looking)
        Map<List<String>, List<String>> myNotes = lines.stream().map(x -> x.split("\\|")).collect(Collectors.toMap(
                x -> Arrays.stream(x[0].split(" ")).collect(Collectors.toList()),
                x -> Arrays.stream(x[1].split(" ")).collect(Collectors.toList())));

// OLD PARSING BELOW
//        Scanner s = new Scanner(f);
//        while(s.hasNextLine()) {
//            String[] twoHalves = s.nextLine().split("\\|");
//            List<String> inputs = Arrays.stream(twoHalves[0].split(" ")).collect(Collectors.toList());
//            List<String> outputs = Arrays.stream(twoHalves[1].split(" ")).collect(Collectors.toList());
//            myNotes.put(inputs, outputs);
//        }
//        s.close();

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
        long totalOutput = 0;

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
            List<String> cf = findSegmentsByLength(key, 2);
            //2. figure out which segments correspond to c, f, and a (number 7)
            List<String> cfa = findSegmentsByLength(key, 3);
            //which is the unique char? that corresponds to "a"
            for(String str : cfa) {
                if(!cf.contains(str)) decoder.replace("a", str);
            }

            //3. which chunk corresponds to 4? (4 segments). the 2 new characters correspond to b and d but idk which
            List<String> increasingChecker = findSegmentsByLength(key, 4);
            List<String> bd = new LinkedList<>();
            for(String str : increasingChecker) {
                if(!cf.contains(str)) bd.add(str);
            }

            //4. find one with six segments and all 5 of the previous jumbled values. remaining unknown is g.
            for(String str : key) {
                if (str.length() == 6 && str.contains(decoder.get("a")) && strContainsAllLetters(increasingChecker, str)) {
                    //string has all the right characters, find the new one (g)
                    increasingChecker.add(decoder.get("a"));
                    decoder.replace("g", findUnknownSegment(increasingChecker, str));
                    increasingChecker.add(decoder.get("g"));
                    break;
                }
            }

            //5. find a 7 segment one (#8) take out the c/f chunk and the b/d chunk and the a and g and e is for sure
            for(String str : key) {
                if(str.length() == 7 && strContainsAllLetters(increasingChecker, str)) {
                    decoder.replace("e", findUnknownSegment(increasingChecker, str));
                    increasingChecker.add(decoder.get("e"));
                    break;
                }
            }

            //6. find one with 6 segments and a,c,d,f,g (number 3)
            List<String> acdfg = new LinkedList<>(List.of(
                    cf.get(0),
                    cf.get(1),
                    decoder.get("g"),
                    decoder.get("a")
            ));

            for(String str : key) {
                if(str.length() == 5 && strContainsAllLetters(acdfg, str)) {
                    decoder.replace("d", findUnknownSegment(acdfg, str));
                    decoder.replace("b", bd.get(0).equals(decoder.get("d")) ? bd.get(1) : bd.get(0));
                    break;
                }
            }

            increasingChecker = increasingChecker.stream().filter(x -> !cf.contains(x)).collect(Collectors.toList());

            //7. all that's left is c and f
            //make sure increasingChecker has everything but c and f's corresponding values
            List<String> finalIncreasingChecker = increasingChecker;
            decoder.forEach((k, v) -> {
                if(!finalIncreasingChecker.contains(v)) finalIncreasingChecker.add(v);
            });


            //find one that only has one remaining unknown and uses 6 segments (num 6)
            for(String str : key) {
                if(calculateNumUnknowns(decoder, str) == 1 && str.length() == 6) {
                    decoder.replace("f", findUnknownSegment(increasingChecker, str));
                    increasingChecker.add(decoder.get("f"));
                    break;
                }
            }

            //find a number 8 and use it to fill in c
            for(String str : key) {
                if(str.length() == 7) {
                    decoder.replace("c", findUnknownSegment(increasingChecker, str));
                    break;
                }
            }

            //decode output
            int output = decodeOutput(decoder, myNotes.get(key));
            totalOutput += output;
        }
        System.out.println("TOTAL: " + totalOutput);

    }

    /**
     * Decodes the 4 output values (Or technically any number of them) but it never gets passed more than 4
     * @param decoder Decoding key for the jumbled input
     * @param outputs The four outputs on each line
     * @return Four-digit number representing the output of the line
     */
    public static int decodeOutput(Map<String, String> decoder, List<String> outputs) {
        StringBuilder finalString = new StringBuilder();
        for(String str : outputs) {
            finalString.append(decodeNumber(decoder, str));
        }
        return Integer.parseInt(finalString.toString());
    }

    /**
     * Converts a jumbled String into a String representation of a digit to be used in decodeOutput's concatenation
     * @param decoder Decoding key for the jumbled input
     * @param jumbledInput The jumbled input (idk what else to say about it)
     * @return One digit that the jumbled input represents in String form
     */
    public static String decodeNumber(Map<String, String> decoder, String jumbledInput) {
        switch(jumbledInput.length()) {
            case 2:
                return "1";
            case 3:
                return "7";
            case 4:
                return "4";
            case 5:
                if(jumbledInput.contains(decoder.get("b"))) return "5";
                else if(jumbledInput.contains(decoder.get("e"))) return "2";
                return "3";
            case 6:
                if(!jumbledInput.contains(decoder.get("d"))) return "0";
                else if(!jumbledInput.contains(decoder.get("c"))) return "6";
                return "9";
            case 7:
                return "8";
        }

        return "-1";
    }

    /**
     * Based on the decoder passed in (which may have unknown values), calculates how many unknown segments are in the
     * jumbledInput
     * @param decoder Decoding key for the jumbled input
     * @param jumbledInput Selection of segments that may have unknowns in them (in jumbled form)
     * @return The number of segments that were unable to be decoded in the jumbled input
     */
    public static int calculateNumUnknowns(Map<String, String> decoder, String jumbledInput) {
        int unknowns = jumbledInput.length();
        for(String key : decoder.keySet()) {
            if(jumbledInput.contains(decoder.get(key)) && !decoder.get(key).isEmpty()) unknowns--;
        }
        return unknowns;
    }

    /**
     * Determines which segment in the jumbled input does not have a value in the decoder
     * @param knownSegments List of segments we do not want to have returned to us (cannot just use the decoder
     *                      because of instances such as c/f where we know the two corresponding values, but not
     *                      which they correspond to)
     * @param jumbledInput Selection of segments that may have a non-decodable value in them presently
     * @return The letter of the segment that does not have a decoding key (or multiple letters if multiple are unknown)
     * I am aware that it should handle having multiple unknowns in its own self, but I handle it elsewhere and
     * do not want to change it
     */
    public static String findUnknownSegment(List<String> knownSegments, String jumbledInput) {
        for(String knownSegment : knownSegments) {
            if(jumbledInput.contains(knownSegment)) jumbledInput = jumbledInput.replace(knownSegment, "");
        }
        return jumbledInput;
    }

    /**
     * Determines whether a combination of segments contains all the letters specified
     * @param containedLetters The letters that we want to be contained in the searchString
     * @param searchString The String that may contain all the segments
     * @return Whether the searchString contains all the containedLetters
     */
    public static boolean strContainsAllLetters(List<String> containedLetters, String searchString) {
        for(String str : containedLetters) if(!searchString.contains(str)) return false;
        return true;
    }

    /**
     * Returns the first data point (e.g. jumbled input) of the specified length in the searchList split into
     * a List of its respective segments
     * @param searchList The list that may contain the segment of the specified length
     * @param len The length of the data point being searched for
     * @return A List of all the letters of the segments in the data point
     */
    public static List<String> findSegmentsByLength(List<String> searchList, int len) {
        for(String str : searchList) if(str.length() == len) return Arrays.stream(str.split("")).collect(Collectors.toList());
        return new LinkedList<>();
    }

}
