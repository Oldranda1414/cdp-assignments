package it.unibo.rmisudoku.model;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SudokuListImpl implements SudokuList {
    private final Set<String> sudokuIds;
    private final Consumer<String> callback;

    public SudokuListImpl(final Consumer<String> callback) {
        this.sudokuIds = new HashSet<>();
        this.callback = callback;
    }

    public Set<String> getSudokuIds() throws RemoteException {
        return this.sudokuIds;
    }

    public boolean addSudoku(final String sudokuId) throws RemoteException {
        if (!this.sudokuIds.contains(sudokuId)) {
            this.callback.accept(sudokuId);
            this.sudokuIds.add(sudokuId);
            return true;
        }
        return false;
    }
}
