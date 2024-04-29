package macropart2.reactiveprogramming;

public class CLIView implements Listener {

    @Override
    public void onUpdate(final Results partialResults) {
        System.out.println("Partial results -> Occurrencies: "
            + partialResults.getOccurrencies());
    }

}
