package unitmax;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        try {
            String day1str = Day1.readResource("day1.txt");
            int ctr1 = Day1.decodeCalibrationDocument(day1str);
            System.out.println(String.format("Result Day1 = %d", ctr1));

            int ctr1p2 = Day1Part2.decodeCalibrationDocument(day1str);
            System.out.println(String.format("Result Day1Part2 = %d", ctr1p2));

            String day2str = Day2.readResource("day2.txt");
            int ctr2 = Day2.numberOfPossibleGames(day2str);
            System.out.println(String.format("Result Day2 = %d", ctr2));

            int ctr2p2 = Day2.minimumNrOfBalls(day2str);
            System.out.println(String.format("Result Day2Part2 = %d", ctr2p2));

            String[] day3 = Util.readResourceLines("day3.txt");
            int ctr3 = Day3.decodeEngineSchematic(day3);
            System.out.println(String.format("Result Day3 = %d", ctr3));
            int ctr3p2 = Day3.gearRatios(day3);
            System.out.println(String.format("Result Day3Part2 = %d", ctr3p2));

            String[] day4 = Util.readResourceLines("day4.txt");
            int ctr4 = Day4.cardScore(day4);
            System.out.println(String.format("Result Day4 = %d", ctr4));
            int ctr4p2 = Day4.cumulatingCardScore(day4);
            System.out.println(String.format("Result Day4Part2 = %d", ctr4p2));

            String day5 = Util.readResource("day5.txt");
            long ctr5 = Day5.getSoilLocation(day5);
            System.out.println(String.format("Result Day5 = %d", ctr5));
            long ctr5p2 = Day5.getSoilLocationWithRanges(day5);
            System.out.println(String.format("Result Day5Part2 = %d", ctr5p2));

            String[] day6 = Util.readResourceLines("day6.txt");
            long ctr6 = Day6.leverageBoatRace(day6);
            System.out.println(String.format("Result Day6 = %d", ctr6));
            long ctr6p2 = Day6.leverageBoatRaceNoSpaces(day6);
            System.out.println(String.format("Result Day6Part2 = %d", ctr6p2));

            String[] day7 = Util.readResourceLines("day7.txt");
            long ctr7 = Day7.totalWinnings(day7);
            System.out.println(String.format("Result Day7 = %d", ctr7));
            long ctr7p2 = Day7Part2.totalWinnings(day7);
            System.out.println(String.format("Result Day7Part2 = %d", ctr7p2));

            String[] day8 = Util.readResourceLines("day8.txt");
            long ctr8 = Day8.nrOfSteps(day8);
            System.out.println(String.format("Result Day8 = %d", ctr8));
            long ctr8p2 = Day8.nrOfStepsGhost(day8);
            System.out.println(String.format("Result Day8Part2 = %d", ctr8p2));

            String[] day9 = Util.readResourceLines("day9.txt");
            long ctr9 = Day9.sumOfExtrapolatedValues(day9);
            System.out.println(String.format("Result Day9 = %d", ctr9));
            long ctr9p2 = Day9.sumOfExtrapolatedValuesBackwards(day9);
            System.out.println(String.format("Result Day9Part2 = %d", ctr9p2));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
