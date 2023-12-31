package unitmax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day14 {

    private static Map<Integer, Integer> gridCache = new HashMap<>();

    /**
     * 0 => .
     * 1 => #
     * 2 => O
     */
    private static List<Byte> shakeUpSequence(ArrayList<Byte> input) {
        int countMoving = 0;
        List<Integer> resetPositions = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            var b = input.get(i);
            if (b == 2) {
                countMoving++;
                resetPositions.add(i);
            } else if (b == 1) {
                for (var rp : resetPositions) {
                    input.set(rp, (byte) 0);
                }
                for (int j = 0; j < countMoving; j++) {
                    input.set(i - j - 1, (byte) 2);
                }
                resetPositions.clear();
                countMoving = 0;
            }
        }
        for (var rp : resetPositions) {
            input.set(rp, (byte) 0);
        }
        for (int j = 0; j < countMoving; j++) {
            input.set(input.size() - 1 - j, (byte) 2);
        }
        return input;
    }

    public static ImmutablePair<Long, Long> totalLoad(String[] input) {
        long ctr1 = 0;
        long ctr2 = 0;
        ArrayList<ArrayList<Byte>> sequences = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i].strip();
        }
        for (int i = 0; i < input.length; i++) {
            ArrayList<Byte> sequence = new ArrayList<>();
            for (int j = 0; j < input[0].length(); j++) {
                var c = input[i].charAt(j);
                sequence.add((byte) (c == '.' ? 0 : (c == '#' ? 1 : 2)));
            }
            sequences.add(sequence);
        }

        // Part 1
        for (int i = 0; i < input[0].length(); i++) {
            ArrayList<Byte> sequence = new ArrayList<>();
            for (int j = input.length - 1; j >= 0; j--) {
                sequence.add(sequences.get(j).get(i));
            }
            shakeUpSequence(sequence);
            int ctrp1 = 1;
            for (var b : sequence) {
                if (b == 2) {
                    ctr1 += ctrp1;
                }
                ctrp1++;
            }
        }

        // Part 2
        boolean cycleFound = false;
        for (int cycles = 0; cycles < 1000000000; cycles++) {

            int gridKey = sequences.hashCode();
            if (gridCache.containsKey(gridKey)) {
                if (!cycleFound) {
                    cycleFound = true;
                    var cycleAdvance = cycles - gridCache.get(gridKey);
                    while (cycles < (1000000000 - cycleAdvance)) {
                        cycles += cycleAdvance;
                    }
                    cycles--;
                    continue;
                }
            }
            gridCache.putIfAbsent(gridKey, cycles);

            for (int times = 0; times < 4; times++) {
                ArrayList<ArrayList<Byte>> newSequences = new ArrayList<>();
                for (int i = 0; i < input[0].length(); i++) {
                    ArrayList<Byte> sequence = new ArrayList<>();
                    for (int j = input.length - 1; j >= 0; j--) {
                        sequence.add(sequences.get(j).get(i));
                    }
                    shakeUpSequence(sequence);
                    newSequences.add(sequence);
                }
                sequences = newSequences;
            }
        }

        int ctr = sequences.size();
        for (var s : sequences) {
            for (var b : s) {
                if (b == 2) {
                    ctr2 += ctr;
                }
            }
            ctr--;
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
