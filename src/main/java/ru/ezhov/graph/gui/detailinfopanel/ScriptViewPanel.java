package ru.ezhov.graph.gui.detailinfopanel;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import ru.ezhov.graph.gui.components.SyntaxTextAreaWithSearchPanel;
import ru.ezhov.graph.gui.domain.GraphObjectGui;

import javax.swing.*;
import java.awt.*;

public class ScriptViewPanel extends JPanel {
    private JLabel labelId;
    private JTextField textFieldId;

    private JLabel labelText;
    private SyntaxTextAreaWithSearchPanel syntaxTextAreaWithSearchPanel;

    private GraphObjectGui graphObjectGui;

    public ScriptViewPanel(GraphObjectGui graphObjectGui) {
        this.graphObjectGui = graphObjectGui;

        setLayout(new BorderLayout());

        labelId = new JLabel("ID");
        textFieldId = new JTextField(graphObjectGui.id());

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(labelId, BorderLayout.WEST);
        panelTop.add(textFieldId, BorderLayout.CENTER);

        JPanel panelCenter = new JPanel(new BorderLayout());
        labelText = new JLabel("Текст");
        syntaxTextAreaWithSearchPanel = new SyntaxTextAreaWithSearchPanel(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        panelCenter.add(labelText, BorderLayout.NORTH);
        panelCenter.add(syntaxTextAreaWithSearchPanel, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        try {
            syntaxTextAreaWithSearchPanel.text(graphObjectGui.text());
        } catch (Exception e) {
            syntaxTextAreaWithSearchPanel.text("Не удалось получить текст скрипта");
            e.printStackTrace();
        }
    }
}
