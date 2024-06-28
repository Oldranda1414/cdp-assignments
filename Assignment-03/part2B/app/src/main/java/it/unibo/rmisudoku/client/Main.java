package it.unibo.rmisudoku.client;

import java.rmi.RemoteException;

import it.unibo.rmisudoku.model.SudokuList;

public class Main {
    public static void main(String[] args) {
        try {
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry("localhost", 10000);
            SudokuList sudokuList = (SudokuList) registry.lookup("sudokuList");
            ClientImpl client = new ClientImpl(sudokuList);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    sudokuList.unregisterClient(client);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));

            System.out.println("Modifying state");
            System.out.println("State modified");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
