package unitmax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class Day1Part2 {
    public static String readResource(String filename) throws IOException {
        InputStream is = Day1.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static int decodeCalibrationDocument(String fileContent) {
        return decodeCalibrationDocument(fileContent.split("\n"));
    }

    public static int decodeNumber(String number) {
        switch (number) {
            case "one":
            case "1":
                return 1;
            case "two":
            case "2":
                return 2;
            case "three":
            case "3":
                return 3;
            case "four":
            case "4":
                return 4;
            case "five":
            case "5":
                return 5;
            case "six":
            case "6":
                return 6;
            case "seven":
            case "7":
                return 7;
            case "eight":
            case "8":
                return 8;
            case "nine":
            case "9":
                return 9;
            default:
                System.err.println("this happened");
                return 0;
        }
    }

    public static int decodeCalibrationDocument(String[] lines) {
        int ctr = 0;
        // Positive lookahead...
        String patternStr = "(?=(one|two|three|four|five|six|seven|eight|nine|[0-9]))";
        Pattern pattern = Pattern.compile(patternStr);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String firstOccurence = matcher.group(1);
            String lastOccurence = "";
            while (matcher.find()) {
                lastOccurence = matcher.group(1);
            }
            if (lastOccurence.isEmpty()) {
                lastOccurence = firstOccurence;
            }
            int n1 = decodeNumber(firstOccurence);
            int n2 = decodeNumber(lastOccurence);
            String finalNumberStr = String.format("%d%d", n1, n2);
            int finalNumber = Integer.parseInt(finalNumberStr);
            ctr += finalNumber;
        }
        return ctr;
    }
}
