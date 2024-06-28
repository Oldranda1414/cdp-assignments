package it.unibo.rmisudoku.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import it.unibo.rmisudoku.model.*;

public class Server {
    private static Map<String, Sudoku> sudokus;

    public static void main(String[] args) {
        sudokus = new HashMap<>();

        try {
            Registry registry = LocateRegistry.createRegistry(10000);

            SudokuList list = new SudokuListImpl((sudokuId) -> {
                try {
                    System.out.println("Creating sudoku " + String.valueOf(sudokuId));

                    CollaborativeSudoku sudoku = new CollaborativeSudokuImpl();
                    CollaborativeSudoku sudokuStub = (CollaborativeSudoku) UnicastRemoteObject.exportObject(sudoku, 10000);
                    registry.rebind(String.valueOf(sudokuId), sudokuStub);
                    sudokus.put(sudokuId, sudoku);
                    
                    System.out.println("Now the server contains " + String.valueOf(sudokus.size()) + " sudokus.");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            SudokuList listStub = (SudokuList) UnicastRemoteObject.exportObject(list, 10000);
            registry.rebind("sudokuList", listStub);

            // list.addSudoku("bruh");
            
            System.out.println("Done.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    } 
}
