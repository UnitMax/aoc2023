package unitmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day17 {

    static class CityBlock {
        public int heatValue;

        public int x;
        public int y;

        public int directionRow;
        public int directionColumn;

        public int remainingSteps;

        public CityBlock(int heatValue, int x, int y, int directionRow, int directionColumn, int remainingSteps) {
            this.heatValue = heatValue;
            this.x = x;
            this.y = y;
            this.directionRow = directionRow;
            this.directionColumn = directionColumn;
            this.remainingSteps = remainingSteps;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + heatValue;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + directionRow;
            result = prime * result + directionColumn;
            result = prime * result + remainingSteps;
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
            CityBlock other = (CityBlock) obj;
            if (heatValue != other.heatValue)
                return false;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (directionRow != other.directionRow)
                return false;
            if (directionColumn != other.directionColumn)
                return false;
            if (remainingSteps != other.remainingSteps)
                return false;
            return true;
        }

    }

    // Modified Dijkstra
    public static long findShortestPath(String[] input, boolean part2) {
        List<ImmutablePair<Integer, Integer>> directions = new ArrayList<>();
        directions.add(new ImmutablePair<Integer, Integer>(0, 1));
        directions.add(new ImmutablePair<Integer, Integer>(1, 0));
        directions.add(new ImmutablePair<Integer, Integer>(0, -1));
        directions.add(new ImmutablePair<Integer, Integer>(-1, 0));

        long result = 0;

        // Set up grid
        Integer[][] city = new Integer[input.length][input[0].strip().length()];
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[0].length; j++) {
                city[i][j] = Integer.parseInt(String.valueOf(input[i].charAt(j)));
            }
        }

        PriorityQueue<CityBlock> cbpq = new PriorityQueue<>((block1, block2) -> {
            int hlComparison = Integer.compare(block1.heatValue, block2.heatValue);
            if (hlComparison != 0) {
                return hlComparison;
            }
            int xComparison = Integer.compare(block1.x, block2.x);
            if (xComparison != 0) {
                return xComparison;
            }
            int yComparison = Integer.compare(block1.x, block2.y);
            if (yComparison != 0) {
                return yComparison;
            }
            int drComparison = Integer.compare(block1.directionRow, block2.directionRow);
            if (drComparison != 0) {
                return drComparison;
            }
            int dcComparison = Integer.compare(block1.directionColumn, block2.directionColumn);
            if (dcComparison != 0) {
                return dcComparison;
            }
            int fuelComparison = Integer.compare(block1.remainingSteps, block2.remainingSteps);
            if (fuelComparison != 0) {
                return fuelComparison;
            }
            return fuelComparison;

        });

        if (part2) {
            cbpq.add(new CityBlock(0, 0, 0, 1, 0, 0));
            cbpq.add(new CityBlock(0, 0, 0, 0, 1, 0));
        } else {
            cbpq.add(new CityBlock(0, 0, 0, 0, 0, 0));
        }

        Set<List<Integer>> seenBlocks = new HashSet<>();
        int maximumSteps = part2 ? 10 : 3;
        while (!cbpq.isEmpty()) {
            var block = cbpq.poll();

            if (block.x == city[0].length - 1 && block.y == city.length - 1) {
                result = block.heatValue;
                break;
            }

            var blockNoHeatLoss = Arrays.asList(block.x, block.y, block.directionRow, block.directionColumn,
                    block.remainingSteps);
            if (seenBlocks.contains(blockNoHeatLoss)) {
                continue;
            }
            seenBlocks.add(blockNoHeatLoss);

            if (block.remainingSteps < maximumSteps && !(block.directionRow == 0 && block.directionColumn == 0)) {
                var nextX = block.x + block.directionRow;
                var nextY = block.y + block.directionColumn;
                if (nextX >= 0 && nextX < city[0].length && nextY >= 0 && nextY < city.length) {
                    cbpq.add(new CityBlock(block.heatValue + city[nextY][nextX], nextX, nextY, block.directionRow,
                            block.directionColumn, block.remainingSteps + 1));
                }
            }

            if (part2 && block.remainingSteps < 4) {
                continue;
            }

            for (var direction : directions) {
                if (!(direction.getLeft() == block.directionRow && direction.getRight() == block.directionColumn)
                        && !(direction.getLeft() == -block.directionRow
                                && direction.getRight() == -block.directionColumn)) {
                    var nextX = block.x + direction.getLeft();
                    var nextY = block.y + direction.getRight();
                    if (nextX >= 0 && nextX < city[0].length && nextY >= 0 && nextY < city.length) {
                        cbpq.add(new CityBlock(block.heatValue + city[nextY][nextX], nextX, nextY, direction.getLeft(),
                                direction.getRight(), 1));
                    }
                }
            }
        }

        return result;
    }

    public static ImmutablePair<Long, Long> shortestPath(String[] input) {
        long ctr1 = findShortestPath(input, false);
        long ctr2 = findShortestPath(input, true);
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
