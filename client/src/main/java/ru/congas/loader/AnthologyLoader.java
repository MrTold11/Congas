package ru.congas.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.CongasClient;
import ru.congas.SimpleGame;
import ru.congas.pages.GameNotFound;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Mr_Told
 */
public class AnthologyLoader extends URLClassLoader implements GameLoader {

    final Logger logger = LogManager.getLogger("Loader");
    final String name;
    private final Map<String, Class<? extends SimpleGame>> gamesMap = new HashMap<>();

    /**
     * Load and store (in map) anthology's games
     * @param file anthology jar file
     * @param name anthology name
     * @throws IOException if there are problems with jar file
     */
    public AnthologyLoader(File file, String name) throws IOException {
        super(new URL[] {file.toURI().toURL()}, getSystemClassLoader());
        this.name = name;

        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> e = jarFile.entries();

        try {
            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")) continue;

                // remove .class
                String className = je.getName().substring(0, je.getName().length() - 6).replace('/', '.');
                Class<?> gameClass = loadClass(className);
                int li = className.lastIndexOf('.');
                if (li != -1)
                    className = className.substring(className.endsWith(".") ? li : li + 1);

                if (!gameClass.getSuperclass().equals(SimpleGame.class)) continue;

                if (Arrays.stream(gameClass.getDeclaredConstructors()).anyMatch(dc -> dc.getParameterCount() == 0))
                    gamesMap.put(className, gameClass.asSubclass(SimpleGame.class));
                else
                    logger.warn("Couldn't add game " + className + " from " + name +
                            " anthology as class " + gameClass.getName() + " doesn't have an empty constructor");
            }
        } catch (ClassNotFoundException ex) {
            logger.error(ex);
        } finally {
            jarFile.close();
        }
    }

    /**
     * Create a new instance of a game
     * @param name name of the game
     * @return game instance extend SimpleGame
     */
    public final SimpleGame getNewGameInstance(String name) {
        Class<? extends SimpleGame> gameClass = gamesMap.get(name);
        if (gameClass == null) {
            if (CongasClient.isDebug()) logger.warn("Game " + name + " not found in anthology " + getName());
            return new GameNotFound(name, getName());
        }

        try {
            return gameClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error("Couldn't create game instance '" + name + "' in anthology " + getName(), e);
        }
        return new GameNotFound(name, getName());
    }

    public boolean hasGames() {
        return gamesMap.size() > 0;
    }

    public String getName() {
        return name;
    }

    public String[] getGames() {
        return gamesMap.keySet().toArray(new String[0]);
    }

}
