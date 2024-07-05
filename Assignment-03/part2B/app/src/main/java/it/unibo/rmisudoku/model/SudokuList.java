package it.unibo.rmisudoku.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import it.unibo.rmisudoku.client.Client;

/**
 * The SudokuList is responsible of maintaining the sudokuIDs of the currently
 * existing sudokus. The sudoku list can be observed by clients, so it has
 * {@code registerClient()} and {@code unregisterClient()} methods.
 */
public interface SudokuList extends Remote {
    Set<String> getSudokuIds() throws RemoteException;
    boolean addSudoku(final String sudokuId) throws RemoteException;
    boolean removeSudoku(final String sudokuId) throws RemoteException;
    void registerClient(Client client) throws RemoteException;
    void unregisterClient(Client client) throws RemoteException;
}
