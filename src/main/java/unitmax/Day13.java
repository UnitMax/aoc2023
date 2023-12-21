package unitmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day13 {

    private static long mirror(String[] input, long exclude) {
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
                    if (atleastOnce && (start + 1) != exclude) {
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

    private static long mirrorVert(String[] input, long exclude) {
        // basically invert this
        List<String> newInput = new ArrayList<>();
        for (int i = 0; i < input[0].length(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < input.length; j++) {
                sb.append(input[j].charAt(i));
            }
            newInput.add(sb.toString());
        }

        return mirror(newInput.toArray(new String[newInput.size()]), exclude);
    }

    private static void printMaze(List<String> maze) {
        for (var s : maze) {
            System.out.println(s);
        }
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
            var horiz = mirror(mazeArray, -0xDEADBEEF);
            var vert = mirrorVert(mazeArray, -0xDEADBEEF);
            ctr1 += (100 * horiz);
            ctr1 += vert;

            boolean found = false;
            complete_break: for (int i = 0; i < maze.size(); i++) {
                var s = maze.get(i);
                for (int j = 0; j < s.length(); j++) {
                    StringBuilder newS = new StringBuilder(s);
                    var oldC = s.charAt(j);
                    newS.setCharAt(j, oldC == '.' ? '#' : '.');
                    maze.set(i, newS.toString());

                    var mazeArray2 = maze.toArray(new String[maze.size()]);
                    var horiz2 = mirror(mazeArray2, horiz);
                    var vert2 = mirrorVert(mazeArray2, vert);
                    if (horiz2 != 0 || vert2 != 0) {
                        ctr2 += (100 * horiz2);
                        ctr2 += vert2;
                        found = true;
                        break complete_break;
                    }

                    newS.setCharAt(j, oldC);
                    maze.set(i, newS.toString());
                }
            }
            if (!found) {
                ctr2 += (100 * horiz);
                ctr2 += vert;
            }
        }

        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
