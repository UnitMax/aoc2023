package unitmax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day14 {

    static class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    private static void shakeUp(String[] input) {
        List<ImmutablePair<Coordinate, Integer>> moveUp = new ArrayList<>();
        List<StringBuilder> stringBuilders = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                var c = input[i].charAt(j);
                if (c == 'O') {
                    int ctr = 0;
                    for (int k = i - 1; k >= 0; k--) {
                        var cAbove = input[k].charAt(j);
                        if (cAbove == '.') {
                            ctr++;
                        } else if (cAbove == '#') {
                            break;
                        } else {
                            // that means it's an O ==> skip
                        }
                    }
                    if (ctr > 0) {
                        moveUp.add(new ImmutablePair<Coordinate, Integer>(new Coordinate(j, i), ctr));
                    }
                }
            }
            stringBuilders.add(new StringBuilder(input[i]));
        }
        Collections.sort(moveUp, (c1, c2) -> c1.getLeft().y - c2.getLeft().y);
        for (var c : moveUp) {
            var coord = c.getLeft();
            stringBuilders.get(coord.y).setCharAt(coord.x, '.');
            stringBuilders.get(coord.y - c.getRight()).setCharAt(coord.x, 'O');
        }
        for (int i = 0; i < input.length; i++) {
            input[i] = stringBuilders.get(i).toString();
        }
    }

    public static ImmutablePair<Long, Long> totalLoad(String[] input) {
        long ctr1 = 0;
        long ctr2 = 0;
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i].strip();
        }
        shakeUp(input);
        for (int i = 0; i < input.length; i++) {
            System.out.println(input[i]);
            for (int j = 0; j < input[0].length(); j++) {
                if (input[i].charAt(j) == 'O') {
                    ctr1 += (input.length - i);
                }
            }
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
