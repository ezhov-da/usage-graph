package ru.ezhov.graph;


import ru.ezhov.graph.script.Scripts;
import ru.ezhov.graph.script.ScriptsFactory;
import ru.ezhov.graph.view.GraphPanel;

import javax.swing.*;
import java.io.File;
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
            Scripts scripts = ScriptsFactory.fromFile(new File("scripts"));
            SwingUtilities.invokeLater(() -> {

                JFrame frame = new JFrame("Использование скриптов");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                frame.getContentPane().add(new GraphPanel(scripts));
                frame.setSize(1000, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}



