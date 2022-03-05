package ru.congas.core.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.core.CongasCore;
import ru.congas.core.application.Activity;

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

    protected final Logger logger = LogManager.getLogger("Loader");
    protected final String name;
    protected final Map<String, Class<? extends Activity>> appsMap = new HashMap<>();

    protected AppsLoader(String name, URL... urls) {
        super(urls, getSystemClassLoader());
        this.name = name;
    }

    public Class<? extends Activity> getClass(String name) {
        return appsMap.get(name);
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
