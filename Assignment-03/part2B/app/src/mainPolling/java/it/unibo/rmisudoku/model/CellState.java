package it.unibo.rmisudoku.model;

import java.io.Serializable;

public class CellState implements Serializable {
    private int number;

    public CellState() {
        this.number = 0;
    }

    private void checkNumber(final int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
    }

    public CellState(final int number) {
        if (number != 0) {
            this.checkNumber(number);
        }
        this.number = number;
    }

    public boolean isEmpty() {
        return this.number == 0;
    }

    public void empty() {
        this.number = 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.checkNumber(number);
        this.number = number;
    }

    @Override
    public String toString() {
        return "CellState [number=" + number + "]";
    }
}
