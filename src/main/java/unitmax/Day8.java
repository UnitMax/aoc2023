package unitmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    static class SequenceResult {
        public Node nextNode;
        public boolean canWin;
        public long nrOfStepsToWin;

        public SequenceResult(Node nextNode, boolean canWin, long nrOfStepsToWin) {
            this.nextNode = nextNode;
            this.canWin = canWin;
            this.nrOfStepsToWin = nrOfStepsToWin;
        }
    }

    static class Node {
        String start;
        String left;
        String right;
        public boolean isWinning = false;
        public Node rightNode = null;
        public Node leftNode = null;
        public Map<String, SequenceResult> sequenceMap = new HashMap<>();

        public Node(String start, String left, String right) {
            this.start = start;
            this.left = left;
            this.right = right;
            this.isWinning = "ZZZ".equals(start);
        }

        public String goRight() {
            return this.right;
        }

        public String goLeft() {
            return this.left;
        }

        public void setRightNode(Node node) {
            this.rightNode = node;
        }

        public void setLeftNode(Node node) {
            this.leftNode = node;
        }

        public void applySequence(Map<String, Node> nodeMap, String sequence) {
            var lrChars = sequence.toCharArray();
            Node currentNode = this;
            Node nextNode = null;
            boolean canWin = false;
            long ctr = 0;
            for (int i = 0; i < lrChars.length; i++) {
                var c = lrChars[i];
                if (c == 'L') {
                    nextNode = currentNode.leftNode;
                } else {
                    nextNode = currentNode.rightNode;
                }
                ctr++;
                if (nextNode.isWinning) {
                    canWin = true;
                    break;
                }
                currentNode = nextNode;
            }
            sequenceMap.put(sequence, new SequenceResult(nextNode, canWin, ctr));
        }

        public SequenceResult getResultOfSequence(String sequence) {
            return sequenceMap.get(sequence);
        }
    }

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static long lcm(Long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) {
            result = lcm(result, input[i]);
        }
        return result;
    }

    static class GhostNode {
        String start;
        String left;
        String right;
        public boolean isWinning = false;
        public GhostNode rightNode = null;
        public GhostNode leftNode = null;

        public GhostNode(String start, String left, String right) {
            this.start = start;
            this.left = left;
            this.right = right;
            this.isWinning = start.endsWith("Z");
        }

        public String goRight() {
            return this.right;
        }

        public String goLeft() {
            return this.left;
        }

        public void setRightNode(GhostNode node) {
            this.rightNode = node;
        }

        public void setLeftNode(GhostNode node) {
            this.leftNode = node;
        }

        public long applySequence(String sequence) {
            var lrChars = sequence.toCharArray();
            GhostNode currentNode = this;
            GhostNode nextNode = null;
            boolean canWin = false;
            long ctr = 0;
            int i = 0;
            while (!canWin) {
                var c = lrChars[i];
                i = (i + 1) % lrChars.length;
                if (c == 'L') {
                    nextNode = currentNode.leftNode;
                } else {
                    nextNode = currentNode.rightNode;
                }
                ctr++;
                if (nextNode.isWinning) {
                    break;
                }
                currentNode = nextNode;
            }
            return ctr;
        }
    }

    public static long nrOfSteps(String[] input) {
        String lrInput = input[0].strip();
        Map<String, Node> nodeMap = new HashMap<>();
        Node currentNode = new Node(".", ".", ".");
        for (int i = 2; i < input.length; i++) {
            var s = input[i].split("=");
            String start = s[0].strip();
            String left = s[1].split(",")[0].replace("(", "").strip();
            String right = s[1].split(",")[1].replace(")", "").strip();
            var node = new Node(start, left, right);
            nodeMap.put(start, node);
        }
        // Populate the map
        for (var node : nodeMap.values()) {
            node.setLeftNode(nodeMap.get(node.goLeft()));
            node.setRightNode(nodeMap.get(node.goRight()));
        }

        // Calculate all sequences
        for (var node : nodeMap.values()) {
            node.applySequence(nodeMap, lrInput);
        }

        // Get start node
        for (var node : nodeMap.values()) {
            if ("AAA".equals(node.start)) {
                currentNode = node;
                break;
            }
        }

        boolean endFound = false;
        long ctr = 0;
        while (!endFound) {
            var sr = currentNode.getResultOfSequence(lrInput);
            if (sr.canWin) {
                ctr += sr.nrOfStepsToWin;
                endFound = true;
                break;
            }
            if (!endFound) {
                var longestSr = currentNode.getResultOfSequence(lrInput);
                ctr += lrInput.length();
                currentNode = longestSr.nextNode;
            }
        }

        return ctr;
    }

    public static long nrOfStepsGhost(String[] input) {
        String lrInput = input[0].strip();
        Map<String, GhostNode> nodeMap = new HashMap<>();
        List<GhostNode> currentNodes = new ArrayList<>();
        for (int i = 2; i < input.length; i++) {
            var s = input[i].split("=");
            String start = s[0].strip();
            String left = s[1].split(",")[0].replace("(", "").strip();
            String right = s[1].split(",")[1].replace(")", "").strip();
            var node = new GhostNode(start, left, right);
            nodeMap.put(start, node);
        }
        // Populate the map
        for (var node : nodeMap.values()) {
            node.setLeftNode(nodeMap.get(node.goLeft()));
            node.setRightNode(nodeMap.get(node.goRight()));
        }

        // Get start node
        for (var node : nodeMap.values()) {
            if (node.start.endsWith("A")) {
                currentNodes.add(node);
            }
        }

        List<Long> winnerList = new ArrayList<>();
        for (var node : currentNodes) {
            winnerList.add(node.applySequence(lrInput));
        }
        Long[] winnerArray = new Long[winnerList.size()];
        winnerList.toArray(winnerArray);

        return lcm(winnerArray);
    }

}
