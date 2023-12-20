package unitmax;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day13 {

    private static long mirror(String[] input) {

        return 0;
    }

    public static ImmutablePair<Long, Long> getPatterns(String[] input) {
        long ctr1 = 0;
        long ctr2 = 0;

        List<List<String>> mazes = new ArrayList<>();
        List<String> currentMaze = new ArrayList<>();
        mazes.add(currentMaze);
        for (var s : input) {
            if (s.strip().isEmpty()) {
                currentMaze = new ArrayList<>();
                mazes.add(currentMaze);
            } else {
                currentMaze.add(s);
            }
        }

        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
