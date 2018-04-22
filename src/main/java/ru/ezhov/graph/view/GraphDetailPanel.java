package ru.ezhov.graph.view;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 20.04.2018.
 */
public class GraphDetailPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(GraphDetailPanel.class.getName());

    private ScriptViewDetail scriptViewDetail;
    private JLabel labelId;
    private JTextField textFieldId;
    private JLabel labelUse;
    private JList listUse;
    private JLabel labelUseIn;
    private JList listUseIn;
    private JLabel labelText;
    private RSyntaxTextArea textAreaText;


    public GraphDetailPanel(ScriptViewDetail scriptViewDetail) {
        this.scriptViewDetail = scriptViewDetail;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        labelId = new JLabel("ID");
        textFieldId = new JTextField();
        textFieldId.setText(scriptViewDetail.id());
        labelUseIn = new JLabel("Используется в:");
        listUseIn = new JList(new DetailPanelListModel(scriptViewDetail.parents()));
        listUseIn.setCellRenderer(new DetailPanelListRender());
        labelUse = new JLabel("Использует:");
        listUse = new JList(new DetailPanelListModel(scriptViewDetail.children()));
        listUse.setCellRenderer(new DetailPanelListRender());
        labelText = new JLabel("Текст");

        textAreaText = new RSyntaxTextArea(20, 60);
        textAreaText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        textAreaText.setCodeFoldingEnabled(true);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList list = (JList) e.getSource();
                    ScriptView view = (ScriptView) list.getSelectedValue();
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Просмотр краткой информации о скрипте");
                    dialog.setModal(true);
                    dialog.add(new ScriptViewPanel(view));
                    dialog.setSize(600, 600);
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

        if (!scriptViewDetail.parents().isEmpty() & !scriptViewDetail.children().isEmpty()) {
            panelUseCommon.setLayout(new GridLayout(1, 2));
            panelUseCommon.add(panelUseIn);
            panelUseCommon.add(panelUse);
            panelTopGroup.add(panelUseCommon, BorderLayout.CENTER);
        } else if (scriptViewDetail.parents().isEmpty() & !scriptViewDetail.children().isEmpty()) {
            panelUseCommon.setLayout(new GridLayout(1, 1));
            panelUseCommon.add(panelUse);
            panelTopGroup.add(panelUseCommon, BorderLayout.CENTER);
        } else if (!scriptViewDetail.parents().isEmpty() & scriptViewDetail.children().isEmpty()) {
            panelUseCommon.setLayout(new GridLayout(1, 1));
            panelUseCommon.add(panelUseIn);
            panelTopGroup.add(panelUseCommon, BorderLayout.CENTER);
        } else {
            //NOT USE
        }
        add(panelTopGroup, BorderLayout.NORTH);
        add(panelText, BorderLayout.CENTER);
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
        panel.add(labelText, BorderLayout.NORTH);
        labelText.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            textAreaText.setText(scriptViewDetail.text());
        } catch (Exception e) {
            e.printStackTrace();
            textAreaText.setText("Не удалось получить текст скрипта");
        }
        panel.add(new RTextScrollPane(textAreaText), BorderLayout.CENTER);
        return panel;
    }
}