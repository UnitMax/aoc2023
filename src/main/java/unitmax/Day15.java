package unitmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day15 {

    static class Lens {
        public String label;
        public int focalLength;

        public Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        @Override
        public String toString() {
            return "Lens [label=" + label + ", focalLength=" + focalLength + "]";
        }

    }

    private static long hashString(String input) {
        long result = 0;
        for (int i = 0; i < input.length(); i++) {
            var c = input.charAt(i);
            var asciiCode = (int) c;
            result += asciiCode;
            result *= 17;
            result %= 256;
        }
        return result;
    }

    static Pattern labelPattern = Pattern.compile("([a-z]+)");

    static Pattern addRemovePattern = Pattern.compile("([=-]{1})");

    static Pattern focalLengthPattern = Pattern.compile("([0-9]{1})");

    private static long focusingPower(String[] input) {
        long ctr = 0;
        Map<Long, List<Lens>> lensMap = new HashMap<>();
        for (var s : input) {
            var splitResult = s.split(",");
            for (var sr : splitResult) {
                sr = sr.strip();
                var matcherLabel = labelPattern.matcher(sr);
                var matcherAddRemoveLens = addRemovePattern.matcher(sr);
                var matcherFocalLength = focalLengthPattern.matcher(sr);
                matcherLabel.find();
                matcherAddRemoveLens.find();
                matcherFocalLength.find();
                var label = matcherLabel.group(1);
                var addRemoveLens = matcherAddRemoveLens.group(1).charAt(0);
                int focalLength = addRemoveLens == '=' ? Integer.parseInt(matcherFocalLength.group(1)) : -1;
                var boxNr = hashString(label);
                if (!lensMap.containsKey(boxNr)) {
                    lensMap.put(boxNr, new ArrayList<Lens>());
                }

                var box = lensMap.get(boxNr);
                Optional<Lens> labelFilterResult = box.stream().filter(b -> b.label.equals(label)).findFirst();
                if (addRemoveLens == '=') {
                    if (labelFilterResult.isPresent()) {
                        // replace lens
                        box.set(box.indexOf(labelFilterResult.get()), new Lens(label, focalLength));
                    } else {
                        // add lens at the end
                        box.add(new Lens(label, focalLength));
                    }
                } else {
                    if (labelFilterResult.isPresent()) {
                        box.remove(labelFilterResult.get());
                    }
                }
            }
        }
        for (var x : lensMap.entrySet()) {
            for (int i = 0; i < x.getValue().size(); i++) {
                ctr += ((x.getKey() + 1) * (x.getValue().get(i).focalLength * (i + 1)));
            }
        }
        return ctr;
    }

    public static ImmutablePair<Long, Long> hashResult(String[] input) {
        long ctr1 = 0;
        long ctr2 = focusingPower(input);
        for (var s : input) {
            var splitResult = s.split(",");
            for (var sr : splitResult) {
                ctr1 += hashString(sr);
            }
        }
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }
}
