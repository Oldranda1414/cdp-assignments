package it.unibo.rmisudoku.utils;

import java.io.Serializable;

public class Coords implements Serializable {
    private final int x;
    private final int y;

    public Coords(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

}
