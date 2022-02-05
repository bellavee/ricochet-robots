package board;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction getOpposite() {
        switch (this) {
        case UP:
            return DOWN;
        case DOWN:
            return UP;
        case LEFT:
            return RIGHT;
        case RIGHT:
            return LEFT;
        }
        return null;
    }

}
