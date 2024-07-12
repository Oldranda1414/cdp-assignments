package it.unibo.rmisudoku.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Grid<E> {
    private List<List<E>> grid;

    public Grid(final int x, final int y) {
        this.grid = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            List<E> l = new ArrayList<>();
            for (int j = 0; j < y; j++) {
                l.add(null);
            }
            this.grid.add(l);
        }
    }

    public void setElement(final Coords coords, final E element) {
        this.grid.get(coords.getX()).set(coords.getY(), element);
    }

    public E getElement(final Coords coords) {
        return this.grid.get(coords.getX()).get(coords.getY());
    }

    public void applyToAll(final Consumer<E> lambda) {
        this.grid.forEach(l -> l.forEach(e -> lambda.accept(e)));
    }

    public List<List<E>> dump() {
        return this.grid;
    }
}
