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
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
