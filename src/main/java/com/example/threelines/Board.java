package com.example.threelines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    public enum Type {
        CLASSIC {
            public int getHeight() { return 5; }
            public int getWidth() { return 5; }
            public int getNumberOfTiles(Tile.Type type) {
                if (type == null) {
                    return 25;
                } else {
                    return 5;
                }
            }
            public int getTargetColumn(Tile.Type type) {
                switch (type) {
                    case YELLOW:
                        return 0;
                    case ORANGE:
                        return 2;
                    case RED:
                        return 4;
                    default:
                        throw new EnumConstantNotPresentException(Tile.Type.class, type.name());
                }
            }
            public Field.State getFieldStartState(int x, int y) {
                if ((y == 0 || y == 2 || y == 4) && (x == 1 || x == 3)) {
                    return Field.State.BLOCKED;
                } else if (x == 0 || x == 2 || x == 4) {
                    return Field.State.FILLED;
                } else {
                    return Field.State.EMPTY;
                }
            }
        };

        public abstract int getHeight();
        public abstract int getWidth();
        public abstract int getNumberOfTiles(Tile.Type type);
        public abstract int getTargetColumn(Tile.Type type);
        public abstract Field.State getFieldStartState(int x, int y);
    }

    public static class Tile {
        public enum Type { RED, ORANGE, YELLOW }

        public final Type type;
        private Field field;

        private Tile(Type type) {
            this.type = type;
        }

        public Field getField() {
            return field;
        }
    }

    public static class Field {
        public enum State { BLOCKED, EMPTY, FILLED }

        private State state;
        private Field left;
        private Field right;
        private Field up;
        private Field down;
        private Tile tile;

        private Field(State state) {
            this.state = state;
        }

        private void setNeighbors(Field left, Field right, Field up, Field down) {
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }

        public Field getLeft() { return left; }
        public Field getRight() { return right; }
        public Field getUp() { return up; }
        public Field getDown() { return down; }

        public State getState() { return state; }
        public Tile getTile() { return tile; }

        public void setTile(Tile tile) {
            this.tile = tile;
            if (tile == null) {
                state = State.EMPTY;
            } else {
                state = State.FILLED;
            }
        }
    }

    public final int height;
    public final int width;
    public final Type type;

    private final List<List<Field>> fields;

    public Board(Type type) {
        this.height = type.getHeight();
        this.width = type.getWidth();
        this.type = type;

        fields = new ArrayList<>(height);
        initFields();
        initTiles(type, fields);
    }

    private void initFields() {
        for (int y = 0; y < height; y++) {
            fields.add(new ArrayList<>(width));
            for (int x = 0; x < width; x++) {
                fields.get(y).add(new Field(type.getFieldStartState(x, y)));
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fields.get(y).get(x).setNeighbors(
                        (x > 0)?          fields.get(y).get(x - 1) : null,
                        (x < width - 1)?  fields.get(y).get(x + 1) : null,
                        (y > 0)?          fields.get(y - 1).get(x) : null,
                        (y < height - 1)? fields.get(y + 1).get(x) : null
                );
            }
        }
    }

    private void initTiles(Type boardType, List<List<Field>> fields) {
        List<Tile> tiles = new ArrayList<>(boardType.getNumberOfTiles(null));
        for (Tile.Type tileType: Tile.Type.values()) {
            for (int i = 0; i < boardType.getNumberOfTiles(tileType); i++) {
                tiles.add(new Tile(tileType));
            }
        }
        Collections.shuffle(tiles);

        for (Tile.Type tileType: Tile.Type.class.getEnumConstants()) {
            int x = boardType.getTargetColumn(tileType);
            for (int y = 0; y < boardType.getHeight(); y++) {
                if (fields.get(y).get(x).state == Field.State.FILLED) {
                    fields.get(y).get(x).tile = tiles.get(tiles.size() - 1);
                    tiles.get(tiles.size() - 1).field = fields.get(y).get(x);
                    tiles.remove(tiles.size() - 1);
                }
            }
        }
    }

    public Field get(int x, int y) {
        return fields.get(y).get(x);
    }

    public void move(Tile tile, Field field) {
        tile.field.tile = null;
        tile.field.state = Field.State.EMPTY;
        tile.field = field;
        field.tile = tile;
        field.state = Field.State.FILLED;
    }

    public boolean canMove(Tile tile, Field field) {
        return
                field != null &&
                tile != null &&
                field.state == Field.State.EMPTY && (
                        tile.getField().getLeft() == field ||
                        tile.getField().getRight() == field ||
                        tile.getField().getUp() == field ||
                        tile.getField().getDown() == field
                );
    }

    public boolean isEnd() {
        for (var tileType: Tile.Type.values()) {
            int x = type.getTargetColumn(tileType);
            for (int y = 0; y < height; y++) {
                if (!fieldFilledCorrectly(fields.get(y).get(x), tileType)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean fieldFilledCorrectly(Field field, Tile.Type targetType) {
        return
                field.state == Field.State.BLOCKED ||
                field.state == Field.State.FILLED && field.getTile().type == targetType;
    }
}
