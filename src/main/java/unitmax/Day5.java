package unitmax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 {

    static class Package {
        public long start;
        public long len;

        public Package(long start, long len) {
            this.start = start;
            this.len = len;
        }
    }

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

        public List<Package> getMappedOutputPackage(Package input) {
            List<Package> pList = new ArrayList<>();
            long currentPackagePosition = input.start;
            var maxCurrentPackage = input.start + input.len;
            while (currentPackagePosition < maxCurrentPackage) {
                boolean foundMatch = false;
                for (var mapping : mappingList) {
                    var maxMappingPos = mapping.src + mapping.len;
                    if (currentPackagePosition >= mapping.src && currentPackagePosition < maxMappingPos) {
                        var dist = currentPackagePosition - mapping.src;
                        var destStart = mapping.dst + dist;
                        if (maxCurrentPackage <= maxMappingPos) {
                            // Mapping goes beyond package (or ends with it exaclty)
                            var pkg = new Package(destStart, maxCurrentPackage - currentPackagePosition);
                            pList.add(pkg);
                            currentPackagePosition = maxCurrentPackage; // this quits the loop
                        } else {
                            // Mapping ends before package ends
                            var pkg = new Package(destStart, maxMappingPos - currentPackagePosition);
                            pList.add(pkg);
                            currentPackagePosition = maxMappingPos;
                        }
                        foundMatch = true;
                        break;
                    }
                }

                if (!foundMatch) {
                    // No match = same input->output until next mapping is found
                    // Find earliest next match
                    final long cpp = currentPackagePosition;
                    var nextMappingSrList = mappingList.stream()
                            .filter(mapping -> mapping.src >= cpp && mapping.src <= maxCurrentPackage)
                            .map(mapping -> mapping.src).collect(Collectors.toList());
                    if (nextMappingSrList.isEmpty()) {
                        // No next mapping, return rest
                        var pkg = new Package(currentPackagePosition, maxCurrentPackage - currentPackagePosition);
                        pList.add(pkg);
                        currentPackagePosition = maxCurrentPackage;
                    } else {
                        // There is a next mapping
                        var minNextMappingSrc = Collections.min(nextMappingSrList);
                        var len = minNextMappingSrc - currentPackagePosition;
                        var pkg = new Package(currentPackagePosition, len);
                        pList.add(pkg);
                        currentPackagePosition += len;
                    }
                }
            }
            return pList;
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

    public static long getSoilLocationWithRanges(String input) {
        List<XtoYMap> mapList = new ArrayList<>();
        List<Long> initialSeeds = new ArrayList<>();
        List<Package> seeds = new ArrayList<>();
        var seedString = input.split("\n")[0];
        Pattern.compile("([0-9]+)").matcher(seedString).results().forEach(mr -> {
            initialSeeds.add(Long.parseLong(mr.group(1)));
        });

        boolean first = true;
        long rbegin = 0;
        for (var iseed : initialSeeds) {
            if (first) {
                first = false;
                rbegin = iseed;
            } else {
                first = true;
                seeds.add(new Package(rbegin, iseed/* len */));
            }
        }

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

        List<Package> mappedSeeds = seeds;
        for (var map : mapList) {
            List<Package> output = new ArrayList<>();
            for (var seed : mappedSeeds) {
                output.addAll(map.getMappedOutputPackage(seed));
            }
            mappedSeeds = output;
        }

        return Collections.min(mappedSeeds.stream().map(pkg -> pkg.start).collect(Collectors.toList()));
    }

}
