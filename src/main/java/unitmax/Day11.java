package unitmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day11 {

    static class Galaxy {
        public int x;
        public int y;

        public Galaxy(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Range {
        public int start;
        public int len;

        public Range(int start, int len) {
            this.start = start;
            this.len = len;
        }
    }

    private static ImmutablePair<String[], List<Range>> expandGalaxy(String[] input, int factor) {
        Map<Integer, Boolean> emptyRows = new HashMap<>();
        Map<Integer, Boolean> emptyCols = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            emptyRows.put(i, true);
        }
        for (int i = 0; i < input[0].length(); i++) {
            emptyCols.put(i, true);
        }
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                var c = input[i].charAt(j);
                if (c == '#') {
                    emptyRows.put(i, false);
                    emptyCols.put(j, false);
                }
            }
        }
        long nrOfEmptyCols = emptyCols.values().stream().filter(v -> v == true).count();

        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input));

        int rowOffset = 0;
        var emptyRowString = StringUtils.repeat(".", input[0].length() + (int) (nrOfEmptyCols * factor));
        List<Range> addedEmptyRows = new ArrayList<>();
        for (var er : emptyRows.entrySet()) {
            if (er.getValue()) {
                addedEmptyRows.add(new Range(er.getKey() + rowOffset, factor));
                for (int i = 0; i < factor; i++) {
                    var row = er.getKey() + rowOffset;
                    inputList.add(row, emptyRowString);
                    rowOffset++;
                }
            }
        }

        var listOfEmptyCols = emptyCols.entrySet().stream().filter(entry -> entry.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());
        Collections.sort(listOfEmptyCols);
        int colOffset = 0;
        var factoredString = StringUtils.repeat(".", factor);
        for (var ec : listOfEmptyCols) {
            for (int i = 0; i < inputList.size(); i++) {
                final int myI = i;
                if (addedEmptyRows.stream().filter(range -> range.start == myI).count() == 1) {
                    i += addedEmptyRows.get(0).len; // they're all the same so...
                    i--; // still have the i++ in the loop so...
                    continue;
                }
                var sb = new StringBuilder(inputList.get(i));
                sb.insert(ec + colOffset, factoredString);
                inputList.set(i, sb.toString());
            }
            colOffset += factor;
        }

        return new ImmutablePair<String[], List<Range>>((String[]) inputList.toArray(new String[inputList.size()]),
                addedEmptyRows);
    }

    private static int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static ImmutablePair<Long, Long> shortestPaths(String[] input) {
        var expandedInputPair = expandGalaxy(input, 1);
        var expandedInputMillionPair = expandGalaxy(input, 1000000 - 1);
        var inputList = Arrays.asList(expandedInputPair, expandedInputMillionPair);
        var result = inputList.stream().map(il -> {
            var galaxies = getGalaxies(il.getLeft(), il.getRight());
            List<ImmutablePair<Galaxy, Galaxy>> galaxyPairs = new ArrayList<>();
            long ctr = 0;
            for (int i = 0; i < galaxies.size(); i++) {
                for (int j = i + 1; j < galaxies.size(); j++) {
                    var g1 = galaxies.get(i);
                    var g2 = galaxies.get(j);
                    galaxyPairs.add(new ImmutablePair<Galaxy, Galaxy>(galaxies.get(i), galaxies.get(j)));
                    var mhd = manhattanDistance(g1.x, g1.y, g2.x, g2.y);
                    ctr += mhd;
                }
            }
            return ctr;
        }).collect(Collectors.toList());
        return new ImmutablePair<Long, Long>((long) result.get(0), (long) result.get(1));
    }

    private static void printGalaxy(String[] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                System.out.print(input[i].charAt(j));
            }
            System.out.println();
        }
    }

    private static List<Galaxy> getGalaxies(String[] input, List<Range> skipRows) {
        List<Galaxy> galaxies = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            final int myI = i;
            if (skipRows.stream().filter(range -> range.start == myI).count() == 1) {
                i += skipRows.get(0).len; // they're all the same so...
                i--; // still have the i++ in the loop so...
                continue;
            }
            for (int j = 0; j < input[0].length(); j++) {
                if (input[i].charAt(j) == '#') {
                    galaxies.add(new Galaxy(j, i));
                }
            }
        }
        return galaxies;
    }

}
