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
    final Map<String, AnthologyLoader> anthology = new HashMap<>();

    public void init(boolean loadLocal) {
        if (loadLocal) loadJars(new File("games/"));
        //todo load global
    }

    private void loadJars(File dir) {
        if (!dir.isDirectory() || !dir.exists()) return;
        File[] jars = dir.listFiles((d, name) -> new File(d, name).isFile() && name.endsWith(".jar"));
        if (jars == null || jars.length == 0) return;

        for (File f : jars)
            tryLoad(f, f.getName());
    }

    private void tryLoad(File f, String name) {
        try {
            AnthologyLoader a = new AnthologyLoader(f, name);
            anthology.put(name, a);
        } catch (IOException e) {
            if (CongasClient.debug) logger.warn("Couldn't load anthology jar: " + name, e);
        }
    }

}
