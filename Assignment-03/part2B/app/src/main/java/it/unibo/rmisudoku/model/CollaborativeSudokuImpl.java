package it.unibo.rmisudoku.model;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.rmisudoku.client.Client;
import it.unibo.rmisudoku.utils.Coords;
import it.unibo.rmisudoku.utils.Grid;
import it.unibo.rmisudoku.utils.SudokuGenerator;

public class CollaborativeSudokuImpl implements CollaborativeSudoku {
    private Map<String, Coords> highlightedCells;
    private Grid<CellState> sudoku;
    private Grid<CellState> solution;
    private List<Client> clients;

    public CollaborativeSudokuImpl() throws IllegalStateException {
        this.clients = new LinkedList<>();
        SudokuGenerator sudokuGenerator;
        try {
            sudokuGenerator = new SudokuGenerator();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("Failed while generating sudoku");
        }
        this.sudoku = sudokuGenerator.getSudoku();
        this.solution = sudokuGenerator.getSolution();
        this.highlightedCells = new HashMap<>();
    }

    @Override
    public synchronized void setNumber(Coords cell, int number)
            throws RemoteException {
        this.sudoku.getElement(new Coords(cell.getX(), cell.getY()))
            .setNumber(number);
        this.notifyClients();
    }

    @Override
    public synchronized int getNumber(Coords cell) throws RemoteException {
        return this.sudoku.getElement(new Coords(cell.getX(), cell.getY()))
            .getNumber();
    }

    @Override
    public synchronized boolean isCellModifiable(Coords cell)
            throws RemoteException {
        return this.sudoku.getElement(new Coords(cell.getX(), cell.getY()))
            .isModifiable();
    }

    @Override
    public synchronized int getSolutionNumber(Coords cell)
            throws RemoteException {
        return this.solution.getElement(new Coords(cell.getX(), cell.getY()))
            .getNumber();
    }

    @Override
    public synchronized void highlightCell(Coords cell, String playerUsername)
            throws RemoteException {
        this.highlightedCells.put(playerUsername, cell);
        this.notifyClients();
    }

    @Override
    public synchronized Map<String, Coords> getHighlightedCells()
            throws RemoteException {
        return this.highlightedCells;
    }

    @Override
    public synchronized void registerClient(Client client)
            throws RemoteException {
        clients.add(client);
        this.notifyClients();
    }

    @Override
    public synchronized void unregisterClient(Client client)
            throws RemoteException {
        clients.remove(client);
        this.highlightedCells.remove(client.getUsername());
        this.notifyClients();
    }

    private void notifyClients() throws RemoteException {
        for (Client client : clients) {
            client.updateClient();
        }
    }
}
