package ru.ezhov.analyse.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ezhov.analyse.AnalyzedObjects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JavaParser {
    private static final Logger LOG = LoggerFactory.getLogger(JavaParser.class);

    private static Pattern patternPackage = Pattern.compile("(?<=package\\s)(\\w+\\.?)+");
    private static Pattern patternImport = Pattern.compile("(?<=import\\s)(\\w+\\.?)+");
    private static Pattern patternClass = Pattern.compile("(?<=class\\s)(\\w+\\.?)+");
    private static Pattern patternWord = Pattern.compile("(?<=\\t)\\w+|(?<=\\()\\w+|(?<= )\\w+");

    private List<SrcRootFolder> srcRootFolders;

    JavaParser(List<SrcRootFolder> srcRootFolders) {
        this.srcRootFolders = srcRootFolders;
    }

    AnalyzedObjects parse() {
        List<JavaFile> javaFiles = new ArrayList<>();
        Map<String, Set<JavaFile>> mapWords = new HashMap<>();
        for (SrcRootFolder file : srcRootFolders) {
            recursiveJavaFileAndContainsWord(file.folder(), javaFiles, mapWords);
        }

        AnalyzedObjects analyzedObjects = buildAnalyzedObjects(javaFiles, mapWords);
//        try {
//            new ConsolePrintAnalyzedObjects(new PlainViewAnalyzedObjects(analyzedObjects).stream()).print();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return analyzedObjects;
    }

    private void recursiveJavaFileAndContainsWord(File srcRootFolder, List<JavaFile> fillJavaFiles, Map<String, Set<JavaFile>> fillContainsWord) {
        File[] files = srcRootFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".java") || pathname.isDirectory();
            }
        });

        for (File f : files) {
            if (f.isDirectory()) {
                recursiveJavaFileAndContainsWord(f, fillJavaFiles, fillContainsWord);
            } else {
                JavaFile javaFile = new JavaFile(f);
                LOG.debug(new String(new char[100]).replace("\0", "="));
                LOG.debug("Обрабатываемый файл: {}", f.getAbsolutePath());
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains("package")) {
                            Matcher matcher = patternPackage.matcher(line);
                            if (matcher.find()) {
                                String packages = matcher.group();
                                LOG.debug("Пакет: {}", packages);
                                javaFile.packageName(packages);
                            }
                        }
                        if (line.contains("import")) {
                            Matcher matcher = patternImport.matcher(line);
                            while (matcher.find()) {
                                String imports = matcher.group();
                                LOG.debug("Импорт: {}", imports);
                                javaFile.imports(imports);
                            }
                        }
                        if (line.contains("class")) {
                            Matcher matcher = patternClass.matcher(line);
                            while (matcher.find()) {
                                String className = matcher.group();
                                LOG.debug("Объявленный класс: {}", className);
                                javaFile.declareClass(className);
                            }
                        }
                        Matcher matcher = patternWord.matcher(line);
                        while (matcher.find()) {
                            String word = matcher.group();
                            javaFile.word(word);

                            Set<JavaFile> idClass;
                            if (fillContainsWord.containsKey(word)) {
                                idClass = fillContainsWord.get(word);
                            } else {
                                idClass = new HashSet<>();
                                fillContainsWord.put(word, idClass);
                            }
                            idClass.add(javaFile);
                        }
                    }
                    fillJavaFiles.add(javaFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private AnalyzedObjects buildAnalyzedObjects(List<JavaFile> javaFiles, Map<String, Set<JavaFile>> mapWords) {
        JavaAnalyzedObjects analyzedObjects = new JavaAnalyzedObjects();

        for (JavaFile javaFile : javaFiles) {
            LOG.debug(new String(new char[100]).replace("\0", "-"));
            analyzedObjects.addAnalyzedObject(javaFile.id(), new JavaAnalyzedObject(javaFile));
            if (mapWords.containsKey(javaFile.name())) {
                LOG.debug(
                        "Класс {} из файла [{}]",
                        javaFile.id(),
                        javaFile.file().getAbsolutePath()
                );
                //бинго, найдено название нашего класса
                //но может произойти так, что название совпадает с классом из другого пакета, нужно это проверить
                //Получаем файлы, в которых используется наш класс
                Set<JavaFile> fileSet = mapWords.get(javaFile.name());
                //сначала проверяем импорты
                for (JavaFile file : fileSet) {
                    //исключаем себя
                    if (javaFile.id().equals(file.id())) {
                        continue;
                    }

                    boolean contains = false;
                    for (String imports : file.imports()) {
                        if (imports.contains(javaFile.packageName())) {
                            contains = true;
                            break;
                        }
                    }

                    //затем проверяем расположение в одном пакете
                    if (javaFile.packageName().equals(file.packageName()) || contains) {
                        LOG.debug("\t используется {} [{}]", file.id(), file.file().getAbsolutePath());
                        //наполняем связь с родителями
                        analyzedObjects.addParent(javaFile.id(), file.id());
                        analyzedObjects.addChild(file.id(), javaFile.id());
                    }
                }
            }
        }

        return analyzedObjects;
    }
}
