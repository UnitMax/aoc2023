package unitmax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day14 {

    /**
     * 0 => .
     * 1 => #
     * 2 => O
     */
    // ##..O.O.OO
    // ##...OO.OO
    // ##...O.OOO
    // ##....OOOO
    private static List<Byte> shakeUpSequence(List<Byte> input) {
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
        List<List<Byte>> sequences = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i].strip();
        }
        for (int j = 0; j < input[0].length(); j++) {
            List<Byte> sequence = new ArrayList<>();
            for (int i = input.length - 1; i >= 0; i--) {
                var c = input[i].charAt(j);
                sequence.add((byte) (c == '.' ? 0 : (c == '#' ? 1 : 2)));
            }
            sequences.add(sequence);
        }

        for (var s : sequences) {
            shakeUpSequence(s);
            int ctr = 1;
            for (var b : s) {
                if (b == 2) {
                    ctr1 += ctr;
                }
                ctr++;
            }
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
