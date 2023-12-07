package unitmax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 {

    static class XtoYMap {

        private class Mapping {

            public long src;
            public long dst;
            public long len;

            public Mapping(long src, long dst, long len) {
                this.src = src;
                this.dst = dst;
                this.len = len;
            }
        }

        private List<Mapping> mappingList = new ArrayList<>();

        public void addMappedOutput(long srcRangeStart, long dstRangeStart, long len) {
            mappingList.add(new Mapping(srcRangeStart, dstRangeStart, len));
        }

        public long getMappedOutput(long input) {
            for (var mapping : mappingList) {
                if (input >= mapping.src && input < (mapping.src + mapping.len)) {
                    var dist = input - mapping.src;
                    return mapping.dst + dist;
                }
            }
            return input;
        }

    }

    private static Pattern getMapPattern(String mapName) {
        return Pattern.compile(String.format("%s map:([\\s\\S]*?)((^\\s*$)|\\z)", mapName), Pattern.MULTILINE);
    }

    private static String[] mapNames = new String[] { "seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water",
            "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location" };

    public static long getSoilLocation(String input) {
        List<XtoYMap> mapList = new ArrayList<>();
        List<Long> seeds = new ArrayList<>();
        var seedString = input.split("\n")[0];
        Pattern.compile("([0-9]+)").matcher(seedString).results().forEach(mr -> {
            seeds.add(Long.parseLong(mr.group(1)));
        });

        for (var mapName : mapNames) {
            XtoYMap map = new XtoYMap();
            var mapString = getMapPattern(mapName).matcher(input).results().map(mr -> mr.group(1))
                    .collect(Collectors.toList()).get(0).strip();
            var lines = mapString.split("\n");
            for (var line : lines) {
                var ranges = Pattern.compile("([0-9]+)").matcher(line).results().map(mr -> Long.parseLong(mr.group(1)))
                        .collect(Collectors.toList());
                map.addMappedOutput(ranges.get(1), ranges.get(0), ranges.get(2));
            }
            mapList.add(map);
        }

        List<Long> mappedSeeds = new ArrayList<>();
        for (var seed : seeds) {
            long x = seed;
            for (var map : mapList) {
                x = map.getMappedOutput(x);
            }
            mappedSeeds.add(x);
        }

        return Collections.min(mappedSeeds);
    }

}
