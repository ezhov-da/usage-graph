package ru.ezhov.source.analyse.plugin.java;

import ru.ezhov.analyse.AnalyseException;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.java.JavaAnalyzedObjectsFactory;
import ru.ezhov.source.analyse.plugin.AbstractSourceAnalysePanel;

import javax.swing.*;
import java.io.File;

public class JavaFileSourceAnalysePanel extends AbstractSourceAnalysePanel {
    private JTextField textFieldPath;
    private JTextField textFieldName;

    public JavaFileSourceAnalysePanel() {
        textFieldPath = new JTextField(new File("").getAbsolutePath());
        textFieldName = new JTextField("src");
        add(textFieldPath);
        add(textFieldName);
    }

    @Override
    public AnalyzedObjects analyse() throws AnalyseException {
        try {
            return JavaAnalyzedObjectsFactory.fromFile(textFieldName.getText(), new File(textFieldPath.getText()));
        } catch (Exception e) {
            throw new AnalyseException(e);
        }
    }
}
