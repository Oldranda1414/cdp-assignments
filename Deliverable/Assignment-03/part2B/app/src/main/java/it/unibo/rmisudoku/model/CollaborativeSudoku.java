package it.unibo.rmisudoku.model;

import java.rmi.RemoteException;
import java.util.Map;

import it.unibo.rmisudoku.client.Client;
import it.unibo.rmisudoku.utils.Coords;

public interface CollaborativeSudoku extends Sudoku {
    void highlightCell(Coords cell, String playerUsername) throws RemoteException;
    Map<String, Coords> getHighlightedCells() throws RemoteException;
    void registerClient(Client client) throws RemoteException;
    void unregisterClient(Client client) throws RemoteException;
}
