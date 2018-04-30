package ru.ezhov.graph.gui.detailinfopanel;

import ru.ezhov.graph.gui.components.TabHeader;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import ru.ezhov.graph.gui.components.SyntaxTextAreaWithSearchPanel;
import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.graphdetailpanel.GraphDetailPanel;
import ru.ezhov.graph.util.PercentScreenDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 20.04.2018.
 */
public class DetailInfoPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(DetailInfoPanel.class.getName());

    private GraphObjectGui graphObjectGui;
    private JLabel labelId;
    private JButton buttonClose;
    private JTextField textFieldId;
    private JLabel labelUse;
    private JList listUse;
    private JLabel labelUseIn;
    private JList listUseIn;
    private SyntaxTextAreaWithSearchPanel syntaxTextAreaWithSearchPanel;

    private JTabbedPane tabbedPane;

    public DetailInfoPanel(GraphObjectGui graphObjectGui) {
        this.graphObjectGui = graphObjectGui;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        labelId = new JLabel("ID");
        buttonClose = new JButton("Закрыть");
        textFieldId = new JTextField();
        textFieldId.setText(graphObjectGui.id());
        labelUseIn = new JLabel("Используется в:");
        listUseIn = new JList(new DetailPanelListModel(graphObjectGui.parents()));
        listUseIn.setCellRenderer(new DetailPanelListRender());
        labelUse = new JLabel("Использует:");
        listUse = new JList(new DetailPanelListModel(graphObjectGui.children()));
        listUse.setCellRenderer(new DetailPanelListRender());
        tabbedPane = new JTabbedPane();

        syntaxTextAreaWithSearchPanel = new SyntaxTextAreaWithSearchPanel(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList list = (JList) e.getSource();
                    GraphObjectGui graphObjectGui = (GraphObjectGui) list.getSelectedValue();
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Просмотр информации о: " + graphObjectGui.id());
                    dialog.setIconImage(new ImageIcon(this.getClass().getResource("/graph_16x16.png")).getImage());
                    dialog.setModal(true);
                    dialog.add(new DetailInfoPanel(graphObjectGui));
                    dialog.setSize(new PercentScreenDimension(70).dimension());
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                }
            }
        };

        listUseIn.addMouseListener(mouseAdapter);
        listUse.addMouseListener(mouseAdapter);

        JPanel panelId = panelId();
        JPanel panelUseIn = panelUseIn();
        JPanel panelUse = panelUse();
        JPanel panelText = panelText();

        JPanel panelTopGroup = new JPanel(new BorderLayout());
        panelTopGroup.add(panelId, BorderLayout.NORTH);

        JPanel panelUseCommon = new JPanel();

        if (!graphObjectGui.parents().isEmpty() & !graphObjectGui.children().isEmpty()) {
            panelUseCommon.setLayout(new GridLayout(1, 2));
            panelUseCommon.add(panelUseIn);
            panelUseCommon.add(panelUse);
            panelTopGroup.add(panelUseCommon, BorderLayout.CENTER);
        } else if (graphObjectGui.parents().isEmpty() & !graphObjectGui.children().isEmpty()) {
            panelUseCommon.setLayout(new GridLayout(1, 1));
            panelUseCommon.add(panelUse);
            panelTopGroup.add(panelUseCommon, BorderLayout.CENTER);
        } else if (!graphObjectGui.parents().isEmpty() & graphObjectGui.children().isEmpty()) {
            panelUseCommon.setLayout(new GridLayout(1, 1));
            panelUseCommon.add(panelUseIn);
            panelTopGroup.add(panelUseCommon, BorderLayout.CENTER);
        } else {
            //NOT USE
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(panelTopGroup);

        if (graphObjectGui.parents().isEmpty() && graphObjectGui.children().isEmpty()) {
            //NOT ADD
        } else {
            tabbedPane.addTab("ГРАФ: " + graphObjectGui.id(), new GraphDetailPanel(graphObjectGui));
            tabbedPane.setTabComponentAt(0, new TabHeader("ГРАФ: " + graphObjectGui.id(), tabbedPane, false));
        }
        tabbedPane.addTab("Текст: " + graphObjectGui.id(), panelText);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new TabHeader("Текст: " + graphObjectGui.id(), tabbedPane, false));

        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerLocation(0.3);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel panelId() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(labelId, BorderLayout.WEST);
        panel.add(textFieldId, BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelUse() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(labelUse, BorderLayout.NORTH);
        panel.add(new JScrollPane(listUse), BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelUseIn() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(labelUseIn, BorderLayout.NORTH);
        panel.add(new JScrollPane(listUseIn), BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelText() {
        JPanel panel = new JPanel(new BorderLayout());
        try {
            syntaxTextAreaWithSearchPanel.text(graphObjectGui.text());
        } catch (Exception e) {
            e.printStackTrace();
            syntaxTextAreaWithSearchPanel.text("Не удалось получить текст скрипта");
        }
        panel.add(syntaxTextAreaWithSearchPanel, BorderLayout.CENTER);
        return panel;
    }
}