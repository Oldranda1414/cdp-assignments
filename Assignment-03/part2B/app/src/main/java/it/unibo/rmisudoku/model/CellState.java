package it.unibo.rmisudoku.model;

import java.io.Serializable;

public class CellState implements Serializable {
    private int number;
    private boolean modifiable;

    public CellState(final boolean modifiable) {
        this.number = 0;
        this.modifiable = modifiable;
    }
    
    public CellState(final int number, final boolean modifiable) {
        if (number != 0) {
            this.checkNumber(number);
        }
        this.number = number;
        this.modifiable = modifiable;
    }
            
    private void checkNumber(final int number) {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException(
                "Number must be between 1 and 9."
            );
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (number != 0) {
            this.checkNumber(number);
        }
        if (this.modifiable) {
            this.number = number;
        }
    }

    public boolean isModifiable() {
        return this.modifiable;
    }
}
