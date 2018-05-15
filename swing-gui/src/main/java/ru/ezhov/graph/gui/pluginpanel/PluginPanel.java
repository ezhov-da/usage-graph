package ru.ezhov.graph.gui.pluginpanel;

import ru.ezhov.analyse.AnalyseException;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.NullAnalyzedObjects;
import ru.ezhov.source.analyse.plugin.AbstractSourceAnalysePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class PluginPanel extends AbstractSourceAnalysePanel {

    private JComboBox<String> comboBox = new JComboBox<>(
            new String[]{
                    "ru.ezhov.source.analyse.plugin.script.ScriptFileSourceAnalysePanel",
                    "ru.ezhov.source.analyse.plugin.java.JavaFileSourceAnalysePanel"
            }
    );

    private Map<String, AbstractSourceAnalysePanel> mapLoadPlugins = new HashMap<>();
    private Component componentCenterStub = new JLabel("Загрузите плагин");
    private Component componentCenter;

    private JPanel pluginPanel = new JPanel(new BorderLayout());

    private JToggleButton buttonHide = new JToggleButton(new ImageIcon(PluginPanel.class.getResource("/hide_16x16.png")));

    private boolean alreadyAddButtonHideShowPluginPanel = false;

    public PluginPanel() {
        buttonHide.setToolTipText("Скрыть панель плагина");
        setLayout(new BorderLayout());
        final JPanel panelSelect = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSelect.add(new JLabel("Выберите плагин для построения графа"));
        JButton buttonSelect = new JButton("Загрузить");
        buttonSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) comboBox.getSelectedItem();
                if (s == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Выберите плагин",
                            "Выбор плагина",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    if (!alreadyAddButtonHideShowPluginPanel) {
                        alreadyAddButtonHideShowPluginPanel = true;
                        panelSelect.add(buttonHide);
                    }

                    PluginPanel.this.pluginPanel.remove(componentCenter);
                    if (mapLoadPlugins.containsKey(s)) {
                        componentCenter = mapLoadPlugins.get(s);
                    } else {
                        try {
                            AbstractSourceAnalysePanel abstractSourceAnalysePanel = (AbstractSourceAnalysePanel) Class.forName(s).newInstance();
                            componentCenter = abstractSourceAnalysePanel;
                            mapLoadPlugins.put(s, abstractSourceAnalysePanel);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    e1.getMessage(),
                                    "Выбор плагина",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            PluginPanel.this.pluginPanel.add(componentCenter, BorderLayout.CENTER);
                            PluginPanel.this.pluginPanel.revalidate();
                            PluginPanel.this.pluginPanel.repaint();
                            return;
                        }
                    }
                    PluginPanel.this.pluginPanel.add(componentCenter, BorderLayout.CENTER);
                    PluginPanel.this.pluginPanel.revalidate();
                    PluginPanel.this.pluginPanel.repaint();
                }
            }
        });

        buttonHide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonHide.isSelected()) {
                    buttonHide.setToolTipText("Отобразить панель плагина");
                    buttonHide.setIcon(new ImageIcon(PluginPanel.class.getResource("/show_16x16.png")));
                    remove(pluginPanel);
                } else {
                    buttonHide.setToolTipText("Скрыть панель плагина");
                    buttonHide.setIcon(new ImageIcon(PluginPanel.class.getResource("/hide_16x16.png")));
                    add(pluginPanel, BorderLayout.CENTER);
                }
                revalidate();
            }
        });

        panelSelect.add(comboBox);
        panelSelect.add(buttonSelect);
        add(panelSelect, BorderLayout.NORTH);
        componentCenter = componentCenterStub;
        pluginPanel.add(componentCenter, BorderLayout.CENTER);
        add(pluginPanel, BorderLayout.CENTER);
    }

    @Override
    public AnalyzedObjects analyse() throws AnalyseException {
        if (componentCenter instanceof AbstractSourceAnalysePanel) {
            return ((AbstractSourceAnalysePanel) componentCenter).analyse();
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Выберите плагин",
                    "Выбор плагина",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return NullAnalyzedObjects.NULL_ANALYZED_OBJECTS;
        }
    }
}
