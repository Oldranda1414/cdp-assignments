package it.unibo.rmisudoku.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import it.unibo.rmisudoku.client.Client;

public interface SudokuList extends Remote {
    Set<String> getSudokuIds() throws RemoteException;
    boolean addSudoku(final String sudokuId) throws RemoteException;
    boolean removeSudoku(final String sudokuId) throws RemoteException;
    void registerClient(Client client) throws RemoteException;
    void unregisterClient(Client client) throws RemoteException;
}
