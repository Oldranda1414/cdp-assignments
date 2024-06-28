package it.unibo.rmisudoku.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.unibo.rmisudoku.model.SudokuList;

public class Main {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 10000);
            SudokuList sudokuList = (SudokuList) registry.lookup("sudokuList");
            ClientImpl client = new ClientImpl(sudokuList, args[0]);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    sudokuList.unregisterClient(client);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
