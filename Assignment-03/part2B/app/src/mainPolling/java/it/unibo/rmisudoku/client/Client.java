package it.unibo.rmisudoku.client;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;

import it.unibo.rmisudoku.model.CollaborativeSudoku;
import it.unibo.rmisudoku.model.SudokuList;
import it.unibo.rmisudoku.utils.Coords;

public class Client implements Serializable {
    private String ip;
    private int port;
    private String username;
    private Registry registry;
    private SudokuList sudokuList;
    private CollaborativeSudoku sudoku;

    public Client(final String ip, final int port, final String username) {
        this.ip = ip;
        this.port = port;
        this.username = username;
    }

    public void start() {
        try {
            this.registry = LocateRegistry.getRegistry(ip, port);
            this.sudokuList = (SudokuList) registry.lookup("sudokuList");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void addSudoku(final String sudokuId) {
        new Thread(() -> {
            try {
                this.sudokuList.addSudoku(sudokuId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void subscribeToGrid(final String sudokuId) {
        try {
            this.sudoku = (CollaborativeSudoku) this.registry.lookup(sudokuId);
            System.out.println("CLIENT SUBSCRIBED TO SUDOKU " + sudokuId + " (it is" + ((this.sudoku != null) ? (" not ") : (" ")) + "null)");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public CollaborativeSudoku getSudoku(final String sudokuId) {
        return this.sudoku;
    }

    public Set<String> getSudokusList() {
        try {
            return this.sudokuList.getSudokuIds();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Set.of();
    }

    public void highlightCell(final String sudokuId, final Coords cell) {
        try {
            this.sudoku.highlightCell(cell, this.username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setNumber(final String sudokuId, final Coords cell, final int number) {
        try {
            this.sudoku.setNumber(cell, number);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
