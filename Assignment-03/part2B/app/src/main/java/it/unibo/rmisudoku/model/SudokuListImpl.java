package it.unibo.rmisudoku.model;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import it.unibo.rmisudoku.client.Client;

public class SudokuListImpl implements SudokuList {
    private final Set<String> sudokuIds;
    private final Function<String, Boolean> callback;
    private List<Client> clients;

    public SudokuListImpl(final Function<String, Boolean> callback) {
        this.clients = new LinkedList<>();
        this.sudokuIds = new HashSet<>();
        this.callback = callback;
    }

    @Override
    public synchronized Set<String> getSudokuIds() throws RemoteException {
        return this.sudokuIds;
    }

    @Override
    public boolean addSudoku(final String sudokuId) throws RemoteException {
        synchronized (this) {
            // If a sudoku with that ID alreadi exists, returns false
            if (this.sudokuIds.contains(sudokuId)) {
                return false;
            }

            // If there were errors while generating the sudoku, returns false
            if (!this.callback.apply(sudokuId)) {
                return false;
            }

            // If all went well, the new ID is added to the list
            this.sudokuIds.add(sudokuId);
        }
        this.notifyClients();
        return true;
    }

    @Override
    public synchronized void registerClient(Client client)
            throws RemoteException {
        clients.add(client);
    }

    @Override
    public synchronized void unregisterClient(Client client)
            throws RemoteException {
        clients.remove(client);
    }

    private void notifyClients() throws RemoteException {
        for (Client client : clients) {
            client.updateClient();
        }
    }
}
