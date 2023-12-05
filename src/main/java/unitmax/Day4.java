package unitmax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {

    private static Pattern cardPattern = Pattern.compile("(Card\\s+[0-9]+:)");

    private static Pattern nrPattern = Pattern.compile("([0-9]+)");

    public static int cardScore(String[] cards) {
        int ctr = 0;
        for (String card : cards) {
            System.out.println(card);
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
            System.out.println(winningNrs + "///" + myNrs + "//Score=" + finalScore);
            ctr += finalScore;
        }
        return ctr;
    }

}
