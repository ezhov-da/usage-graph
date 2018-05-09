package ru.ezhov.analyse.script;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ezhov.analyse.AnalyzedObject;
import ru.ezhov.analyse.AnalyzedObjects;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ScriptsFile implements AnalyzedObjects {

    private static final Logger LOG = LoggerFactory.getLogger(ScriptsFile.class);

    private File root;
    private Map<String, AnalyzedObject> scripts = new HashMap<>();
    private Map<String, List<String>> parents = new HashMap<>();
    private Map<String, List<String>> children = new HashMap<>();
    private Map<String, List<String>> usageScript = new HashMap<>();

    ScriptsFile(File root) throws Exception {
        this.root = root;
        long start = System.currentTimeMillis();
        initialScripts(root);
        initParentAndChildren();

        LOG.info("SUCCESS SCRIPT ANALYSE: {} ms SCRIPTS COUNT: {}", System.currentTimeMillis() - start, scripts.size());
    }

    private void initialScripts(File file) {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                initialScripts(f);
            }
        } else {
            String path = file.getAbsolutePath();
            LOG.debug("Обработка файла: {}", path);
            path = path.replace('\\', '/');
            int index = path.indexOf("scripts");
            try {
                String id = "/" + path.substring(index, path.length());
                AnalyzedObject script = new ScriptFile(id, file);
                scripts.put(id, script);

                String text = script.text();
                Pattern pattern = Pattern.compile("(?<=getScriptByPath\\(\").+?(?=\")");
                Matcher matcher = pattern.matcher(text);

                List<String> groups = new ArrayList<>();
                while (matcher.find()) {
                    String result;
                    String textFind = result = matcher.group();
                    if (!textFind.startsWith("/")) {
                        result = "/" + textFind;
                        LOG.debug("Обнаружен скрипт [{}] с неверным объявлением: [{}], исправлен на: [{}]", path, textFind, result);
                    }
                    groups.add(result);
                }

                if (!usageScript.containsKey(id)) {
                    usageScript.put(id, new ArrayList<String>());
                }

                List<String> uses = usageScript.get(id);
                for (String textFind : groups) {
                    if (!uses.contains(textFind)) {
                        uses.add(textFind);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initParentAndChildren() throws Exception {
        Set<Map.Entry<String, AnalyzedObject>> entries = scripts.entrySet();
        for (Map.Entry<String, AnalyzedObject> entry : entries) {
            LOG.debug("Используется ID: {}", entry.getKey());
            for (Map.Entry<String, AnalyzedObject> entryInner : entries) {
                //не используем самого себя
                if (entry.getKey().equals(entryInner.getKey())) {
                    continue;
                }
                //если внутренний скрипт использует внешний скрипт,
                //тогда для внутреннего скрипта внешний - ребенок, а для внешнего внутренний родитель
                if (usageScript.get(entryInner.getKey()).contains(entry.getKey())) {
                    putChildren(entryInner.getKey(), entry.getKey());
                    putParent(entry.getKey(), entryInner.getKey());
                }
                //если внешний скрипт использует внутренний скрипт
                //тогда для внешнего внутренний ребенок, а для внутреннего внешний родитель
                if (usageScript.get(entry.getKey()).contains(entryInner.getKey())) {
                    putParent(entryInner.getKey(), entry.getKey());
                    putChildren(entry.getKey(), entryInner.getKey());
                }
            }
        }
    }

    private void putParent(String id, String idParent) {
        if (!parents.containsKey(id)) {
            parents.put(id, new ArrayList<String>());
        }

        List<String> list = parents.get(id);
        if (!list.contains(idParent)) {
            list.add(idParent);
        }
    }

    private void putChildren(String id, String idChild) {
        if (!children.containsKey(id)) {
            children.put(id, new ArrayList<String>());
        }
        List<String> list = children.get(id);
        if (!list.contains(idChild)) {
            list.add(idChild);
        }
    }

    @Override
    public AnalyzedObject get(String id) {
        return scripts.get(id);
    }

    @Override
    public List<AnalyzedObject> all() {
        return new ArrayList<>(scripts.values());
    }

    @Override
    public List<AnalyzedObject> parents(String id) {
        List<AnalyzedObject> parents = new ArrayList<>();
        List<String> pId = this.parents.get(id);
        if (pId != null) {
            for (String s : pId) {
                parents.add(scripts.get(s));
            }
        }
        return parents;
    }

    @Override
    public List<AnalyzedObject> children(String id) {
        List<AnalyzedObject> children = new ArrayList<>();
        List<String> cId = this.children.get(id);
        if (cId != null) {
            for (String s : cId) {
                children.add(scripts.get(s));
            }
        }
        return children;
    }
}
