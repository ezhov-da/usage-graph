package ru.ezhov.graph.gui.commonapppanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ezhov.graph.gui.components.JTextFieldWithText;
import ru.ezhov.graph.gui.tabbedpanel.CommonPanel;
import ru.ezhov.graph.gui.util.ScriptsToScriptsGui;
import ru.ezhov.graph.script.Scripts;
import ru.ezhov.graph.script.ScriptsFactory;
import ru.ezhov.graph.storage.FilePathStore;
import ru.ezhov.graph.storage.Path;
import ru.ezhov.graph.storage.PathStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;

/**
 * Created by ezhov_da on 25.04.2018.
 */
public class BasicPanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(BasicPanel.class);

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
        private JFileChooser fileChooser = new JFileChooser("");
        private JComboBox comboBoxLastUser;
        private PathStore pathStore = new FilePathStore();

        public DirectoryPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            label = new JLabel("Путь для загрузки:");
            textField = new JTextFieldWithText("Выберите папку со скриптами...");
            buttonOpenDir = new JButton("Выбрать");
            fileChooser.setDialogTitle("Выберите папку со скриптами");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            comboBoxLastUser = new JComboBox<>();


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
            JPanel panelComboBox = new JPanel(new BorderLayout());
            panelComboBox.add(new JLabel("Последние загруженные скрипты: "), BorderLayout.WEST);
            panelComboBox.add(comboBoxLastUser, BorderLayout.CENTER);

            final ComboBoxModel comboBoxModel = new ComboBoxModel(pathStore);
            comboBoxLastUser.setModel(comboBoxModel);
            add(panelComboBox, BorderLayout.SOUTH);

            comboBoxLastUser.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Path path = (Path) comboBoxLastUser.getSelectedItem();
                    textField.setText(path.path());
                }
            });

            buttonLoad.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        pathStore.path(textField.getText());
                        comboBoxModel.reload();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
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
                        panelCenter = new CommonPanel(new ScriptsToScriptsGui(scripts).convert());
                        BasicPanel.this.add(panelCenter, BorderLayout.CENTER);
                        BasicPanel.this.revalidate();
                    }
                });
            } catch (Exception e1) {
                LOG.error("Ошибка загрузки файлов скриптов", e1);
            }
        }

        private class ComboBoxModel extends AbstractListModel<Path> implements MutableComboBoxModel<Path>, Serializable {

            private PathStore pathStore;
            private Path path;
            private java.util.List<Path> list;

            public ComboBoxModel(PathStore pathStore) {
                this.pathStore = pathStore;
                try {
                    list = pathStore.paths();
                } catch (Exception e) {
                    e.printStackTrace();
                    list = Collections.EMPTY_LIST;
                }
            }

            @Override
            public void addElement(Path item) {

            }

            @Override
            public void removeElement(Object obj) {

            }

            @Override
            public void insertElementAt(Path item, int index) {

            }

            @Override
            public void removeElementAt(int index) {

            }

            @Override
            public void setSelectedItem(Object anItem) {
                this.path = (Path) anItem;
            }

            @Override
            public Object getSelectedItem() {
                return path;
            }

            @Override
            public int getSize() {
                try {
                    return pathStore.paths().size();
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            public void reload() {
                try {
                    list = pathStore.paths();
                    fireContentsChanged(this, 0, list.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Path getElementAt(int index) {

                return list.get(index);
            }
        }
    }
}
