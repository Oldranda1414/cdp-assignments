package it.unibo.rmisudoku.client;

import javax.swing.*;

import it.unibo.rmisudoku.model.SudokuList;
import it.unibo.rmisudoku.utils.Coords;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int THICK_BORDER = 3;
    private static final int THIN_BORDER = 1;

    private SudokuList sudokuList;
    private Client client;

    private List<List<JTextField>> cells = new ArrayList<>();
    private JComboBox<String> sudokusComboBox;

    public GUI(SudokuList sudokuList, Client client) {
        this.sudokuList = sudokuList;
        this.client = client;

        setTitle("Sudoku");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        setSize(new Dimension(520, 650));

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));

        for (int row = 0; row < GRID_SIZE; row++) {
            cells.add(new ArrayList<>());
            for (int col = 0; col < GRID_SIZE; col++) {
                var textField = new JTextField();
                textField.setPreferredSize(new Dimension(55, 55));
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font("Arial", Font.BOLD, 20));

                final int xCoord = row;
                final int yCoord = col;

                textField.addFocusListener(new FocusListener() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        System.out.println("Changed highlighted cell to (" + xCoord + ", " + yCoord + ")");
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        // Does nothing
                    }
                    
                });

                textField.addKeyListener(new KeyListener() {

                    @Override
                    public void keyPressed(KeyEvent arg0) {
                        // Do nothing
                    }

                    @Override
                    public void keyReleased(KeyEvent arg0) {
                        try {
                            handleTextChange();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                        
                    @Override
                    public void keyTyped(KeyEvent arg0) {
                        // Do nothing
                    }
                    
                    private void handleTextChange() throws RemoteException {
                        System.out.println("TEXT: " + textField.getText());
                        if (textField.getText().equals("")) {
                            System.out.println("Cell emptied");
                            client.getSudoku((String) sudokusComboBox.getSelectedItem())
                                .setNumber(
                                    new Coords(xCoord, yCoord),
                                    0
                                );
                        } else {
                            try {
                                int value = Integer.parseInt(textField.getText());
                                if (value < 1 || value > 9) {
                                    throw new IllegalArgumentException("Number must be between 1 and 9.");
                                }
                                System.out.println("Legal content");
                                client.getSudoku((String) sudokusComboBox.getSelectedItem())
                                    .setNumber(
                                        new Coords(xCoord, yCoord),
                                        Integer.parseInt(textField.getText())
                                    );
                            } catch (NumberFormatException e1) {
                                System.out.println("Invalid text detected.");
                            } catch (IllegalArgumentException e2) {
                                System.out.println(e2.getMessage());
                            }
                        }
                    }
                });

                int top = THIN_BORDER;
                int left = THIN_BORDER;
                int bottom = THIN_BORDER;
                int right = THIN_BORDER;

                // Set thicker borders for subgrid boundaries
                if (row % SUBGRID_SIZE == 0) {
                    top = THICK_BORDER;
                }
                if (col % SUBGRID_SIZE == 0) {
                    left = THICK_BORDER;
                }
                if ((row + 1) % SUBGRID_SIZE == 0) {
                    bottom = THICK_BORDER;
                }
                if ((col + 1) % SUBGRID_SIZE == 0) {
                    right = THICK_BORDER;
                }
                textField.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                cells.get(row).add(textField);
                gridPanel.add(textField);
            }
        }
        add(gridPanel);

        JPanel subscribePanel = new JPanel();
        subscribePanel.setLayout(new FlowLayout());
        this.sudokusComboBox = new JComboBox<>();
        this.sudokusComboBox.setPreferredSize(new Dimension(200, 30));
        subscribePanel.add(this.sudokusComboBox);
        JButton subscribeToSudokuButton = new JButton("Subscribe");
        subscribeToSudokuButton.setPreferredSize(new Dimension(100, 30));
        subscribeToSudokuButton.addActionListener(e -> {
            updateState();
        });
        subscribePanel.add(subscribeToSudokuButton);
        add(subscribePanel);

        JPanel newSudokuPanel = new JPanel();
        newSudokuPanel.setLayout(new FlowLayout());
        JTextField newSudokuId = new JTextField();
        newSudokuId.setToolTipText("Insert sudoku ID");
        newSudokuId.setPreferredSize(new Dimension(200, 30));
        newSudokuPanel.add(newSudokuId);
        JButton newSudokuButton = new JButton("Create new");
        newSudokuButton.setPreferredSize(new Dimension(100, 30));
        newSudokuButton.addActionListener(e -> {
            try {
                if (this.sudokuList.getSudokuIds().contains(newSudokuId.getText())) {
                    System.out.println("This ID has been already taken.");
                } else {
                    this.client.newSudoku(newSudokuId.getText());
                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });
        newSudokuPanel.add(newSudokuButton);
        add(newSudokuPanel);

        setVisible(true);
        this.updateState();
    }

    // public GUI(SudokuList sudokuList, Client client) {
    //     this.sudokuList = sudokuList;
    //     this.client = client;
    //     label = new JLabel("Initial State");
    //     add(label);
    //     setSize(300, 100);
    //     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     setVisible(true);
    // }

    private boolean isItemInComboBox(final String item) {
        for (int i = 0; i < this.sudokusComboBox.getItemCount(); i++) {
            if (item.equals(this.sudokusComboBox.getItemAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void updateSudokuList() throws RemoteException {
        String selected = (String) this.sudokusComboBox.getSelectedItem();
        if (selected == null) {
            selected = "";
        }
        this.sudokusComboBox.removeAllItems();
        var sudokus = this.sudokuList.getSudokuIds();
        sudokus.forEach(s -> this.sudokusComboBox.addItem(s));
        if (this.isItemInComboBox(selected)) {
            this.sudokusComboBox.setSelectedItem(selected);
        } else {
            try {
                this.sudokusComboBox.setSelectedIndex(0);
            } catch (IllegalArgumentException e) {
                // Do nothing
            }
        }
    }

    private void updateGrid() throws RemoteException {
        var sudoku = this.client.getSudoku((String) this.sudokusComboBox.getSelectedItem());
        if (sudoku != null) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    try {
                        if (sudoku.getNumber(new Coords(i, j)) != 0) {
                            this.cells.get(i).get(j).setText(
                                String.valueOf(sudoku.getNumber(new Coords(i, j)))
                            );
                        } else {
                            this.cells.get(i).get(j).setText("");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void updateState() {
        // int newState = sudokuList.getSudokuIds().size();
        // SwingUtilities.invokeLater(() -> label.setText(String.valueOf(newState)));
        SwingUtilities.invokeLater(() -> {
            try {
                this.updateSudokuList();
                this.updateGrid();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}

