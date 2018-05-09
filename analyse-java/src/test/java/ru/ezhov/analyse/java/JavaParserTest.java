package ru.ezhov.analyse.java;

import org.junit.Test;
import ru.ezhov.analyse.util.Folders;

import java.io.File;
import java.util.HashSet;

public class JavaParserTest {
    @Test
    public void parse() throws Exception {
        JavaParser javaParser = new JavaParser(new Folders(new HashSet<File>() {{
            add(new File("analyse-java").getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile());
        }}));
        javaParser.parse();
    }

}