package unitmax;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        try {
            int ctr = Day1.decodeCalibrationDocument(Day1.readResource("day1.txt"));
            System.out.println(String.format("Result Day1 = %d", ctr));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
