package unitmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day12 {

    private static Map<String, Long> resultMap = new HashMap<>();

    private static long countCombinationsCached(String springs, Queue<Long> myGroups) {
        if (resultMap.containsKey(springs + myGroups.toString())) {
            return resultMap.get(springs + myGroups.toString());
        }
        var result = countCombinations(springs, myGroups);
        resultMap.putIfAbsent(springs + myGroups.toString(), result);
        return result;
    }

    private static Pattern workingOrUnknownSprings = Pattern.compile("[\\?\\.]*");

    private static long countCombinations(String input, Queue<Long> givenCountQueue) {
        Queue<Long> countQueue = new LinkedList<>(givenCountQueue); // deep copy
        if (countQueue.size() == 0) {
            return 0;
        }
        long currentCount = countQueue.peek();
        if (currentCount > input.length()) {
            return 0;
        }
        long nrOfCombinations = 0;
        for (int i = 0; i <= input.length() - (int) currentCount; i++) {
            if (i > 0 && input.charAt(i - 1) == '#') {
                // previous spring is broken => not possible because broken group must be contiguous
                break;
            }
            // [currentSplicedGrouping][restOfString] = input
            String currentSplicedGrouping = input.substring(i, i + (int) currentCount);
            if (currentSplicedGrouping.contains(".")) {
                // Cannot be a contiguous group of broken springs => try next combination
                continue;
            }
            // Beyond this point, CURRENT springs only contains either # or ?, i.e. the current grouping is valid
            String restOfString = input.substring(i + (int) currentCount);
            if (workingOrUnknownSprings.matcher(restOfString).matches() && countQueue.size() == 1) {
                // Queue size = 1 => This is the last possible grouping => Try it for the rest of the string
                nrOfCombinations++;
            } else if (restOfString.startsWith(".") || restOfString.startsWith("?")) {
                // Not the last entry in the queue => Start a new branch to search for possibilities with this grouping already fulfilled
                // Requirement: we encountered a "border" (either . or ? acting as .)
                // -> Continue with next part of the string EXCLUDING the border (at this point we can have new groupings again)
                // -> "Consume" one group/count
                Queue<Long> newCountQueue = new LinkedList<>(countQueue);
                newCountQueue.poll();
                nrOfCombinations += countCombinationsCached(restOfString.substring(1), newCountQueue);
            }
        }
        return nrOfCombinations;
    }

    private static String unfold(String input) {
        var splitInput = input.split(" ");
        var springs = splitInput[0].strip();
        var countsString = splitInput[1].strip();

        List<String> springsList = new ArrayList<>();
        List<String> countList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            springsList.add(springs);
            countList.add(countsString);
        }

        return String.join("?", springsList) + " " + String.join(",", countList);
    }

    private static long combinations(String input) {
        var splitInput = input.split(" ");
        var springs = splitInput[0].strip();
        var countsString = splitInput[1].strip();

        var countList = Arrays.stream(countsString.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
        Queue<Long> countQueue = new LinkedList<>(countList);
        return countCombinationsCached(springs, countQueue);
    }

    public static ImmutablePair<Long, Long> springRecords(String[] input) {
        long ctr1 = 0;
        long ctr2 = 0;
        for (var combination : input) {
            ctr1 += combinations(combination);
            ctr2 += combinations(unfold(combination));
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
