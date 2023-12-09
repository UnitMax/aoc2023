package unitmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day7Part2 {

    enum Cards {
        A("A"),
        K("K"),
        Q("Q"),
        T("T"),
        _9("9"),
        _8("8"),
        _7("7"),
        _6("6"),
        _5("5"),
        _4("4"),
        _3("3"),
        _2("2"),
        J("J");

        private String value;

        Cards(String value) {
            this.value = value;
        }

        public static Cards fromString(String card) {
            for (Cards c : Cards.values()) {
                if (c.value.equalsIgnoreCase(card)) {
                    return c;
                }
            }
            return null;
        }
    }

    static class Hand {
        public String hand;
        public long bid;
        public long strength;

        public static long[] strengths = { 7, 6, 5, 4, 3, 2, 1 };

        public Hand(String hand, long bid) {
            this.hand = hand;
            this.bid = bid;
            calcStrength(hand);
        }

        public static Map<Character, Integer> countCardOccurrences(String hand) {
            Map<Character, Integer> occurrences = new HashMap<>();
            Arrays.stream(Cards.values()).filter(c -> c != Cards.J /* no joker */).forEach(card -> {
                Character c = card.value.charAt(0);
                occurrences.put(c, StringUtils.countMatches(hand, c));
            });
            return occurrences;
        }

        public void calcStrength(String hand) {
            var cardMap = countCardOccurrences(hand);
            var jokers = StringUtils.countMatches(hand, "J");
            // Five of a kind (either cannot contain jokers or is all jokers)
            if (cardMap.values().stream().filter(c -> c == 5).count() == 1 || jokers == 5) {
                this.strength = 7;
                return;
            }
            // Four of a kind
            if (cardMap.values().stream().filter(c -> c == 4).count() == 1) {
                if (jokers == 1) {
                    this.strength = 7; // makes five of a kind
                } else {
                    this.strength = 6;
                }
                return;
            }
            // Full house or Three of a Kind
            if (cardMap.values().stream().filter(c -> c == 3).count() == 1) {
                if (cardMap.values().stream().filter(c -> c == 2).count() == 1) {
                    // Full house (cannot contain jokers)
                    this.strength = 5;
                    return;
                } else {
                    // Three of a kind
                    switch (jokers) {
                        case 1:
                            this.strength = 6; // four of a kind
                            break;
                        case 2:
                            this.strength = 7; // five of a kind
                            break;
                        default:
                            this.strength = 4; // no jokers = 3 of a kind
                            break;
                    }
                    return;
                }
            }
            // Two pairs
            if (cardMap.values().stream().filter(c -> c == 2).count() == 2) {
                if (jokers == 1) {
                    // Two pairs + joker makes full house
                    this.strength = 5;
                } else {
                    this.strength = 3;
                }
                return;
            }
            // One pair
            if (cardMap.values().stream().filter(c -> c == 2).count() == 1) {
                switch (jokers) {
                    case 1:
                        this.strength = 4; // three of a kind
                        break;
                    case 2:
                        this.strength = 6; // four of a kind
                        break;
                    case 3:
                        this.strength = 7; // five of a kind
                        break;
                    default:
                        this.strength = 2; // no jokers = one pair
                        break;
                }
                return;
            }
            // High card (none of the above)
            switch (jokers) {
                case 1:
                    this.strength = 2; // one pair
                    break;
                case 2:
                    this.strength = 4; // three of a kind
                    break;
                case 3:
                    this.strength = 6; // four of a kind
                    break;
                case 4:
                case 5:
                    this.strength = 7; // five of a kind
                    break;
                default:
                    this.strength = 1; // no jokers = highest card
                    break;
            }
            return;
        }

        public int compareAgainst(Hand otherHand) {
            if (this.strength != otherHand.strength) {
                return (int) this.strength - (int) otherHand.strength;
            }
            for (int i = 0; i < hand.length(); i++) {
                var myCard = hand.toCharArray()[i];
                var otherCard = otherHand.hand.toCharArray()[i];
                var myCardVal = Cards.fromString(Character.toString(myCard));
                var otherCardVal = Cards.fromString(Character.toString(otherCard));
                if (myCardVal.compareTo(otherCardVal) == 0) {
                    continue;
                } else {
                    return myCardVal.compareTo(otherCardVal);
                }
            }
            return 0;
        }
    }

    public static long totalWinnings(String[] input) {
        List<Hand> handList = new ArrayList<>();
        for (var s : input) {
            var handString = s.split(" ");
            var hand = new Hand(handString[0], Long.parseLong(handString[1]));
            handList.add(hand);
        }

        // Filter hands by strengths into sublist
        List<List<Hand>> loloHands = new ArrayList<>();
        Arrays.stream(Hand.strengths).forEach(str -> {
            // System.out.println("Sublist for str = " + str);
            // Get sublist by filter
            var subList = handList.stream().filter(hand -> hand.strength == str).collect(Collectors.toList());
            // Order sublist within itself
            Collections.sort(subList, (h1, h2) -> h1.compareAgainst(h2));
            loloHands.add(subList);
        });

        List<Hand> finalHandList = loloHands.stream().flatMap(List::stream).collect(Collectors.toList());
        var rank = finalHandList.size();

        long ctr = 0;
        for (var hand : finalHandList) {
            // System.out.println(rank + ": " + hand.hand + " (" + hand.bid + ") [Strength=" + hand.strength + "]");
            ctr += (rank * hand.bid);
            rank--;
        }

        return ctr;
    }

}
