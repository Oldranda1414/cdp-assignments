package it.unibo.rmisudoku.utils;

import java.awt.Color;
import java.util.List;
import java.util.Iterator;

public class Colors implements Iterator<Color> {
    private Iterator<Color> knownColors;

    public Colors() {
        this.knownColors = List.of(
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.CYAN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.PINK
        ).iterator();
    }

    @Override
    public boolean hasNext() {
        return this.knownColors.hasNext();
    }

    @Override
    public Color next() {
        return this.knownColors.next();
    }
    
}
