package day18;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("src/day18/test_input.txt");
        String finalSum = "";
        for(String line : Files.readAllLines(f.toPath())) {
            finalSum = addSnailfishNumbers(finalSum, line);
        }

        String templatedSum = templateSnailfishNumber(finalSum);
        ArrayList<Integer> realNums = snailfishNumberToRealNums(finalSum);
        System.out.println(explodeSnailfishNumber(realNums, templatedSum));
        //add, then reduce, then add, then reduce
    }

    public static String addSnailfishNumbers(String num1, String num2) {
        if(num1.equals("")) return num2;
        return "[" + num1 + "," + num2 + "]";
    }

    public static String reduceSnailfishNumber(String num) {

        return null;
    }

    public static String templateSnailfishNumber(String num) {
        StringBuilder templatedNum = new StringBuilder();
        for(int i=0; i<num.length(); i++) {
            if(i == 0) {
                templatedNum.append(num, i, i+1);
                continue;
            }
            switch(num.charAt(i)) {
                case '[':
                case ']':
                case ',':
                    templatedNum.append(num, i, i+1);
                    break;
                default:
                    char prevChar = templatedNum.charAt(templatedNum.length()-1);
                    if(prevChar != 'x') templatedNum.append('x');
                    break;
            }

        }
        return templatedNum.toString();
    }

    public static String explodeSnailfishNumber(ArrayList<Integer> realNums, String templatedNum) {
        //if we can't explode, just return the original input
        int openBracketCount = 0;
        int xCount = 0;

        for(int i=0; i<templatedNum.length()-4; i++) {
            switch (templatedNum.charAt(i)) {
                case '[':
                    openBracketCount++;
                    break;
                case ']':
                    openBracketCount--;
                    break;
                case 'x':
                    xCount++;
            }

            String potentialPair = templatedNum.substring(i, i+5);
            if(potentialPair.charAt(0) == '[' && potentialPair.charAt(4) == ']') {
                //this is a pair. is it far enough inset?
                if(openBracketCount >= 5) {
                    //this pair can explode
                    StringBuilder postExplosion = new StringBuilder();

                    //store the left and right values of the pair
                    int leftNum = realNums.get(xCount);
                    int rightNum = realNums.get(xCount+1);

                    //go left until you find a regular num
                    boolean foundRegNum = false;
                    for(int j=i-1; j>=0; j--) {
                        if(templatedNum.charAt(j) == 'x') {
                            realNums.set(xCount, realNums.get(xCount) + leftNum);
                            String leftChunk = buildSnailfishNumFromRealNums(realNums, templatedNum.substring(0, j));
                            postExplosion.append(leftChunk);
                            foundRegNum = true;
                            break;
                        }
                    }
                    if(!foundRegNum) postExplosion.append(templatedNum, 0, i);

                    //replace original pair with 0
                    postExplosion.append("0");
                    //go right until you find regular num
                    foundRegNum = false;
                    for(int j=i+5; j<templatedNum.length(); j++) {
                        if(templatedNum.charAt(j) == 'x') {
                            realNums.remove(xCount+1);
                            realNums.remove(xCount);

                            realNums.set(xCount, realNums.get(xCount) + rightNum);
                            String rightChunk = buildSnailfishNumFromRealNums(realNums, templatedNum.substring(j-1));
                            postExplosion.append(rightChunk);
                            foundRegNum = true;
                            break;
                        }
                    }
                    if(!foundRegNum) postExplosion.append(templatedNum, i, templatedNum.length());

                    return postExplosion.toString();
                }
            }
        }

        return null;
    }

    //assumes numbers stay in 2 digits...
    public static ArrayList<Integer> snailfishNumberToRealNums(String num) {
        ArrayList<Integer> realNums = new ArrayList<>();
        boolean skipNextLoop = false;
        for(int i=0; i<num.length(); i++) {
            if(skipNextLoop) {
                skipNextLoop = false;
                continue;
            }
            char c = num.charAt(i);
            if(charIsRealNum(c)) {
                if(i<num.length()-1 && charIsRealNum(num.charAt(i+1))) {
                    realNums.add(Integer.parseInt(num.substring(i, i+2)));
                    skipNextLoop = true;
                }
                else realNums.add(Integer.parseInt(String.valueOf(c)));
            }
        }
        return realNums;
    }

    public static String buildSnailfishNumFromRealNums(ArrayList<Integer> realNums, String templatedNum) {
        StringBuilder snailfishNum = new StringBuilder();
        int xCount = 0;
        for(int i=0; i<templatedNum.length(); i++) {
            if(charIsRealNum(templatedNum.charAt(i))) {
                snailfishNum.append(realNums.get(xCount));
                xCount++;
            } else snailfishNum.append(templatedNum.charAt(i));
        }
        return snailfishNum.toString();
    }

    public static boolean charIsRealNum(char c) {
        return c != '[' && c != ']' && c != ',';
    }

}
