package it.unibo.rmisudoku.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import it.unibo.rmisudoku.client.Client;

public class SharedObjectImpl implements SharedObject {
    private String state;
    private List<Client> clients;

    public SharedObjectImpl() throws RemoteException {
        state = "";
        clients = new ArrayList<>();
    }

    public synchronized String getState() throws RemoteException {
        return state;
    }

    public void setState(String state) throws RemoteException {
        synchronized (this) {
            this.state = state;
        }
        notifyClients();
    }

    public synchronized void registerClient(Client client) throws RemoteException {
        clients.add(client);
    }

    public synchronized void unregisterClient(Client client) throws RemoteException {
        clients.remove(client);
    }

    private void notifyClients() throws RemoteException {
        for (Client client : clients) {
            client.updateClient();
        }
    }
}

