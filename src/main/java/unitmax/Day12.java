package unitmax;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day12 {

    private static long remainingCombinations(String str, long currentCount, Queue<Long> givenCountQueue,
            boolean lastCharDotOrStart, String finalString) {
        Queue<Long> countQueue = new LinkedList<>(givenCountQueue); // deep copy
        // System.out
        //         .println("Remaining combinations: " + str + " [currentCount=" + currentCount
        //                 + "][" + countQueue + "]");
        if (str == null || str.length() == 0) {
            System.out.println("Empty string");
            return 0;
        }
        var c = str.charAt(0);
        var newStr = str.substring(1);
        if (str.length() == 1) {
            // last character
            if ((c == '#' || c == '?') && (currentCount == 1 && countQueue.isEmpty())
                    || (lastCharDotOrStart && currentCount == 0 && countQueue.size() == 1 && countQueue.peek() == 1)) {
                System.out
                        .println("+1[Finished#] on c=" + c + " cc=" + currentCount + " cqEmpty=" +
                                countQueue.isEmpty() + " cc=" + countQueue);
                System.out.println("OK Finished = " + finalString + "#");
                return 1;
            } else if ((c == '.' || c == '?') && currentCount == 0 && countQueue.isEmpty()) {
                System.out
                        .println("+1[Finished.] on c=" + c + " cc=" + currentCount + " cqEmpty=" +
                                countQueue.isEmpty());
                System.out.println("OK Finished = " + finalString + ".");
                return 1;
            } else {
                System.out
                        .println("+0[Finished] on c=" + c + " cc=" + currentCount + " cqEmpty=" +
                                countQueue.isEmpty());
                System.out.println("XX Finished = " + finalString + c);
                return 0;
            }
        }
        if (c == '#') {
            if (currentCount <= 0 && !lastCharDotOrStart) {
                // System.out.println("c==# and cc <= 0 // " + finalString + c);
                return 0; // doesn't work, defective count already reached, but more defective found
            } else if (currentCount <= 0 && lastCharDotOrStart) {
                // start a new parsingDefective loop
                Long currentCountNew = countQueue.poll();
                if (currentCountNew == null) {
                    // System.out.println("c==# and queue empty // " + finalString + c);
                    return 0; // queue empty
                } else {
                    currentCount = currentCountNew - 1;
                }
                return remainingCombinations(newStr, currentCount, countQueue, false, finalString + c);
            } else {
                currentCount--;
                return remainingCombinations(newStr, currentCount, countQueue, false, finalString + c);
            }
        } else if (c == '.') {
            if (!lastCharDotOrStart && currentCount > 0) {
                // System.out.println("c=. and cc > 0 // " + finalString + c);
                return 0;
            }
            return remainingCombinations(newStr, currentCount, countQueue, true, finalString + c);
        } else {
            // c basically must be '?' here
            if (c != '?') {
                System.out.println(c);
                throw new RuntimeException("This should not happen");
            }

            var poundVariant = remainingCombinations("#" + newStr, currentCount, countQueue, lastCharDotOrStart,
                    finalString);
            var dotVariant = remainingCombinations("." + newStr, currentCount, countQueue, lastCharDotOrStart,
                    finalString);

            return poundVariant + dotVariant;
        }
    }

    private static long combinations(String input) {
        var splitInput = input.split(" ");
        var springs = splitInput[0].strip();
        var countsString = splitInput[1].strip();
        System.out.println(springs);
        System.out.println(countsString);

        var countList = Arrays.stream(countsString.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
        Queue<Long> countQueue = new LinkedList<>(countList);
        var currentCount = countQueue.poll();

        if (springs.charAt(0) == '?') {
            return remainingCombinations("#" + springs.substring(1), currentCount, countQueue, true, "")
                    + remainingCombinations("." + springs.substring(1), currentCount, countQueue, true, "");
        } else {
            return remainingCombinations(springs, currentCount, countQueue, true, "");
        }
    }

    public static ImmutablePair<Long, Long> springRecords(String[] input) {
        long ctr1 = 0;
        for (var combination : input) {
            ctr1 += combinations(combination);
        }
        long ctr2 = 0;
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
