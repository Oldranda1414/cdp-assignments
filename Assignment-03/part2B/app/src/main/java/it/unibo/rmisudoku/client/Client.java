package it.unibo.rmisudoku.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import it.unibo.rmisudoku.model.CollaborativeSudoku;
import it.unibo.rmisudoku.utils.Coords;

public interface Client extends Remote {
    void updateClient() throws RemoteException;
    CollaborativeSudoku getSudoku(final String sudokuId) throws RemoteException;
    boolean newSudoku(final String sudokuId) throws RemoteException;
    void highlightCell(final Coords cell) throws RemoteException;
    Map<String, Coords> getHighlightedCells() throws RemoteException;
    String getUsername() throws RemoteException;
    void stop() throws RemoteException;
}
