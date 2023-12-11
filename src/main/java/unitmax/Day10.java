package unitmax;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day10 {

    static class Coord {
        public long x;
        public long y;

        public Coord(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    /*
     * 123
     * 8x4
     * 765
     */
    enum Direction {
        NW,
        N,
        NE,
        E,
        SE,
        S,
        SW,
        W,
        NONE
    }

    static Map<Character, ImmutablePair<Direction, Direction>> pipeMap;
    static {
        pipeMap = new HashMap<>();
        pipeMap.put('|', new ImmutablePair<>(Direction.S, Direction.N));
        pipeMap.put('-', new ImmutablePair<>(Direction.E, Direction.W));
        pipeMap.put('L', new ImmutablePair<>(Direction.N, Direction.E));
        pipeMap.put('J', new ImmutablePair<>(Direction.N, Direction.W));
        pipeMap.put('7', new ImmutablePair<>(Direction.S, Direction.W));
        pipeMap.put('F', new ImmutablePair<>(Direction.S, Direction.E));
        // pipeMap.put('|', new ImmutablePair<>(Direction.S, Direction.N));
        // pipeMap.put('-', new ImmutablePair<>(Direction.E, Direction.W));
        // pipeMap.put('L', new ImmutablePair<>(Direction.S, Direction.W));
        // pipeMap.put('J', new ImmutablePair<>(Direction.S, Direction.E));
        // pipeMap.put('7', new ImmutablePair<>(Direction.N, Direction.E));
        // pipeMap.put('F', new ImmutablePair<>(Direction.N, Direction.W));
    }

    static boolean isGround(Character c) {
        return c.charValue() == '.';
    }

    static boolean isStart(Character c) {
        return c.charValue() == 'S';
    }

    static class Pipe {
        public char symbol;
        private Direction entryDirection = Direction.NONE;

        public Pipe(char symbol) {
            this.symbol = symbol;
        }

        public void enter(Direction direction) {
            switch (direction) {
                case N:
                    this.entryDirection = Direction.S;
                    break;
                case S:
                    this.entryDirection = Direction.N;
                    break;
                case E:
                    this.entryDirection = Direction.W;
                    break;
                default:
                    this.entryDirection = Direction.E;
                    break;
            }
        }

        public Optional<Direction> exit() {
            if (entryDirection == Direction.NONE) {
                return Optional.empty();
            }
            var exitDirection = pipeMap.get(symbol);
            return Optional
                    .of(exitDirection.getLeft() == entryDirection ? exitDirection.getRight() : exitDirection.getLeft());
        }
    }

    private static Coord getCoordDist(Direction direction) {
        var x = 0;
        var y = -1; // N
        switch (direction) {
            case E:
                x = 1;
                y = 0;
                break;
            case S:
                x = 0;
                y = 1;
                break;
            case W:
                x = -1;
                y = 0;
                break;
            default:
                break;
        }
        return new Coord(x, y);
    }

    public static Optional<ImmutablePair<Coord, Direction>> findConnectedLocation(Pipe[][] board, Coord location) {
        var boardHeight = board.length;
        var boardWidth = board[0].length;
        List<Direction> directionList = Arrays.asList(Direction.N, Direction.E, Direction.S, Direction.W);
        List<Character> connectingNorth = Arrays.asList('|', '7', 'F'); // if direction from X where we're looking from
                                                                        // is North --> these all connect south
        List<Character> connectingEast = Arrays.asList('-', '7', 'J'); // similar for these
        List<Character> connectingWest = Arrays.asList('-', 'L', 'F');
        List<Character> connectingSouth = Arrays.asList('|', 'L', 'J');
        Map<Direction, List<Character>> connectionMapping = new HashMap<>();
        connectionMapping.put(Direction.N, connectingNorth);
        connectionMapping.put(Direction.W, connectingWest);
        connectionMapping.put(Direction.E, connectingEast);
        connectionMapping.put(Direction.S, connectingSouth);
        for (var direction : directionList) {
            Coord xy = getCoordDist(direction);
            var x = xy.x;
            var y = xy.y;
            int newX = (int) location.x + (int) x;
            int newY = (int) location.y + (int) y;
            if (newX > 0 && newX < boardWidth && newY > 0 && newY < boardHeight) {
                var newPipe = board[newY][newX];
                var c = newPipe.symbol;
                if (connectionMapping.get(direction).contains(c)) {
                    return Optional.of(new ImmutablePair<>(new Coord(x, y), direction));
                }
            }
        }
        return Optional.empty();
    }

    public static long pipeMaze(String[] input) {
        var boardWidth = input[0].strip().length();
        var boardHeight = input.length;

        // System.out.println("----- / width = " + boardWidth + " / height = " + boardHeight);

        Pipe[][] board = new Pipe[boardHeight][boardWidth];

        var startingLocation = new Coord(-1, -1);

        // init board and get starting location
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                board[y][x] = new Pipe(input[y].strip().charAt(x));
                if (isStart(board[y][x].symbol)) {
                    startingLocation = new Coord(x, y);
                }
            }
        }

        // Find first determinate location
        var connectedStart = findConnectedLocation(board, startingLocation).get();
        Direction entryDirection = connectedStart.getRight();
        Coord startDist = connectedStart.getLeft();
        Coord currentLocation = new Coord(startingLocation.x + startDist.x, startingLocation.y + startDist.y);
        Pipe currentPipe = board[(int) currentLocation.y][(int) currentLocation.x];
        currentPipe.enter(entryDirection);

        long ctr = 1;
        boolean endFound = false;
        while (!endFound) {
            // System.out.println("----");
            // System.out.println("Ctr = " + ctr);
            // System.out.println(String.format("Current pipe = %c", currentPipe.symbol));
            // System.out.println(String.format("Current location = %d, %d", currentLocation.x, currentLocation.y));
            Direction nextPipeDirection = currentPipe.exit().get();
            // System.out.println("Next pipe in direction " + nextPipeDirection);
            Coord nextDist = getCoordDist(nextPipeDirection);
            // Technically we shouldn't do any validation here because we can't go off board
            currentLocation = new Coord(currentLocation.x + nextDist.x, currentLocation.y + nextDist.y);
            Pipe nextPipe = board[(int) currentLocation.y][(int) currentLocation.x];
            nextPipe.enter(nextPipeDirection);
            if (isStart(nextPipe.symbol)) {
                endFound = true;
            }
            currentPipe = nextPipe;
            ctr++;
        }

        // for (int y = 0; y < boardHeight; y++) {
        //     for (int x = 0; x < boardWidth; x++) {
        //         System.out.print(board[y][x].symbol);
        //     }
        //     System.out.println();
        // }

        return ctr / 2;
    }
}
