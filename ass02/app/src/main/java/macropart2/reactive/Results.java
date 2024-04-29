package macropart2.reactive;

public class Results {
    private int occurrencies;
    private boolean finished;
    private final Object lock = new Object();

    public Results() {
        this.occurrencies = 0;
    }

    public boolean isFinished() {
        synchronized (lock) {
            return finished;
        }
    }

    public void finish() {
        synchronized (lock) {
            this.finished = true;
        }
    }

    public void addOccurrencies(final int amount) {
        synchronized (lock) {
            this.occurrencies += amount;
        }
    }

    public int getOccurrencies() {
        synchronized (lock) {
            return this.occurrencies;
        }
    }
}
