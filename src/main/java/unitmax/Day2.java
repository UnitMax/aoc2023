package unitmax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class Day2 {
    public static class Balls {
        public int blue = 0;
        public int red = 0;
        public int green = 0;
    }

    public static String readResource(String filename) throws IOException {
        InputStream is = Day1.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static int numberOfPossibleGames(String fileContent) {
        return numberOfPossibleGames(fileContent.split("\n"));
    }

    private static Pattern patternBlue = Pattern.compile("([0-9]+) blue");
    private static Pattern patternGreen = Pattern.compile("([0-9]+) green");
    private static Pattern patternRed = Pattern.compile("([0-9]+) red");

    public static boolean subSetGamePossible(String ssg) {
        Matcher blue = patternBlue.matcher(ssg);
        Matcher green = patternGreen.matcher(ssg);
        Matcher red = patternRed.matcher(ssg);
        // 12 red cubes, 13 green cubes, and 14 blue cubes
        if (red.find()) {
            int redNr = Integer.parseInt(red.group(1));
            if (redNr > 12) {
                return false;
            }
        }
        if (green.find()) {
            int greenNr = Integer.parseInt(green.group(1));
            if (greenNr > 13) {
                return false;
            }
        }
        if (blue.find()) {
            int blueNr = Integer.parseInt(blue.group(1));
            if (blueNr > 14) {
                return false;
            }
        }

        return true;
    }

    public static Balls nrOfBalls(String ssg) {
        Matcher blue = patternBlue.matcher(ssg);
        Matcher green = patternGreen.matcher(ssg);
        Matcher red = patternRed.matcher(ssg);
        Balls balls = new Balls();
        if (red.find()) {
            balls.red = Integer.parseInt(red.group(1));
        }
        if (green.find()) {
            balls.green = Integer.parseInt(green.group(1));
        }
        if (blue.find()) {
            balls.blue = Integer.parseInt(blue.group(1));
        }

        return balls;
    }

    public static int numberOfPossibleGames(String[] lines) {
        int ctr = 0;
        Pattern patternGameId = Pattern.compile("Game ([0-9]+)");
        for (String line : lines) {
            Matcher gameIdMatcher = patternGameId.matcher(line);
            int gameId = 0;
            if (gameIdMatcher.find()) {
                gameId = Integer.parseInt(gameIdMatcher.group(1));
            }
            String[] subSetGame = line.split(";");
            boolean gamePossible = true;
            for (String ssg : subSetGame) {
                if (!subSetGamePossible(ssg)) {
                    gamePossible = false;
                    break;
                }
            }
            if (gamePossible) {
                ctr += gameId;
            }
            System.out.println(String.format("Game[ID=%d] Possible=%b ==> %s", gameId, gamePossible, line));
        }
        return ctr;
    }

    public static int minimumNrOfBalls(String fileContent) {
        return minimumNrOfBalls(fileContent.split("\n"));
    }

    public static int minimumNrOfBalls(String[] lines) {
        int ctr = 0;
        for (String line : lines) {
            String[] subSetGame = line.split(";");
            Balls thisGame = new Balls();
            for (String ssg : subSetGame) {
                Balls balls = nrOfBalls(ssg);
                if (balls.red > thisGame.red) {
                    thisGame.red = balls.red;
                }
                if (balls.green > thisGame.green) {
                    thisGame.green = balls.green;
                }
                if (balls.blue > thisGame.blue) {
                    thisGame.blue = balls.blue;
                }
            }
            ctr += (thisGame.red * thisGame.green * thisGame.blue);
        }
        return ctr;
    }
}
