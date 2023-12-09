package unitmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    static class Node {
        String start;
        String left;
        String right;
        public boolean isWinning = false;
        public Node rightNode = null;
        public Node leftNode = null;

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
    }

    public static long nrOfSteps(String[] input) {
        String lrInput = input[0];
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
        for (var node : nodeMap.values()) {
            node.setLeftNode(nodeMap.get(node.goLeft()));
            node.setRightNode(nodeMap.get(node.goRight()));
        }
        boolean endFound = false;
        var lrChars = lrInput.toCharArray();
        int lrCtr = 0;
        long ctr = 0;
        while (!endFound) {
            char c = lrChars[lrCtr];
            lrCtr = (lrCtr + 1) % lrChars.length;
            Node nextNode;
            if (c == 'L') {
                nextNode = currentNode.leftNode;
            } else {
                nextNode = currentNode.rightNode;
            }
            // System.out.println("Going " + c + " from " + currentNode.start + " to " +
            // nextNode.start);
            if (ctr % 1000000 == 0) {
                System.out.println(ctr);
            }
            ctr++;
            if (nextNode.isWinning) {
                endFound = true;
            }
            currentNode = nextNode;
        }
        return ctr;
    }

}
