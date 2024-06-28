package it.unibo.rmisudoku.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.unibo.rmisudoku.client.Client;

public interface SharedObject extends Remote {
    String getState() throws RemoteException;
    void setState(String state) throws RemoteException;
    void registerClient(Client client) throws RemoteException;
    void unregisterClient(Client client) throws RemoteException;
}

