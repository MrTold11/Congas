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

    final Logger logger = LogManager.getLogger("Storage");
    final static Map<String, GameLoader> anthology = new HashMap<>();
    final static TestGames testGames = new TestGames();
    final static String testGamesName = "TestGames";

    public void init(boolean loadLocal) {
        if (loadLocal) loadJars(new File("games/"));
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
            AnthologyLoader a = new AnthologyLoader(f, name);
            if (a.hasGames()) {
                if (!anthology.containsKey(name))
                    anthology.put(name, a);
                else logger.warn("Anthology already loaded: " + name);
            }
            else if (CongasClient.isDebug()) logger.warn("Find anthology without games: " + name);
        } catch (IOException e) {
            if (CongasClient.isDebug()) logger.warn("Couldn't load anthology jar: " + name, e);
        }
    }

    public static String[] getLoadedAnthologies() {
        if (CongasClient.isDebug()) {
            if (!anthology.containsKey(testGamesName)) anthology.put(testGamesName, testGames);
        } else
            anthology.remove(testGamesName);
        return anthology.keySet().toArray(new String[0]);
    }

    public static GameLoader getLoader(String name) {
        return anthology.get(name);
    }

    public void close() {

    }

}
