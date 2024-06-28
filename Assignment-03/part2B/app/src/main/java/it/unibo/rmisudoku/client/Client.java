package it.unibo.rmisudoku.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import it.unibo.rmisudoku.model.CollaborativeSudoku;
import it.unibo.rmisudoku.utils.Coords;

public interface Client extends Remote {
    void updateClient() throws RemoteException;
    public CollaborativeSudoku getSudoku(final String sudokuId) throws RemoteException;
    public boolean newSudoku(final String sudokuId) throws RemoteException;
    public void highlightCell(final Coords cell) throws RemoteException;
    public Map<String, Coords> getHighlightedCells() throws RemoteException;
    public String getUsername() throws RemoteException;
    public void stop() throws RemoteException;
}
