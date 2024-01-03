package unitmax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day17 {

    static class CityBlock {
        public boolean visited = false;

        public int heatValue;

        public int distance = Integer.MAX_VALUE;

        public CityBlock prev = null;

        public int x;
        public int y;

        public boolean special = false;

        public CityBlock(int heatValue, int x, int y) {
            this.heatValue = heatValue;
            this.x = x;
            this.y = y;
        }

    }

    // Dijkstra
    public static long findShortestPath(String[] input) {
        long ctr = 0;
        CityBlock[][] city = new CityBlock[input.length][input[0].strip().length()];
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[0].length; j++) {
                city[i][j] = new CityBlock(Integer.parseInt(String.valueOf(input[i].charAt(j))), j, i);
            }
        }

        // set distance to zero for initial node
        Queue<CityBlock> unvisitedNodes = new LinkedList<>();
        unvisitedNodes.add(city[0][0]);
        unvisitedNodes.peek().distance = 0;

        while (!unvisitedNodes.isEmpty()) {
            // Find lowest distance node
            int lowestDist = Integer.MAX_VALUE;
            CityBlock node = unvisitedNodes.peek();
            for (var block : unvisitedNodes) {
                if (block.distance < lowestDist) {
                    node = block;
                    lowestDist = block.distance;
                }
            }
            node.visited = true; // do we even need that?
            unvisitedNodes.remove(node);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    // neighbors
                    if (i == 0 && j == 0 || !(i == 0 || j == 0) || (node.x + j < 0) || (node.x + j >= city[0].length)
                            || (node.y + i < 0)
                            || (node.y + i >= city.length)) {
                        continue;
                    }
                    CityBlock neighbor = city[node.y + i][node.x + j];
                    if (neighbor.visited || unvisitedNodes.contains(neighbor)) {
                        continue;
                    }
                    var dist = node.distance + neighbor.heatValue;
                    if (dist < neighbor.distance) {
                        neighbor.distance = dist;
                        neighbor.prev = node;
                    }
                    unvisitedNodes.add(neighbor);
                }
            }
        }

        List<CityBlock> shortestPath = new ArrayList<>();
        CityBlock lastNode = city[city.length - 1][city[0].length - 1];
        shortestPath.add(lastNode);
        lastNode.special = true;
        ctr = lastNode.distance;
        while (true) {
            var nextNode = lastNode.prev;
            nextNode.special = true;
            shortestPath.add(nextNode);
            lastNode = nextNode;
            if (nextNode.x == 0 && nextNode.y == 0) {
                break;
            }
        }

        // print city
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[0].length; j++) {
                System.out.print(city[i][j].heatValue);
            }
            System.out.println();
        }
        System.out.println("---");
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[0].length; j++) {
                System.out.print(city[i][j].distance);
                System.out.print("|");
            }
            System.out.println();
        }
        System.out.println("---");
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[0].length; j++) {
                System.out.print(city[i][j].special ? "x" : ".");
            }
            System.out.println();
        }
        return ctr;
    }

    public static ImmutablePair<Long, Long> shortestPath(String[] input) {
        long ctr1 = findShortestPath(input);
        long ctr2 = 0;
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
