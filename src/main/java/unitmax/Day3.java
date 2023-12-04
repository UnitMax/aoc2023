package unitmax;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

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
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
                    continue;
                }
                char c = board[i][j];
                String s = String.format("%c", c);
                Matcher m = patternSymbol.matcher(s);
                if (m.matches()) {
                    return true;
                }
            }
        }
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

    private static int findNumberInSchematic(int x, int y, List<SchematicNumber> snList) {
        for (var sn : snList) {
            if (sn.y == y && x >= sn.xFrom && x <= sn.xTo) {
                return sn.number;
            }
        }
        return 0;
    }

    private static ImmutablePair<Integer, Integer> getRatiosConnectedToGear(Gear g, char[][] schematic,
            List<SchematicNumber> snList) {
        boolean foundFirst = false;
        int first = 0;
        Set<ImmutablePair<Integer, Integer>> skipCoords = new HashSet<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                var coords = new ImmutablePair<Integer, Integer>(i, j);
                if (skipCoords.contains(coords)) {
                    // System.out.println("skippedy skip " + skipCoords.size());
                    skipCoords.remove(coords);
                    continue;
                }

                int newX = g.x + i;
                int newY = g.y + j;
                if ((i == 0 && j == 0) || newX < 0 || newX >= schematic.length || newY < 0
                        || newY >= schematic[0].length) {
                    continue;
                }
                char c = schematic[newX][newY];
                Matcher m = patternNumber.matcher(String.format("%c", c));
                if (m.find()) {
                    if (!foundFirst) {
                        foundFirst = true;
                        // definitely skip the next one
                        skipCoords.add(new ImmutablePair<Integer, Integer>(i + 1, j));
                        // skip the one after if they're both numbers (AND not in the line of the gear)
                        int skip2x = newX + 2;
                        if (j != 0 && skip2x < schematic.length) {
                            char c1 = schematic[skip2x - 1][newY];
                            char c2 = schematic[skip2x][newY];
                            if (patternNumber.matcher(String.format("%c", c1)).find()
                                    && patternNumber.matcher(String.format("%c", c2)).find()) {
                                skipCoords.add(new ImmutablePair<Integer, Integer>(i + 2, j));
                            }
                        }
                        first = findNumberInSchematic(newX, newY, snList);
                    } else {
                        int second = findNumberInSchematic(newX, newY, snList);
                        // System.out.println("First = " + first + " / second = " + second);
                        return new ImmutablePair<Integer, Integer>(first, second);
                    }
                }
            }
        }
        return new ImmutablePair<Integer, Integer>(0, 0);
    }

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
        for (var g : gearList) {
            var ratios = getRatiosConnectedToGear(g, schematic, snList);
            ctr += (ratios.left * ratios.right);
        }
        return ctr;
    }
}