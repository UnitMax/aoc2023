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

    private static String[] expandGalaxy(String[] input) {
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
        long nrOfEmptyRows = emptyRows.values().stream().filter(v -> v == true).count();
        System.out.println("Nr of empty rows " + nrOfEmptyRows);
        System.out.println("Nr of empty cols " + nrOfEmptyCols);

        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input));

        int rowOffset = 0;
        for (var er : emptyRows.entrySet()) {
            if (er.getValue()) {
                inputList.add(er.getKey() + rowOffset, StringUtils.repeat(".", input[0].length()));
                rowOffset++;
            }
        }

        var listOfEmptyCols = emptyCols.entrySet().stream().filter(entry -> entry.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());
        Collections.sort(listOfEmptyCols);
        int colOffset = 0;
        for (var ec : listOfEmptyCols) {
            for (int i = 0; i < inputList.size(); i++) {
                inputList.set(
                        i, (new StringBuilder(inputList.get(i))).insert(ec + colOffset, '.').toString());
            }
            colOffset++;
        }

        return (String[]) inputList.toArray(new String[inputList.size()]);
    }

    public static ImmutablePair<Long, Long> shortestPaths(String[] input) {
        printGalaxy(input);
        var expandedInput = expandGalaxy(input);
        System.out.println("----");
        printGalaxy(expandedInput);
        return new ImmutablePair<Long, Long>((long) 0, (long) 0);
    }

    private static void printGalaxy(String[] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                System.out.print(input[i].charAt(j));
            }
            System.out.println();
        }
    }

}
