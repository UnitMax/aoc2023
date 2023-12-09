package unitmax;

import java.util.HashMap;
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
            // for (int j = 0; j < sequence.length(); j++) {
            // var subSequence = sequence.substring(j, sequence.length());
            // var lrChars = subSequence.toCharArray();
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
            // sequenceMap.put(subSequence, new SequenceResult(nextNode, canWin, ctr));
            sequenceMap.put(sequence, new SequenceResult(nextNode, canWin, ctr));
            // String s = "[" + start + "] => " + subSequence + " = " + nextNode.start + " /
            // " + canWin + " / " + ctr;
            // System.out.println(s);
            // if (canWin) {
            // System.out.println("Node " + this.start + " --> subseq " + subSequence +
            // "//steps2win=" + ctr);
            // }
            // }
        }

        public SequenceResult getResultOfSequence(String sequence) {
            // System.out.println("Getting 4 subseq = " + sequence + "/seqLenght=" +
            // sequence.length() + "/seqMapSize=" + sequenceMap.size());
            return sequenceMap.get(sequence);
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
            if (i == 2) {
                currentNode = node;
            }
            nodeMap.put(start, node);
        }
        // Populate the map
        for (var node : nodeMap.values()) {
            node.setLeftNode(nodeMap.get(node.goLeft()));
            node.setRightNode(nodeMap.get(node.goRight()));
        }

        // Calculate all sequences
        for (var node : nodeMap.values()) {
            System.out.println("Apply for " + node.start + " - " + lrInput);
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

        // boolean endFound = false;
        // var lrChars = lrInput.toCharArray();
        // int lrCtr = 0;
        // long ctr = 0;
        // while (!endFound) {
        // char c = lrChars[lrCtr];
        // lrCtr = (lrCtr + 1) % lrChars.length;
        // Node nextNode;
        // if (c == 'L') {
        // nextNode = currentNode.leftNode;
        // } else {
        // nextNode = currentNode.rightNode;
        // }
        // // System.out.println("Going " + c + " from " + currentNode.start + " to " +
        // // nextNode.start);
        // // if (ctr % 1000000 == 0) {
        // // System.out.println(ctr);
        // // }
        // ctr++;
        // if (nextNode.isWinning) {
        // endFound = true;
        // }
        // currentNode = nextNode;
        // }
        return ctr;
    }

}
