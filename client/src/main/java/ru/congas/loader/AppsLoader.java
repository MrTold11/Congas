package ru.congas.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.CongasClient;
import ru.congas.SimpleApp;
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
public abstract class AppsLoader extends URLClassLoader {

    final Logger logger = LogManager.getLogger("Loader");
    final String name;
    protected final Map<String, Class<? extends SimpleApp>> appsMap = new HashMap<>();

    protected AppsLoader(String name, URL... urls) {
        super(urls, getSystemClassLoader());
        this.name = name;
    }

    /**
     * Create a new instance of an application
     * @param name name of the application
     * @return game instance extend SimpleGame
     */
    public final SimpleApp getNewAppInstance(String name) {
        Class<? extends SimpleApp> appClass = appsMap.get(name);
        if (appClass == null) {
            if (CongasClient.isDebug()) logger.warn("App " + name + " not found in package " + getName());
            return new AppNotFound(name, getName());
        }

        try {
            return appClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error("Couldn't create app instance '" + name + "' in package " + getName(), e);
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
