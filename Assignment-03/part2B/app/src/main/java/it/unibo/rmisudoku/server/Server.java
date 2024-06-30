package it.unibo.rmisudoku.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import it.unibo.rmisudoku.model.SudokuList;
import it.unibo.rmisudoku.model.SudokuListImpl;
import it.unibo.rmisudoku.model.CollaborativeSudoku;
import it.unibo.rmisudoku.model.CollaborativeSudokuImpl;

public class Server {
    public static void main(String[] args) {
        Map<String, CollaborativeSudoku> sudokus = new HashMap<>();

        try {
            Registry registry = LocateRegistry.createRegistry(10000);

            SudokuList sudokuList = new SudokuListImpl((sudokuId) -> {
                try {
                    System.out.println(
                        "Creating sudoku " + String.valueOf(sudokuId)
                    );

                    var sudoku = new CollaborativeSudokuImpl();
                    var sudokuStub = (CollaborativeSudoku) UnicastRemoteObject
                        .exportObject(sudoku, 10000);
                    registry.rebind(String.valueOf(sudokuId), sudokuStub);
                    sudokus.put(sudokuId, sudoku);
                    
                    System.out.println(
                        "Now the server contains "
                        + String.valueOf(sudokus.size())
                        + " sudokus."
                    );

                    return true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return false;
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }, (sudokuId) -> {
                System.out.println(
                    "Removing sudoku " + String.valueOf(sudokuId)
                );

                if (sudokus.remove(sudokuId) != null) {
                    System.out.println(
                        "Now the server contains "
                        + String.valueOf(sudokus.size())
                        + " sudokus."
                    );
    
                    return true;
                }
                return false;

            });
            var sudokuListStub = (SudokuList) UnicastRemoteObject
                .exportObject(sudokuList, 0);
            registry.rebind("sudokuList", sudokuListStub);

            System.out.println("Server is ready.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
