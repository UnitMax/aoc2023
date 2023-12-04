package unitmax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public class Day3 {
    public static class SchematicNumber {
        String numberS = "";
        int number = 0;
        int y = 0;
        int xFrom = 0;
        int xTo;
    }

    private static Pattern patternSymbol = Pattern.compile("([^0-9\\.])");

    public static boolean isSymbolAdjacent(SchematicNumber sn, char[][] board) {
        int xStart = sn.xFrom - 1;
        int yStart = sn.y - 1;
        int xEnd = sn.xTo + 1;
        int yEnd = sn.y + 1;
        // System.out.println("------ " + sn.number + " / " + xStart + " " + xEnd + " -
        // " + yStart + " " + yEnd + " /// "
        // + board.length + " " + board[0].length);
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
                    continue;
                }
                char c = board[i][j];
                String s = String.format("%c", c);
                // System.out.println(s);
                Matcher m = patternSymbol.matcher(s);
                if (m.matches()) {
                    // System.out.println("--M----");
                    return true;
                }
            }
        }
        // System.out.println("------");
        return false;
    }

    private static Pattern patternNumber = Pattern.compile("([0-9]+)");

    private static char[][] getBoard(String[] schematicString) {
        int x = schematicString.length;
        int y = schematicString[0].length();
        // Fill schematic
        char[][] schematic = new char[y][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                schematic[i][j] = schematicString[j].charAt(i);
            }
        }
        return schematic;
    }

    private static List<SchematicNumber> getSchematicNumbers(String[] schematicString) {
        List<SchematicNumber> snList = new ArrayList<>();
        for (int i = 0; i < schematicString.length; i++) {
            String s = schematicString[i];
            Matcher m = patternNumber.matcher(s);
            while (m.find()) {
                // System.out.println("Found " + m.group(1) + " start=" + m.start() + " end = "
                // + m.end());
                SchematicNumber sn = new SchematicNumber();
                sn.numberS = m.group(1);
                sn.number = Integer.parseInt(m.group(1));
                sn.y = i;
                sn.xFrom = m.start();
                sn.xTo = m.end() - 1;
                snList.add(sn);
            }
        }
        return snList;
    }

    public static int decodeEngineSchematic(String[] schematicString) {
        int ctr = 0;
        // Find numbers
        List<SchematicNumber> snList = getSchematicNumbers(schematicString);
        // Get board
        char[][] schematic = getBoard(schematicString);
        for (SchematicNumber sn : snList) {
            if (isSymbolAdjacent(sn, schematic)) {
                // System.out.println("Symbol adjacent: " + sn.numberS);
                ctr += sn.number;
            }
        }
        // Print schematic
        int x = schematicString.length;
        int y = schematicString[0].length();
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                System.out.print(schematic[i][j]);
            }
            System.out.println();
        }
        return ctr;
    }

    public static class Gear {
        int x = 0;
        int y = 0;

        public Gear(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static Pattern gearPattern = Pattern.compile("\\*");

    public static int gearRatios(String[] schematicString) {
        int ctr = 0;
        char[][] schematic = getBoard(schematicString);
        List<SchematicNumber> snList = getSchematicNumbers(schematicString);
        List<Gear> gearList = new ArrayList<>();
        for (int i = 0; i < schematicString.length; i++) {
            String s = schematicString[i];
            final int y = i;
            var gearListSub = gearPattern.matcher(s).results().map(r -> new Gear(r.start(), y))
                    .collect(Collectors.toList());
            gearList.addAll(gearListSub);
        }
        return ctr;
    }
}
