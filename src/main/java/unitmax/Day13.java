package unitmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day13 {

    private static long mirror(String[] input) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        List<Integer> hashList = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            hashMap.putIfAbsent(i, input[i].hashCode());
            hashList.add(input[i].hashCode());
        }
        for (int start = 0; start < hashList.size(); start++) {
            boolean atleastOnce = false;
            for (int i = 0; i < hashList.size(); i++) {
                var idx1 = start + i + 1;
                var idx2 = start - i;
                if (idx1 >= hashList.size() || idx2 < 0) {
                    if (atleastOnce) {
                        return start + 1;
                    } else {
                        break;
                    }
                }
                int hc1 = hashList.get(idx1);
                int hc2 = hashList.get(idx2);
                if (hc1 != hc2) {
                    break;
                }
                atleastOnce = true;
            }
        }
        return 0;
    }

    private static long getMirrorVertically(String[] input) {
        // basically invert this
        List<String> newInput = new ArrayList<>();
        for (int i = 0; i < input[0].length(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < input.length; j++) {
                sb.append(input[j].charAt(i));
            }
            newInput.add(sb.toString());
        }

        return mirror(newInput.toArray(new String[newInput.size()]));
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
                currentMaze.add(s.strip());
            }
        }

        for (var maze : mazes) {
            var mazeArray = maze.toArray(new String[maze.size()]);
            var horiz = (100 * mirror(mazeArray));
            var vert = getMirrorVertically(mazeArray);
            ctr1 += horiz;
            ctr1 += vert;
        }

        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
