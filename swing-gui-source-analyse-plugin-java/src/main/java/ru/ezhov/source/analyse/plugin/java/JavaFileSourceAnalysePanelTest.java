package ru.ezhov.source.analyse.plugin.java;

import javax.swing.*;

public class JavaFileSourceAnalysePanelTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Throwable ex) {
                    //
                }
                JavaFileSourceAnalysePanel javaFileSourceAnalysePanel = new JavaFileSourceAnalysePanel();
                JFrame frame = new JFrame("_________");
                frame.add(javaFileSourceAnalysePanel);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
