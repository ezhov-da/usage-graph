package ru.ezhov.graph.gui.components;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;

public class SyntaxTextAreaWithSearchPanelTest {
    public static void main(String[] args) {
        // Start all Swing applications on the EDT.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    String laf = UIManager.getSystemLookAndFeelClassName();
                    UIManager.setLookAndFeel(laf);
                } catch (Exception e) { /* never happens */ }
                JFrame demo = new JFrame();

                demo.setTitle("Find and Replace Demo");
                demo.add(new SyntaxTextAreaWithSearchPanel(SyntaxConstants.SYNTAX_STYLE_JAVA));
                demo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                demo.pack();
                demo.setLocationRelativeTo(null);
                demo.setVisible(true);
            }
        });
    }
}