package ru.congas.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.SimpleGame;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr_Told
 */
public class AnthologyLoader extends URLClassLoader {

    final Logger logger = LogManager.getLogger("Loader");
    final Map<String, Class<? extends SimpleGame>> games = new HashMap<>();

    public AnthologyLoader(File file, String name) throws MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, getSystemClassLoader());

        try {
            Class<?> jarClass;
            jarClass = Class.forName("ru.congas.anthology." + name, true, this);

            Class<? extends SimpleGame> gameClass = jarClass.asSubclass(SimpleGame.class);

            games.put("1", gameClass);
            //game = gameClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException ex) {
            logger.error(ex);
        }
    }

    public Class<? extends SimpleGame> getGameClass(String name) {
        return games.get(name);
    }

}
