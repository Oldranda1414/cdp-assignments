package it.unibo.rmisudoku.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface SudokuList extends Remote {
    Set<String> getSudokuIds() throws RemoteException;
    boolean addSudoku(final String sudokuId) throws RemoteException;
}
