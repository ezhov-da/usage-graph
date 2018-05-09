package ru.ezhov.graph;


import ru.ezhov.graph.gui.commonapppanel.BasicPanel;
import ru.ezhov.graph.gui.pluginpanel.PluginPanel;
import ru.ezhov.graph.util.PercentScreenDimension;
import ru.ezhov.source.analyse.plugin.AbstractSourceAnalysePanel;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 19.04.2018.
 */
public class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame("Граф использования");
                    frame.setIconImage(new ImageIcon(this.getClass().getResource("/graph_16x16.png")).getImage());
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    frame.getContentPane().add(new BasicPanel(new PluginPanel()));
                    frame.setSize(new PercentScreenDimension(90).dimension());
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



