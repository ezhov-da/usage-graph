package ru.ezhov.source.analyse.plugin.script;

import ru.ezhov.analyse.AnalyseException;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.NullAnalyzedObjects;
import ru.ezhov.analyse.script.ScriptsFactory;
import ru.ezhov.source.analyse.plugin.AbstractSourceAnalysePanel;
import ru.ezhov.source.analyse.plugin.script.storage.FilePathStore;
import ru.ezhov.source.analyse.plugin.script.storage.Path;
import ru.ezhov.source.analyse.plugin.script.storage.PathStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;

public class ScriptFileSourceAnalysePanel extends AbstractSourceAnalysePanel {
    private JLabel label;
    private JTextField textField;
    private JButton buttonOpenDir;
    private JFileChooser fileChooser = new JFileChooser("");
    private JComboBox comboBoxLastUser;
    private PathStore pathStore = new FilePathStore();
    private LastUsageComboBoxModel lastUsageComboBoxModel;

    public ScriptFileSourceAnalysePanel() {
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
                int answer = fileChooser.showOpenDialog(ScriptFileSourceAnalysePanel.this);
                if (answer == JFileChooser.APPROVE_OPTION) {
                    textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        JPanel panelEast = new JPanel(new BorderLayout());
        panelEast.add(buttonOpenDir, BorderLayout.WEST);

        add(panel, BorderLayout.CENTER);
        add(panelEast, BorderLayout.EAST);
        JPanel panelComboBox = new JPanel(new BorderLayout());
        panelComboBox.add(new JLabel("Последние загруженные скрипты: "), BorderLayout.WEST);
        panelComboBox.add(comboBoxLastUser, BorderLayout.CENTER);

        lastUsageComboBoxModel = new LastUsageComboBoxModel(pathStore);
        comboBoxLastUser.setModel(lastUsageComboBoxModel);
        add(panelComboBox, BorderLayout.SOUTH);

        comboBoxLastUser.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Path path = (Path) comboBoxLastUser.getSelectedItem();
                textField.setText(path.path());
            }
        });
    }

    @Override
    public AnalyzedObjects analyse() throws AnalyseException {
        try {
            String text = textField.getText();
            if ("".equals(text) || !new File(text).exists()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Укажите корректный путь к папке со скриптами",
                        "Путь к папке",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return NullAnalyzedObjects.NULL_ANALYZED_OBJECTS;
            } else {
                pathStore.path(text);
                lastUsageComboBoxModel.reload();
                return ScriptsFactory.fromFile(new File(text));
            }
        } catch (Exception ex) {
            throw new AnalyseException(ex);
        }
    }

    private class LastUsageComboBoxModel extends AbstractListModel<Path> implements MutableComboBoxModel<Path>, Serializable {
        private PathStore pathStore;
        private Path path;
        private java.util.List<Path> list;

        public LastUsageComboBoxModel(PathStore pathStore) {
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
