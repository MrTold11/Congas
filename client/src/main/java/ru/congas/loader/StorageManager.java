package ru.congas.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.CongasClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr_Told
 */
public class StorageManager {

    final static Logger logger = LogManager.getLogger("Storage");
    final static Map<String, AnthologyLoader> anthology = new HashMap<>();
    final static String testAppsName = "TestApps";
    static TestAppsLoader TEST_APPS_LOADER = null;

    public void init(boolean loadLocal) {
        if (CongasClient.isDebug()) loadTestApps();
        if (loadLocal) loadJars(new File("games/"));
        //todo load global
        logger.info("Loaded " + anthology.size() + " anthologies");
    }

    private static void loadTestApps() {
        try {
            TEST_APPS_LOADER = new TestAppsLoader(testAppsName);
        } catch (IOException e) {
            logger.error("Couldn't load test games: ", e);
        }
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
            AnthologyJarLoader a = new AnthologyJarLoader(f, name);
            if (a.appsCount() > 0) {
                if (!anthology.containsKey(name)) {
                    if (a.appsCount() == 1) name += ": " + a.getApps()[0];
                    anthology.put(name, a);
                } else
                    logger.warn("Anthology already loaded: " + name);
            }
            else if (CongasClient.isDebug()) logger.warn("Find anthology without games: " + name);
        } catch (IOException e) {
            if (CongasClient.isDebug()) logger.warn("Couldn't load anthology jar: " + name, e);
        }
    }

    public static String[] getLoadedAnthologies() {
        if (CongasClient.isDebug()) {
            if (TEST_APPS_LOADER == null) loadTestApps();
            if (TEST_APPS_LOADER != null && !anthology.containsKey(testAppsName)) anthology.put(testAppsName, TEST_APPS_LOADER);
        } else
            anthology.remove(testAppsName);
        return anthology.keySet().toArray(new String[0]);
    }

    public static AnthologyLoader getLoader(String name) {
        return anthology.get(name);
    }

    public void close() {

    }

}
