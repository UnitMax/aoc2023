package unitmax;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day15 {
    private static long hashString(String input) {
        long result = 0;
        for (int i = 0; i < input.length(); i++) {
            var c = input.charAt(i);
            var asciiCode = (int) c;
            result += asciiCode;
            result *= 17;
            result %= 256;
        }
        return result;
    }

    public static ImmutablePair<Long, Long> hashResult(String[] input) {
        long ctr1 = 0;
        long ctr2 = 0;
        for (var s : input) {
            var splitResult = s.split(",");
            for (var sr : splitResult) {
                ctr1 += hashString(sr);
            }
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }
}
