package it.unibo.rmisudoku.model;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.rmisudoku.utils.Coords;
import it.unibo.rmisudoku.utils.SudokuGenerator;

public class CollaborativeSudokuImpl implements CollaborativeSudoku {
    private Map<String, Coords> highlightedCells;
    private List<List<CellState>> sudoku;

    public CollaborativeSudokuImpl() {
        this.sudoku = SudokuGenerator.generateSudoku();
        this.highlightedCells = new HashMap<>();
    }

    @Override
    public void setNumber(Coords cell, int number) throws RemoteException {
        this.sudoku.get(cell.getX()).get(cell.getY()).setNumber(number);
    }

    @Override
    public int getNumber(Coords cell) throws RemoteException {
        return this.sudoku.get(cell.getX()).get(cell.getY()).getNumber();
    }

    @Override
    public void highlightCell(Coords cell, String playerName) throws RemoteException {
        this.highlightedCells.put(playerName, cell);
    }

    @Override
    public Map<String, Coords> getHighlightedCells() throws RemoteException {
        return this.highlightedCells;
    }
}
