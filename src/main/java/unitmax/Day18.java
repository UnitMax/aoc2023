package unitmax;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day18 {

    static Pattern digPattern = Pattern.compile("([RDUL]) ([0-9]+) \\(");

    static class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Coordinate other = (Coordinate) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }

    }

    public static long part1(String[] input) {
        long result = 0;

        // Create grid
        Stack<Coordinate> digStack = new Stack<>();
        digStack.push(new Coordinate(0, 0));
        var minCoord = new Coordinate(0, 0);
        var maxCoord = new Coordinate(0, 0);
        for (var s : input) {
            var m = digPattern.matcher(s);
            m.find();
            var direction = m.group(1).charAt(0);
            var dirCount = Integer.parseInt(m.group(2));
            System.out.printf("Direction %s dirCount %d%n", direction, dirCount);

            var currentCoord = digStack.peek();
            for (int i = 1; i <= dirCount; i++) {
                switch (direction) {
                    case 'R':
                        digStack.push(new Coordinate(currentCoord.x + i, currentCoord.y));
                        break;
                    case 'L':
                        digStack.push(new Coordinate(currentCoord.x - i, currentCoord.y));
                        break;
                    case 'D':
                        digStack.push(new Coordinate(currentCoord.x, currentCoord.y + i));
                        break;
                    case 'U':
                        digStack.push(new Coordinate(currentCoord.x, currentCoord.y - i));
                        break;
                    default:
                        throw new RuntimeException("How did we get here?");
                }
                var newCoord = digStack.peek();
                if (newCoord.x > maxCoord.x) {
                    maxCoord = new Coordinate(newCoord.x, maxCoord.y);
                }
                if (newCoord.y > maxCoord.y) {
                    maxCoord = new Coordinate(maxCoord.x, newCoord.y);
                }
                if (newCoord.x < minCoord.x) {
                    minCoord = new Coordinate(newCoord.x, minCoord.y);
                }
                if (newCoord.y < minCoord.y) {
                    minCoord = new Coordinate(minCoord.x, newCoord.y);
                }
            }
        }
        var gridSize = new Coordinate(maxCoord.x - minCoord.x, maxCoord.y - minCoord.y);
        System.out.println("Grid size = " + gridSize.x + " / " + gridSize.y);
        boolean[][] grid = new boolean[gridSize.y + 1][gridSize.x + 1];
        while (!digStack.isEmpty()) {
            var hole = digStack.pop();
            grid[hole.y + Math.abs(minCoord.y)][hole.x + Math.abs(minCoord.x)] = true;
        }

        // Print grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] ? '#' : '.');
            }
            System.out.println();
        }

        // Flood fill
        var startingLocation = new Coordinate(grid[0].length / 2, grid.length / 2);
        floodFill(grid, startingLocation.x, startingLocation.y);

        System.out.println("********");
        // Print grid again
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] ? '#' : '.');
                if (grid[i][j]) {
                    result++;
                }
            }
            System.out.println();
        }
        return result;
    }

    private static void floodFill(boolean[][] grid, int x, int y) {
        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> coords = new LinkedList<>();
        coords.add(new Coordinate(x, y));

        while (!coords.isEmpty()) {
            var c = coords.poll();
            if (c.x < 0 || c.y < 0 || c.x >= grid[0].length || c.y >= grid.length) {
                continue;
            }
            if (visited.contains(c)) {
                continue;
            }
            visited.add(c);

            if (!grid[c.y][c.x]) {
                grid[c.y][c.x] = true;
                coords.add(new Coordinate(c.x + 1, c.y));
                coords.add(new Coordinate(c.x - 1, c.y));
                coords.add(new Coordinate(c.x, c.y + 1));
                coords.add(new Coordinate(c.x, c.y - 1));
            }
        }
    }

    public static ImmutablePair<Long, Long> lavaVolume(String[] input) {
        long ctr1 = part1(input);
        long ctr2 = 0;
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
