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

            int ctr2 = Day1Part2.decodeCalibrationDocument(day1str);
            System.out.println(String.format("Result Day2 = %d", ctr2));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
