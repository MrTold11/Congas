package ru.congas.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.CongasClient;
import ru.congas.SimpleGame;
import ru.congas.pages.AppNotFound;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for loading applications from anywhere
 * @author Mr_Told
 */
public abstract class AnthologyLoader extends URLClassLoader {

    final Logger logger = LogManager.getLogger("Loader");
    final String name;
    protected final Map<String, Class<? extends SimpleGame>> appsMap = new HashMap<>();

    protected AnthologyLoader(String name, URL... urls) {
        super(urls, getSystemClassLoader());
        this.name = name;
    }

    /**
     * Create a new instance of an application
     * @param name name of the application
     * @return game instance extend SimpleGame
     */
    public final SimpleGame getNewAppInstance(String name) {
        Class<? extends SimpleGame> gameClass = appsMap.get(name);
        if (gameClass == null) {
            if (CongasClient.isDebug()) logger.warn("App " + name + " not found in anthology " + getName());
            return new AppNotFound(name, getName());
        }

        try {
            return gameClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error("Couldn't create app instance '" + name + "' in anthology " + getName(), e);
        }
        return new AppNotFound(name, getName());
    }

    public String getName() {
        return name;
    }

    public String[] getApps() {
        return appsMap.keySet().toArray(new String[0]);
    }

    public int appsCount() {
        return appsMap.size();
    }

}
