package ru.ezhov.graph.view;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TextAreaPanel extends JPanel {
    private JTextField textFieldSearch;
    private RSyntaxTextArea syntaxTextArea;

    public TextAreaPanel() {
        this("");
    }

    public TextAreaPanel(String text) {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        textFieldSearch = new JTextField();

        syntaxTextArea = new RSyntaxTextArea(20, 60);
        syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        syntaxTextArea.setCodeFoldingEnabled(true);

        add(textFieldSearch, BorderLayout.NORTH);
        add(new RTextScrollPane(syntaxTextArea), BorderLayout.CENTER);

        textFieldSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String searchText = textFieldSearch.getText();
                    String sourceText = syntaxTextArea.getText();
                    List<Integer> indexes = indexFind(searchText, sourceText);
                    if (!indexes.isEmpty()) {
                        //TODO: сделать адекватный поиск
                        syntaxTextArea.setCaretPosition(indexes.get(0));
                        syntaxTextArea.requestFocusInWindow();
                    }
                }
            }
        });

    }

    private List<Integer> indexFind(String search, String source) {
        List<Integer> indexes = new ArrayList<>();
        int index = source.indexOf(search);
        if (index != -1) {
            syntaxTextArea.setCaretPosition(index);
            syntaxTextArea.requestFocusInWindow();
        }
        return indexes;
    }


    public void text(String text) {
        this.syntaxTextArea.setText(text);
    }
}
