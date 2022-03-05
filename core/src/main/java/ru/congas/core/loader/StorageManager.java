package ru.congas.core.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr_Told
 */
public class StorageManager {

    //todo storage manager refactor

    protected final static Logger logger = LogManager.getLogger("Storage");
    protected final static Map<String, AppsLoader> anthology = new HashMap<>();

    public void init(boolean loadLocal) {
        if (loadLocal) loadJars(new File("apps/"));
        //todo load global
        logger.info("Loaded " + anthology.size() + " anthologies");
    }

    private void loadJars(File dir) {
        if (!dir.isDirectory() || !dir.exists()) return;
        File[] jars = dir.listFiles((d, name) -> new File(d, name).isFile() && name.endsWith(".jar"));
        if (jars == null || jars.length == 0) return;

        for (File f : jars) {
            String name = f.getName().substring(0, f.getName().lastIndexOf('.'));
            //int li = name.lastIndexOf('.');
            //if (li != -1)
            //    name = name.substring(name.endsWith(".") ? li : li + 1);
            String[] nms = name.split("\\.");
            name = nms.length == 1 ? nms[0] : nms[1];
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            tryLoad(f, name);
        }
    }

    private void tryLoad(File f, String name) {
        try {
            AppsJarLoader a = new AppsJarLoader(f, name);
            if (a.appsCount() > 0) {
                if (!anthology.containsKey(name)) {
                    if (a.appsCount() == 1) name += ": " + a.getApps()[0];
                    anthology.put(name, a);
                } else
                    logger.warn("Package already loaded: " + name);
            }
            else logger.warn("Find package without apps: " + name);
        } catch (Exception e) {
            logger.warn("Couldn't load application jar: " + name, e);
        }
    }

    public static boolean hasApps() {
        return anthology.size() > 0;
    }

    public static String[] getLoadedAnthologies() {
        return anthology.keySet().toArray(new String[0]);
    }

    public static AppsLoader getLoader(String name) {
        return anthology.get(name);
    }

    public void close() {

    }

}
