package ru.ezhov.graph.gui.components;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SyntaxTextAreaWithSearchPanel extends JPanel implements ActionListener {
    private RSyntaxTextArea textArea;
    private JTextField searchField;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;
    private JLabel labelInfo;

    public SyntaxTextAreaWithSearchPanel(String syntaxConstants) {
        setLayout(new BorderLayout());
        JPanel cp = new JPanel(new BorderLayout());
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(syntaxConstants);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        searchField = new JTextField(30);
        toolBar.add(searchField);
        final JButton nextButton = new JButton("Следующее совпадение");
        nextButton.setActionCommand("FindNext");
        nextButton.addActionListener(this);
        toolBar.add(nextButton);
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextButton.doClick(0);
            }
        });
        JButton prevButton = new JButton("Предыдущее совпадение");
        prevButton.setActionCommand("FindPrev");
        prevButton.addActionListener(this);
        toolBar.add(prevButton);
        regexCB = new JCheckBox("Регулярное выражение");
        toolBar.add(regexCB);
        matchCaseCB = new JCheckBox("Регистрозависимость");
        toolBar.add(matchCaseCB);
        cp.add(toolBar, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel(new BorderLayout());
        labelInfo = new JLabel();
        panelInfo.add(labelInfo, BorderLayout.CENTER);
        cp.add(panelInfo, BorderLayout.SOUTH);

        add(cp, BorderLayout.CENTER);

        textArea.requestFocusInWindow();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        boolean forward = "FindNext".equals(command);

        // Create an object defining our search parameters.
        SearchContext context = new SearchContext();
        String text = searchField.getText();
        if (text.length() == 0) {
            return;
        }
        context.setSearchFor(text);
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        context.setSearchForward(forward);
        context.setWholeWord(false);

        boolean found = SearchEngine.find(textArea, context).wasFound();
        if (!found) {
            labelInfo.setText("<html><font color=\"red\">Совпадение не найдено</font>");
        } else {
            labelInfo.setText("");
        }
    }

    public void text(String text) {
        textArea.setText(text);
    }

    public String text() {
        return textArea.getText();
    }
}
