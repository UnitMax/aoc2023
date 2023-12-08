package unitmax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day6 {

    static class Race {
        public long time;
        public long distance;

        public Race(long time, long distance) {
            this.time = time;
            this.distance = distance;
        }
    }

    public static long getNrOfWaysToBeatRecord(Race race) {
        long ctr = 0;
        for (int i = 1; i < race.time; i++) {
            long racingTime = race.time - i;
            long speed = i;
            long finalDistance = speed * racingTime;
            if (finalDistance > race.distance) {
                ctr++;
            }
        }
        return ctr;
    }

    private static Pattern nrPattern = Pattern.compile("([0-9]+)");

    public static long leverageBoatRace(String[] input) {
        List<Race> races = new ArrayList<>();
        List<Long> times = new ArrayList<>();
        List<Long> dists = new ArrayList<>();
        String timesString = input[0];
        String distString = input[1];

        nrPattern.matcher(timesString).results().forEach(result -> {
            times.add(Long.parseLong(result.group(1)));
        });
        nrPattern.matcher(distString).results().forEach(result -> {
            dists.add(Long.parseLong(result.group(1)));
        });
        for (int i = 0; i < times.size(); i++) {
            races.add(new Race(times.get(i), dists.get(i)));
        }

        long ctr = 1;
        for (var race : races) {
            ctr *= getNrOfWaysToBeatRecord(race);
        }

        return ctr;
    }

}
