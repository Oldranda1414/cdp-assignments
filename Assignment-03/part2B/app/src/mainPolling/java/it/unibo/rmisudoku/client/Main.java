package it.unibo.rmisudoku.client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("localhost", 10000, "utente");
        GUI gui = new GUI(client);
        gui.start();
    }
}
