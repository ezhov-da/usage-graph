package ru.ezhov.graph.script;

import java.io.File;
import java.util.*;
import java.util.regex.*;

class FileScripts implements Scripts {

	private File root;
	private Map<String, Script> scripts = new HashMap<>();
	private Map<String, List<String>> parents = new HashMap<>();
	private Map<String, List<String>> children = new HashMap<>();
	private Map<String, List<String>> usageScript = new HashMap<>();

	public FileScripts(File root) throws Exception {
		this.root = root;
		long start = System.currentTimeMillis();
		initialScripts(root);
		initParentAndChildren();
		System.out.println("SUCCESS SCRIPT ANALYSE: " + (System.currentTimeMillis() - start) + " ms COUNT SCRIPTS: " +
			scripts.size());
	}

	private void initialScripts(File file) {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				initialScripts(f);
			}
		} else {
			String path = file.getAbsolutePath();
			System.out.println("> " + path);
			path = path.replace('\\', '/');
			int index = path.indexOf("scripts");
			try {
				String id = "/" + path.substring(index, path.length());
				Script script = new FileScript(id, file);
				scripts.put(id, script);

				String text = script.text();
				Pattern pattern = Pattern.compile("(?<=getScriptByPath\\(\").+?(?=\")");
				Matcher matcher = pattern.matcher(text);

				List<String> groups = new ArrayList<>();
				while (matcher.find()) {
					String textFind = matcher.group();
					groups.add(textFind);
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
		String patternTemplate = "getScriptByPath\\(\"%s\"\\)";
		Set<Map.Entry<String, Script>> entries = scripts.entrySet();
		for (Map.Entry<String, Script> entry : entries) {
			System.out.println(">>> " + entry.getKey());
			for (Map.Entry<String, Script> entryInner : entries) {
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
	public Script get(String id) {
		return scripts.get(id);
	}

	@Override
	public List<Script> all() {
		return new ArrayList<>(scripts.values());
	}

	@Override
	public List<Script> parents(String id) {
		List<Script> parents = new ArrayList<>();
		List<String> pId = this.parents.get(id);
		if (pId != null) {
			for (String s : pId) {
				parents.add(scripts.get(s));
			}
		}
		return parents;
	}

	@Override
	public List<Script> children(String id) {
		List<Script> children = new ArrayList<>();
		List<String> cId = this.children.get(id);
		if (cId != null) {
			for (String s : cId) {
				children.add(scripts.get(s));
			}
		}
		return children;
	}
}
