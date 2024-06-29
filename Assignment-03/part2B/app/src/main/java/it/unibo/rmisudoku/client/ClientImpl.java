package it.unibo.rmisudoku.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import it.unibo.rmisudoku.model.CollaborativeSudoku;
import it.unibo.rmisudoku.model.SudokuList;
import it.unibo.rmisudoku.utils.Coords;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private final Registry registry;
    private final SudokuList sudokuList;
    private String username;
    private String currentSudokuId;
    private CollaborativeSudoku currentSudoku;
    private GUI gui;

    public ClientImpl(SudokuList sudokuList, final String username) throws RemoteException {
        this.registry = LocateRegistry.getRegistry("localhost", 10000);
        this.username = username;
        // try {
        //     this.sudokuList = (SudokuList) registry.lookup("sudokuList");
        // } catch (RemoteException | NotBoundException e) {
        //     e.printStackTrace();
        // }

        this.sudokuList = sudokuList;
        this.gui = new GUI(sudokuList, this);
        this.sudokuList.registerClient(this);
        this.currentSudokuId = "";
        this.currentSudoku = null;
    }

    @Override
    public void updateClient() throws RemoteException {
        this.gui.updateState();
    }
    
    @Override
    public CollaborativeSudoku getSudoku(final String sudokuId) throws RemoteException {
        if (sudokuId == null) {
            return null;
        }

        try {
            if (!sudokuId.equals(this.currentSudokuId)) {
                // Unregistering client from previous sudoku
                if (this.currentSudoku != null) {
                    this.currentSudoku.unregisterClient(this);
                }

                // Changing current sudoku
                this.currentSudokuId = sudokuId;
                this.currentSudoku = this.getRemoteSudoku(sudokuId);

                // Registering to current sudoku
                this.currentSudoku.registerClient(this);
            }
            return this.currentSudoku;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private CollaborativeSudoku getRemoteSudoku(final String sudokuId) {
        try {
            if (sudokuId != null) {
                return (CollaborativeSudoku) this.registry.lookup(sudokuId);
            }
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Grid '" + sudokuId + "' not found");
        }
        return null;
    }

    @Override
    public boolean newSudoku(final String sudokuId) throws RemoteException {
        try {
            return this.sudokuList.addSudoku(sudokuId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void highlightCell(final Coords cell) throws RemoteException {
        try {
            if (this.currentSudoku != null) {
                this.currentSudoku.highlightCell(cell, this.username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Coords> getHighlightedCells() throws RemoteException {
        try {
            if (this.currentSudoku != null) {
                return this.currentSudoku.getHighlightedCells();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUsername() throws RemoteException {
        return this.username;
    }

    @Override
    public void stop() throws RemoteException {
        if (this.currentSudoku != null) {
            this.currentSudoku.unregisterClient(this);
        }
    }
}

