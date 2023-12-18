package unitmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day12 {

    private static boolean check(String s, List<Long> l) {
        Queue<Long> q = new LinkedList<>(l);
        long currentCount = q.poll();
        boolean countingPounds = s.charAt(0) == '#';
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            if (c == '#' && countingPounds) {
                if (currentCount == 0) {
                    System.out.println("Failed at " + i);
                    return false;
                }
                currentCount--;
                countingPounds = true;
            } else if (c == '#' && !countingPounds) {
                if (currentCount == 0) {
                    System.out.println("Failed at " + i);
                    return false;
                } else {
                    currentCount--;
                    countingPounds = true;
                }
            } else if (c == '.') {
                if (countingPounds) {
                    if (currentCount > 0) {
                        System.out.println("Failed at " + i);
                        return false;
                    }
                    if (q.isEmpty()) {
                        currentCount = 0;
                    } else {
                        currentCount = q.poll();
                    }
                }
                countingPounds = false;
            }
        }
        if (currentCount != 0 || !q.isEmpty()) {
            System.out.println("Failed at end");
        }
        return currentCount == 0 && q.isEmpty();
    }

    private static long remainingCombinations(String str, long currentCount, Queue<Long> givenCountQueue,
            boolean lastCharDotOrStart, String finalString, List<Long> orig) {
        Queue<Long> countQueue = new LinkedList<>(givenCountQueue); // deep copy
        if (str == null || str.length() == 0) {
            System.out.println("Empty string");
            return 0;
        }
        var c = str.charAt(0);
        var newStr = str.substring(1);
        if (str.length() == 1) {
            // last character
            if ((c == '#' || c == '?') && ((currentCount == 1 && countQueue.isEmpty())
                    || (lastCharDotOrStart && currentCount == 0 && countQueue.size() == 1 && countQueue.peek() == 1))) {
                return 1;
            } else if ((c == '.' || c == '?') && currentCount == 0 && countQueue.isEmpty()) {
                return 1;
            } else {
                return 0;
            }
        }
        if (c == '#') {
            if (currentCount == 0 && !lastCharDotOrStart) {
                return 0; // doesn't work, defective count already reached, but more defective found
            } else if (currentCount == 0 && lastCharDotOrStart) {
                // start a new parsingDefective loop
                Long currentCountNew = countQueue.poll();
                if (currentCountNew == null) {
                    return 0; // queue empty
                } else {
                    currentCount = currentCountNew - 1;
                }
                return remainingCombinations(newStr, currentCount, countQueue, false, finalString + c, orig);
            } else {
                currentCount--;
                return remainingCombinations(newStr, currentCount, countQueue, false, finalString + c, orig);
            }
        } else if (c == '.') {
            if (!lastCharDotOrStart && currentCount > 0) {
                return 0;
            }
            return remainingCombinations(newStr, currentCount, countQueue, true, finalString + c, orig);
        } else {
            // c basically must be '?' here
            if (c != '?') {
                System.out.println(c);
                throw new RuntimeException("This should not happen");
            }

            var poundVariant = remainingCombinations("#" + newStr, currentCount, countQueue, lastCharDotOrStart,
                    finalString, orig);
            var dotVariant = remainingCombinations("." + newStr, currentCount, countQueue, lastCharDotOrStart,
                    finalString, orig);

            return poundVariant + dotVariant;
        }
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
        var currentCount = countQueue.poll();

        if (springs.charAt(0) == '?') {
            return remainingCombinations("#" + springs.substring(1), currentCount, countQueue, true, "", countList)
                    + remainingCombinations("." + springs.substring(1), currentCount, countQueue, true, "", countList);
        } else {
            return remainingCombinations(springs, currentCount, countQueue, true, "", countList);
        }
    }

    public static ImmutablePair<Long, Long> springRecords(String[] input) {
        long ctr1 = 0;
        long ctr2 = 0;
        for (var combination : input) {
            ctr1 += combinations(combination);
            // ctr2 += combinations(unfold(combination));
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
