package unitmax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class Day1 {
    public static String readResource(String filename) throws IOException {
        InputStream is = Day1.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static int decodeCalibrationDocument(String fileContent) {
        return decodeCalibrationDocument(fileContent.split("\n"));
    }

    public static int decodeCalibrationDocument(String[] lines) {
        int ctr = 0;
        String patternStr = "[0-9]";
        Pattern pattern = Pattern.compile(patternStr);
        for (String line : lines) {
            String lineReverse = new StringBuilder(line).reverse().toString();
            Matcher matcherStart = pattern.matcher(line);
            Matcher matcherEnd = pattern.matcher(lineReverse);
            if (matcherStart.find() && matcherEnd.find()) {
                int startIdx = matcherStart.start();
                int endIdx = line.length() - matcherEnd.start() - 1;
                // Conversion of endIdx not necessary since we can use charAt at lineReverse
                // This is just to illustrate
                String numberStr = String.format("%c%c", line.charAt(startIdx), line.charAt(endIdx));
                int number = Integer.parseInt(numberStr);
                ctr += number;
            }
        }
        return ctr;
    }
}
