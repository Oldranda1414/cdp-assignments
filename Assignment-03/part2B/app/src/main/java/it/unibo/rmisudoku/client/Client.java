package it.unibo.rmisudoku.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.unibo.rmisudoku.model.CollaborativeSudoku;

public interface Client extends Remote {
    void updateClient() throws RemoteException;
    public CollaborativeSudoku getSudoku(final String sudokuId) throws RemoteException;
    public boolean newSudoku(final String sudokuId) throws RemoteException;
}
