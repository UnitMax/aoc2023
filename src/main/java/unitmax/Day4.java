package unitmax;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {

    private static Pattern cardNrPattern = Pattern.compile("Card\\s+([0-9]+):");

    private static Pattern cardPattern = Pattern.compile("(Card\\s+[0-9]+:)");

    private static Pattern nrPattern = Pattern.compile("([0-9]+)");

    public static int cardScore(String[] cards) {
        int ctr = 0;
        for (String card : cards) {
            // System.out.println(card);
            String[] sides = card.split("\\|");
            String winningNrs = sides[0];
            String myNrs = sides[1];
            Matcher cardNrRemover = cardPattern.matcher(winningNrs);
            if (cardNrRemover.find()) {
                winningNrs = winningNrs.replace(cardNrRemover.group(1), "");
            }

            Matcher winningNrMatcher = nrPattern.matcher(winningNrs);
            Matcher myNrMatcher = nrPattern.matcher(myNrs);
            var winningNrSet = winningNrMatcher.results().map(r -> Integer.parseInt(r.group(1)))
                    .collect(Collectors.toSet());
            var finalScore = myNrMatcher.results().map(r -> Integer.parseInt(r.group(1))).filter(winningNrSet::contains)
                    .reduce(0, (subtotal, element) -> {
                        return (subtotal == 0 ? 1 : (subtotal * 2));
                    });
            // System.out.println(winningNrs + "///" + myNrs + "//Score=" + finalScore);
            ctr += finalScore;
        }
        return ctr;
    }

    public static int cumulatingCardScore(String[] cards) {
        int ctr = 0;
        var cardQueue = new ArrayDeque<Integer>();
        var cardMap = new HashMap<Integer, Integer>();
        for (String card : cards) {
            int cardNr = 0;
            var cardNrMatcher = cardNrPattern.matcher(card);
            if (cardNrMatcher.find()) {
                cardNr = Integer.parseInt(cardNrMatcher.group(1));
            }
            String[] sides = card.split("\\|");
            String winningNrs = sides[0];
            String myNrs = sides[1];
            Matcher cardNrRemover = cardPattern.matcher(winningNrs);
            if (cardNrRemover.find()) {
                winningNrs = winningNrs.replace(cardNrRemover.group(1), "");
            }

            Matcher winningNrMatcher = nrPattern.matcher(winningNrs);
            Matcher myNrMatcher = nrPattern.matcher(myNrs);
            var winningNrSet = winningNrMatcher.results().map(r -> Integer.parseInt(r.group(1)))
                    .collect(Collectors.toSet());
            var nrOfWinningCards = myNrMatcher.results().map(r -> Integer.parseInt(r.group(1)))
                    .filter(winningNrSet::contains)
                    .count();

            cardMap.put(cardNr, (int) nrOfWinningCards);
            cardQueue.add(cardNr);
        }
        while (!cardQueue.isEmpty()) {
            int card = cardQueue.poll();
            if (!cardMap.containsKey(card)) {
                // card doesn't exist
                continue;
            }
            int nrOfWinningCards = cardMap.get(card);
            for (int i = 0; i < nrOfWinningCards; i++) {
                cardQueue.add(card + i + 1);
            }

            // add card itself to counter
            ctr++;
        }
        return ctr;
    }

}
