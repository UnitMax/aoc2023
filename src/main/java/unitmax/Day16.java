package unitmax;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Day16 {

    static enum TileType {
        EMPTY(0),
        FORWARD_MIRROR(1),
        BACKWARD_MIRROR(2),
        VERTICAL_SPLITTER(3),
        HORIZONTAL_SPLITTER(4);

        private final int value;

        private TileType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    static enum BeamDirection {
        NORTH,
        WEST,
        EAST,
        SOUTH
    }

    static class Tile {

        public TileType tileType;

        public boolean energized = false;

        public Tile(TileType tileType) {
            this.tileType = tileType;
        }

        public void energize() {
            this.energized = true;
        }

        @Override
        public String toString() {
            return String.format("%d", tileType.getValue());
        }
    }

    static class Contraption {
        // y x
        public Tile[][] tiles;

        private Set<String> visitedDirections = new HashSet<>();

        public void startPropagation() {
            propagateBeam(0, 0, BeamDirection.EAST);
        }

        public void propagateBeam(int startX, int startY, BeamDirection startDirection) {
            String directionHash = String.format("%d_%d_%s", startX, startY, startDirection);
            if (visitedDirections.contains(directionHash)) {
                return;
            }
            visitedDirections.add(directionHash);
            int x = startX;
            int y = startY;
            BeamDirection beamDirection = startDirection;
            // System.out.println("PROPAGATE_BEAM x=" + x + "/y=" + y + "/Direction=" +
            // beamDirection + "/activeBeams="
            // + nrOfActiveBeams);
            while (!(y < 0 || x < 0 || y >= this.tiles.length || x >= this.tiles[0].length)) {
                var tile = this.tiles[y][x];
                tile.energize();
                switch (tile.tileType) {
                    case EMPTY: {
                        switch (beamDirection) {
                            case EAST:
                                x++;
                                break;
                            case NORTH:
                                y--;
                                break;
                            case SOUTH:
                                y++;
                                break;
                            case WEST:
                                x--;
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                    case BACKWARD_MIRROR: {
                        switch (beamDirection) {
                            case EAST:
                                beamDirection = BeamDirection.SOUTH;
                                y++;
                                break;
                            case NORTH:
                                beamDirection = BeamDirection.WEST;
                                x--;
                                break;
                            case SOUTH:
                                beamDirection = BeamDirection.EAST;
                                x++;
                                break;
                            case WEST:
                                beamDirection = BeamDirection.NORTH;
                                y--;
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                    case FORWARD_MIRROR: {
                        switch (beamDirection) {
                            case EAST:
                                beamDirection = BeamDirection.NORTH;
                                y--;
                                break;
                            case NORTH:
                                beamDirection = BeamDirection.EAST;
                                x++;
                                break;
                            case SOUTH:
                                beamDirection = BeamDirection.WEST;
                                x--;
                                break;
                            case WEST:
                                beamDirection = BeamDirection.SOUTH;
                                y++;
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                    case HORIZONTAL_SPLITTER: {
                        switch (beamDirection) {
                            case EAST: // = Empty
                                x++;
                                break;
                            case WEST: // = Empty
                                x--;
                                break;
                            case NORTH:
                            case SOUTH:
                                propagateBeam(x - 1, y, BeamDirection.WEST);
                                propagateBeam(x + 1, y, BeamDirection.EAST);
                                return;
                            default:
                                break;
                        }
                        break;
                    }
                    case VERTICAL_SPLITTER: {
                        switch (beamDirection) {
                            case NORTH:
                                y--;
                                break;
                            case SOUTH:
                                y++;
                                break;
                            case EAST:
                            case WEST:
                                propagateBeam(x, y - 1, BeamDirection.NORTH);
                                propagateBeam(x, y + 1, BeamDirection.SOUTH);
                                return;
                            default:
                                break;
                        }
                        break;
                    }
                    default:
                        break;

                }
            }
            return;
        }

        public Contraption(String[] input) {
            this.tiles = new Tile[input.length][input[0].strip().length()];
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].strip().length(); j++) {
                    TileType tileType = TileType.EMPTY;
                    var c = input[i].charAt(j);
                    switch (c) {
                        case '|':
                            tileType = TileType.VERTICAL_SPLITTER;
                            break;
                        case '-':
                            tileType = TileType.HORIZONTAL_SPLITTER;
                            break;
                        case '/':
                            tileType = TileType.FORWARD_MIRROR;
                            break;
                        case '\\':
                            tileType = TileType.BACKWARD_MIRROR;
                            break;
                        default:
                            break;
                    }
                    this.tiles[i][j] = new Tile(tileType);
                }
            }
        }

        public void printContraption() {
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    System.out.print(tiles[i][j]);
                }
                System.out.println();
            }
        }

        public void printEnergized() {
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    System.out.print(tiles[i][j].energized ? "#" : ".");
                }
                System.out.println();
            }
        }

        public long countEnergized() {
            long ctr = 0;
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if (tiles[i][j].energized) {
                        ctr++;
                    }
                }
            }
            return ctr;
        }

    }

    public static ImmutablePair<Long, Long> day16(String[] input) {
        // yeah I got uninspired with function names
        long ctr1 = 0;
        long ctr2 = 0;
        var contraption = new Contraption(input);
        contraption.printContraption();
        contraption.startPropagation();
        System.out.println("---");
        contraption.printEnergized();
        ctr1 = contraption.countEnergized();
        return new ImmutablePair<Long, Long>(ctr1, ctr2);
    }

}
