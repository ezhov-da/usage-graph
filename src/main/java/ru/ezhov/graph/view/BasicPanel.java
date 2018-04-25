package ru.ezhov.graph.view;

import ru.ezhov.graph.script.Scripts;
import ru.ezhov.graph.script.ScriptsFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 25.04.2018.
 */
public class BasicPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(BasicPanel.class.getName());

    private JButton buttonLoad;

    private DirectoryPanel directoryPanel;
    private JPanel panelStub;
    private JPanel panelCenter;

    public BasicPanel() {
        setLayout(new BorderLayout());
        directoryPanel = new DirectoryPanel();
        panelStub = new JPanel(new BorderLayout());
        JLabel labelStub = new JLabel("Построитель графов");
        labelStub.setHorizontalAlignment(SwingConstants.CENTER);
        panelStub.add(labelStub, BorderLayout.CENTER);
        add(directoryPanel, BorderLayout.NORTH);
        panelCenter = panelStub;
        add(panelCenter, BorderLayout.CENTER);
    }


    private class DirectoryPanel extends JPanel {

        private JLabel label;
        private JTextField textField;
        private JButton buttonOpenDir;
        private JFileChooser fileChooser = new JFileChooser("E:\\MDM\\branches\\trunk\\Scripts\\scripts");

        public DirectoryPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            label = new JLabel("Путь для загрузки:");
            textField = new JTextField();
            textField.setText("E:\\MDM\\branches\\trunk\\Scripts\\scripts");
            buttonOpenDir = new JButton("Выбрать");
            fileChooser.setDialogTitle("Выберите папку со скриптами");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            buttonOpenDir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int answer = fileChooser.showOpenDialog(DirectoryPanel.this);
                    if (answer == JFileChooser.APPROVE_OPTION) {
                        textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
            });

            buttonLoad = new JButton("Загрузить скрипты");

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.WEST);
            panel.add(textField, BorderLayout.CENTER);

            JPanel panelEast = new JPanel(new BorderLayout());
            panelEast.add(buttonOpenDir, BorderLayout.WEST);
            panelEast.add(buttonLoad, BorderLayout.CENTER);

            add(panel, BorderLayout.CENTER);
            add(panelEast, BorderLayout.EAST);

            buttonLoad.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    load();
                }
            });
        }

        private void load() {
            try {
                final Scripts scripts = ScriptsFactory.fromFile(new File(textField.getText()));
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        BasicPanel.this.remove(panelCenter);
                        panelCenter = new GraphPanel(scripts);
                        BasicPanel.this.add(panelCenter, BorderLayout.CENTER);
                        BasicPanel.this.revalidate();
                    }
                });
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
