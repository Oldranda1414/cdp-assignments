package macropart2.View;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import macropart2.WordCounter;
import macropart2.WordCounterListener;

public class GUI extends JFrame implements WordCounterListener {

    private final JTextArea resultsTextArea = new JTextArea();
    private final JLabel finalResultLabel = new JLabel();
    private int totalOccurrences = 0;
    private final WordCounter wordCounter;

    public GUI(WordCounter wordCounter) {
        this.wordCounter = wordCounter;
        setTitle("Word Counter GUI");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        var urlField = new JTextField(30);
        urlField.setText("https://www.google.com");
        var depthField = getDepthField(3);
        var wordField = new JTextField(18);
        wordField.setText("hello");
        var button = new JButton("Start");
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter an URL: "));
        panel.add(urlField);
        panel.add(new JLabel("Enter a word: "));
        panel.add(wordField);
        panel.add(new JLabel("Enter the depth: "));
        panel.add(depthField);
        panel.add(button);
        add(panel);

        button.addActionListener(e -> {
            startWordCounter(panel, urlField, wordField, depthField);
            new Thread(() -> {
                this.wordCounter.join();
                this.resultsTextArea.append("-------------RESEARCH FINISHED-------------\n");
            }).start();
        });
    }

    @Override
    public void onNewWordCounted(final String url, final int count) {
        this.totalOccurrences += count;
        finalResultLabel.setText("Total occurrences: " + totalOccurrences);
        resultsTextArea.append("In " + url + " were found " + count + " occurrences of the word.\n");
        resultsTextArea.setCaretPosition(resultsTextArea.getDocument().getLength());
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    private JTextField getDepthField(final int defaultValue) {
        JTextField textField = new JTextField();
        textField.setText("3");
        textField.setColumns(5);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumberOnlyFilter());

        return textField;
    }

    private void startWordCounter(final JPanel panel, final JTextField urlField, final JTextField wordField, final JTextField depthField) {
        panel.removeAll();
        var pauseResumeButton = new JButton("Pause");
        panel.add(pauseResumeButton);
        panel.add(this.finalResultLabel);
        this.resultsTextArea.setEditable(false);
        this.resultsTextArea.setLineWrap(true);
        this.resultsTextArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(this.resultsTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll);
        pauseResumeButton.addActionListener(e2 -> {
            if (this.wordCounter.isPaused()) {
                this.wordCounter.resume();
                pauseResumeButton.setText("Pause");
            } else {
                this.wordCounter.pause();
                pauseResumeButton.setText("Resume");
            }
        });
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.revalidate();
        panel.repaint();
        this.wordCounter.addListener(this);
        this.wordCounter.start(urlField.getText(), wordField.getText(), Integer.parseInt(depthField.getText()));
    }

    private class NumberOnlyFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            } else {
                // Do nothing, if the input is not a number
            }
        }

        private boolean test(String text) {
            return text.matches("\\d*"); // Match zero or more digits
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                // Do nothing, if the input is not a number
            }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offset, offset + length);

            if (test(sb.toString())) {
                super.remove(fb, offset, length);
            } else {
                // Do nothing, if the input is not a number
            }
        }
    }
}