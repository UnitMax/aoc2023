package unitmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

    public static long extrapolate(List<Long> numbers) {
        boolean allZeroes = false;
        List<List<Long>> loloDiffs = new ArrayList<>();
        List<Long> orig = numbers;
        while (!allZeroes) {
            List<Long> diffs = new ArrayList<>();
            numbers.stream().reduce((long) -0x0B00B135, (subtotal, element) -> {
                if (subtotal != -0x0B00B135) {
                    // ensure list gets reduced by one
                    diffs.add(element - subtotal);
                }
                return element;
            });
            loloDiffs.add(diffs);
            if (diffs.stream().allMatch(d -> d == 0)) {
                allZeroes = true;
            }
            numbers = diffs;
        }
        Collections.reverse(loloDiffs);
        loloDiffs.get(0).add((long) 0);
        for (int i = 0; i < (loloDiffs.size() - 1); i++) {
            var current = loloDiffs.get(i);
            var next = loloDiffs.get(i + 1);
            long newVal = next.get(next.size() - 1) + current.get(current.size() - 1);
            next.add(newVal);
        }
        return orig.get(orig.size() - 1) + loloDiffs.get(loloDiffs.size() - 1).get(loloDiffs.get(loloDiffs.size() - 1).size() - 1);
    }

    public static long subOfExtrapolatedValues(String[] input) {
        long ctr = 0;
        for (String s : input) {
            ctr += extrapolate(Arrays.stream(s.split("\s")).map(String::strip).map(Long::parseLong).collect(Collectors.toList()));
        }
        return ctr;
    }
}
