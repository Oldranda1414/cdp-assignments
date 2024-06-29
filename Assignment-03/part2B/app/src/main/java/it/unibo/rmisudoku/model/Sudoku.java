package it.unibo.rmisudoku.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.unibo.rmisudoku.utils.Coords;

public interface Sudoku extends Remote {
    void setNumber(Coords cell, int number) throws RemoteException;
    int getNumber(Coords cell) throws RemoteException;
    boolean isCellModifiable(Coords cell) throws RemoteException;
    int getSolutionNumber(Coords cell) throws RemoteException;
}
