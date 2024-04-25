package macropart2.View;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import macropart2.WordCounter;

public class GUI extends JFrame {

    public GUI(WordCounter wordCounter) {
        setTitle("Simple Frame");
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
            panel.removeAll();
            var pauseResumeButton = new JButton("Pause");
            panel.add(pauseResumeButton);
            
            pauseResumeButton.addActionListener(e2 -> {
                if (wordCounter.isPaused()) {
                    wordCounter.resume();
                    pauseResumeButton.setText("Pause");
                } else {
                    wordCounter.pause();
                    pauseResumeButton.setText("Resume");
                }
            });

            panel.revalidate();
            panel.repaint();
            
            wordCounter.start(urlField.getText(), wordField.getText(), Integer.parseInt(depthField.getText()));
        });
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