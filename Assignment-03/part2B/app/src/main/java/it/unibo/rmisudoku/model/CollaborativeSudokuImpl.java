package it.unibo.rmisudoku.model;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.rmisudoku.client.Client;
import it.unibo.rmisudoku.utils.Coords;
import it.unibo.rmisudoku.utils.SudokuGenerator;

public class CollaborativeSudokuImpl implements CollaborativeSudoku {
    private Map<String, Coords> highlightedCells;
    private List<List<CellState>> sudoku;
    private List<List<CellState>> solution;
    private List<Client> clients;

    public CollaborativeSudokuImpl() {
        this.clients = new LinkedList<>();
        var sudokuGenerator = new SudokuGenerator();
        this.sudoku = sudokuGenerator.getSudoku();
        this.solution = sudokuGenerator.getSolution();
        this.highlightedCells = new HashMap<>();
    }

    @Override
    public void setNumber(Coords cell, int number) throws RemoteException {
        synchronized (this) {
            this.sudoku.get(cell.getX()).get(cell.getY()).setNumber(number);
        }
        this.notifyClients();
    }

    @Override
    public synchronized int getNumber(Coords cell) throws RemoteException {
        return this.sudoku.get(cell.getX()).get(cell.getY()).getNumber();
    }

    @Override
    public synchronized int getSolutionNumber(Coords cell) throws RemoteException {
        return this.solution.get(cell.getX()).get(cell.getY()).getNumber();
    }

    @Override
    public void highlightCell(Coords cell, String playerName) throws RemoteException {
        synchronized (this) {
            this.highlightedCells.put(playerName, cell);
        }
        this.notifyClients();
    }

    @Override
    public synchronized Map<String, Coords> getHighlightedCells() throws RemoteException {
        return this.highlightedCells;
    }

    @Override
    public synchronized void registerClient(Client client) throws RemoteException {
        clients.add(client);
        this.notifyClients();
    }

    @Override
    public synchronized void unregisterClient(Client client) throws RemoteException {
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
