package ru.ezhov.graph.view;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class ScriptViewPanel extends JPanel {
    private JLabel labelId;
    private JTextField textFieldId;

    private JLabel labelText;
    private RSyntaxTextArea textAreaText;

    private ScriptView scriptView;

    public ScriptViewPanel(ScriptView scriptView) {
        this.scriptView = scriptView;

        setLayout(new BorderLayout());

        labelId = new JLabel("ID");
        textFieldId = new JTextField(scriptView.id());

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(labelId, BorderLayout.WEST);
        panelTop.add(textFieldId, BorderLayout.CENTER);

        JPanel panelCenter = new JPanel(new BorderLayout());
        labelText = new JLabel("Текст");
        textAreaText = new RSyntaxTextArea(20, 60);
        textAreaText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        textAreaText.setCodeFoldingEnabled(true);
        panelCenter.add(labelText, BorderLayout.NORTH);
        panelCenter.add(new RTextScrollPane(textAreaText), BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        try {
            textAreaText.setText(scriptView.text());
        } catch (Exception e) {
            textAreaText.setText("Не удалось получить текст скрипта");
            e.printStackTrace();
        }
    }
}
