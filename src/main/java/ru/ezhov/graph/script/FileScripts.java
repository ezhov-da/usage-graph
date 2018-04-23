package ru.ezhov.graph.script;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FileScripts implements Scripts {

    private File root;
    private Map<String, Script> scripts = new HashMap();
    private Map<String, Set<String>> parents = new HashMap();
    private Map<String, Set<String>> children = new HashMap();


    public FileScripts(File root) throws Exception {
        this.root = root;
        initialScripts(root);
        initParentAndChildren();
    }

    private void initialScripts(File file) {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                initialScripts(f);
            }
        } else {
            String path = file.getAbsolutePath();
            System.out.println(path);
            path = path.replace('\\', '/');
            int index = path.indexOf("scripts");
            try {
                String id = "/" + path.substring(index, path.length());
                scripts.put(id, new FileScript(id, file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initParentAndChildren() throws Exception {
        String patternTemplate = "getScriptByPath\\(\"%s\"\\)";
        Set<Map.Entry<String, Script>> entries = scripts.entrySet();
        for (Map.Entry<String, Script> entry : entries) {
            for (Map.Entry<String, Script> entryInner : entries) {
                //не используем самого себя
                if (entry.getKey().equals(entryInner.getKey())) {
                    continue;
                }
                //если внутренний скрипт использует внешний скрипт,
                //тогда для внутреннего скрипта внешний - ребенок, а для внешнего внутренний родитель
                Pattern patternSource = Pattern.compile(String.format(patternTemplate, entry.getKey()));
                String entryInnerText = entryInner.getValue().text().replaceAll("\r\n", "");
                Matcher matcher = patternSource.matcher(entryInnerText);
                if (matcher.find()) {
                    putChildren(entryInner.getKey(), entry.getKey());
                    putParent(entry.getKey(), entryInner.getKey());
                }
                //если внешний скрипт использует внутренний скрипт
                //тогда для внешнего внутренний ребенок, а для внутреннего внешний родитель
                Pattern patternFind = Pattern.compile(String.format(patternTemplate, entryInner.getKey()));
                String entryText = entry.getValue().text().replaceAll("\r\n", "");
                matcher = patternFind.matcher(entryText);
                if (matcher.find()) {
                    putParent(entryInner.getKey(), entry.getKey());
                    putChildren(entry.getKey(), entryInner.getKey());
                }
            }
        }
    }

    private void putParent(String id, String idParent) {
        if (!parents.containsKey(id)) {
            parents.put(id, new HashSet<String>());
        }
        parents.get(id).add(idParent);
    }

    private void putChildren(String id, String idChild) {
        if (!children.containsKey(id)) {
            children.put(id, new HashSet<String>());
        }
        children.get(id).add(idChild);
    }

    @Override
    public Script get(String id) {
        return scripts.get(id);
    }

    @Override
    public Set<Script> all() {
        return new HashSet(scripts.values());
    }

    @Override
    public Set<Script> parents(String id) {
        Set<Script> parents = new HashSet<>();
        Set<String> pId = this.parents.get(id);
        if (pId != null) {
            for (String s : pId) {
                parents.add(scripts.get(s));
            }
        }
        return parents;
    }

    @Override
    public Set<Script> children(String id) {
        Set<Script> children = new HashSet<>();
        Set<String> cId = this.children.get(id);
        if (cId != null) {
            for (String s : cId) {
                children.add(scripts.get(s));
            }
        }
        return children;
    }
}
