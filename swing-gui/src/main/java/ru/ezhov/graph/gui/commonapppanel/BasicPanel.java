package ru.ezhov.graph.gui.commonapppanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.NullAnalyzedObjects;
import ru.ezhov.graph.gui.tabbedpanel.CommonPanel;
import ru.ezhov.graph.gui.util.AnalyseObjectToGraphObjectGui;
import ru.ezhov.graph.util.PercentScreenDimension;
import ru.ezhov.source.analyse.plugin.AbstractSourceAnalysePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ezhov_da on 25.04.2018.
 */
public class BasicPanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(BasicPanel.class);

    private JButton buttonLoad;

    private JPanel panelStub;
    private JPanel panelCenter;
    private AbstractSourceAnalysePanel abstractSourceAnalysePanel;

    public BasicPanel(AbstractSourceAnalysePanel abstractSourceAnalysePanel) {
        this.abstractSourceAnalysePanel = abstractSourceAnalysePanel;
        setLayout(new BorderLayout());
        panelStub = new JPanel(new BorderLayout());
        JLabel labelStub = new JLabel("Построитель графов");
        labelStub.setHorizontalAlignment(SwingConstants.CENTER);
        panelStub.add(labelStub, BorderLayout.CENTER);
//        add(directoryPanel, BorderLayout.NORTH);
        panelCenter = panelStub;
        buttonLoad = new JButton("Анализировать");
        buttonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    AnalyseThread worker = new AnalyseThread();
                    worker.execute();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(
                            BasicPanel.this,
                            "Ошибка загрузки данных для анализа",
                            "Ошибка загрузки",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    LOG.error("Ошибка загрузки данных для анализа", e1);
                }
            }
        });

        JPanel panelSource = new JPanel(new BorderLayout());
        panelSource.setBorder(BorderFactory.createTitledBorder("Источник анализа"));
        panelSource.add(abstractSourceAnalysePanel, BorderLayout.CENTER);
        JPanel panelButtonLoad = new JPanel();
        panelButtonLoad.add(buttonLoad);
        panelSource.add(panelButtonLoad, BorderLayout.SOUTH);

        add(panelSource, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
    }

    private AnalyzedObjects load() throws Exception {
        return abstractSourceAnalysePanel.analyse();
    }


    private class AnalyseThread extends SwingWorker {
        private JDialog dialog = new JDialog();

        public AnalyseThread() {
            JPanel panelLabel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Анализ...");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panelLabel.add(label, BorderLayout.CENTER);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(panelLabel, BorderLayout.CENTER);
            panel.add(progressBar, BorderLayout.SOUTH);
            panel.setBorder(BorderFactory.createLineBorder(Color.black));
            dialog.add(panel);
            dialog.setUndecorated(true);
            dialog.setSize(new PercentScreenDimension(20).dimension());
            dialog.setLocationRelativeTo(BasicPanel.this);
            dialog.setVisible(true);
        }

        @Override
        protected Object doInBackground() throws Exception {
            return load();
        }

        protected void done() {
            try {
                AnalyzedObjects analyzedObjects = (AnalyzedObjects) get();
                if (analyzedObjects != NullAnalyzedObjects.NULL_ANALYZED_OBJECTS) {
                    BasicPanel.this.remove(panelCenter);
                    panelCenter = new CommonPanel(new AnalyseObjectToGraphObjectGui(analyzedObjects).convert());
                    BasicPanel.this.add(panelCenter, BorderLayout.CENTER);
                    BasicPanel.this.revalidate();
                }
            } catch (Exception e) {
                LOG.error("Во время анализа произошла ошибка", e);
                JOptionPane.showMessageDialog(
                        BasicPanel.this,
                        "Произошла ошибка при анализе",
                        "Ошибка при анализе",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } finally {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }
    }
}
