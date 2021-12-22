package day16;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(new File("src/day16/test_input.txt").toPath()).get(0);
        //convert to binary
        Map<String, String> mapHexToBinary = Files.readAllLines(new File("src/day16/hex_to_binary.txt").toPath())
                .stream().map(l -> l.split(" = ")).collect(Collectors.toMap(
                        x -> x[0], x -> x[1]
                ));
        StringBuilder binaryBuilder = new StringBuilder();
        for(int i=0; i<input.length(); i++) {
            binaryBuilder.append(mapHexToBinary.get(input.substring(i, i+1)));
        }
        String binaryInput = binaryBuilder.toString();


        //part 1: add up all version numbers of packets
        List<Integer> packetVersions = new LinkedList<>();
        List<Integer> literalValues = new LinkedList<>();

        parsePacket(binaryInput, packetVersions, literalValues);
        int versionSum = packetVersions.stream().mapToInt(x -> x).sum();
        System.out.println(versionSum);
//        packetVersions.forEach(x -> System.out.print(x + ", "));
//        System.out.println();
//        literalValues.forEach(x -> System.out.print(x + ", "));

    }

    /**
     *
     * @param packet
     * @param packetVersions
     * @return length of the packet
     */
    public static int parsePacket(String packet, List<Integer> packetVersions, List<Integer> literalValues) {
        System.out.println("\nbegin new packet");
        System.out.println(packet);
        int packetVersion = Integer.parseInt(packet.substring(0, 3), 2);
        System.out.println("version: " + packetVersion);
        packetVersions.add(packetVersion);
        int packetType = Integer.parseInt(packet.substring(3, 6), 2);

        if(packetType == 4) { //literal packet, encodes a single binary #
            System.out.println("literal found");
            //each group of 4 bits is prefixed by a 1 bit, the last group is prefixed by a 0 bit
            StringBuilder literalValue = new StringBuilder();
            for(int i = 6; i <= packet.substring(6).length()+6; i+=5) {
                literalValue.append(packet, i+1, i+5);
                //if we hit the last packet, stop
                if(packet.charAt(i) == '0') break;
            }
            System.out.println("literal value: " + Integer.parseInt(literalValue.toString(), 2));
            literalValues.add(Integer.parseInt(literalValue.toString(), 2));
            return literalValue.length() + 7;
        } else { //operator packet
            System.out.println("operator found");
            int lengthTypeID = packet.charAt(6) == '0' ? 15 : 11;
            int index = 7+lengthTypeID;
            int innerPacketLength = Integer.parseInt(packet.substring(7, index), 2);
            System.out.println("packetLengthID: " + innerPacketLength);

            //parse down any inner packets
            if(lengthTypeID == 11) { //next 11 bits are # representing number of subpackets
                System.out.println("there are " + innerPacketLength + " inner packets");

                for(int i = 0; i< innerPacketLength; i++) {
                    int innerLength = parsePacket(packet.substring(index), packetVersions, literalValues);
                    index += innerLength;
                }
                return packet.length();
            } else { //next 15 bits are a number representing total length in bits of subpackets
                //save off the next 15 bits worth of packets
                System.out.println("there are " + innerPacketLength + " bits worth of inner packets");
                String innerBits = packet.substring(index, index+innerPacketLength);
                int count = 0;
                while(count < innerPacketLength-1) {
                    int innerLength = parsePacket(innerBits.substring(count), packetVersions, literalValues);
                    count += innerLength;
                }
                //multiply by num of operator packets rather than 2???
                return innerPacketLength * 2;
            }


        }
    }


}
