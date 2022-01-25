package ru.congas.loader;

import ru.congas.CongasClient;
import ru.congas.SimpleApp;
import ru.congas.SimpleGame;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class for loading apps from a jar file
 * @author Mr_Told
 */
public class AppsJarLoader extends AppsLoader {

    /**
     * Load and store (in map) package apps
     * @param file apps jar file
     * @param name apps group name
     * @throws IOException if there are problems with jar file
     */
    public AppsJarLoader(File file, String name) throws IOException {
        super(name, file.toURI().toURL());

        JarFile jarFile = new JarFile(file);
        if (!normalLoad(jarFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            legacyLoad(e);
        }
        jarFile.close();
    }

    private boolean normalLoad(JarFile jar) {
        return false;
    }

    private void legacyLoad(Enumeration<JarEntry> e) {
        try {
            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je == null || rejectJarEntry(je)) continue;

                // remove .class
                String className = je.getName().substring(0, je.getName().length() - 6).replace('/', '.');
                Class<?> appClass;
                try {
                    appClass = loadClass(className);
                } catch (NoClassDefFoundError er) {
                    if (CongasClient.isDebug()) logger.warn(er);
                    continue;
                }

                if (appClass == null) throw new ClassNotFoundException("Class with name " + className + " is null!");
                int li = className.lastIndexOf('.');
                if (li != -1)
                    className = className.substring(className.endsWith(".") ? li : li + 1);

                if (rejectClass(appClass)) continue;

                if (Arrays.stream(appClass.getDeclaredConstructors()).anyMatch(dc -> dc.getParameterCount() == 0))
                    appsMap.put(className, appClass.asSubclass(SimpleApp.class));
                else
                    logger.warn("Couldn't add application " + className + " from " + name +
                            " package as class " + appClass.getName() + " doesn't have an empty constructor");
            }
        } catch (ClassNotFoundException ex) {
            logger.error("Error while loading package " + name + ": ", ex);
        }
    }

    /**
     * Check if found class is an application class
     * @param c class
     * @return true if it's not application
     */
    protected boolean rejectClass(Class<?> c) {
        return c.getSuperclass() == null || Modifier.isAbstract(c.getModifiers()) ||
                (!c.getSuperclass().equals(SimpleApp.class) && !c.getSuperclass().equals(SimpleGame.class));
    }

    /**
     * Check if found jar entry is a possible application class
     * @param je jar entry
     * @return true if it's not possible class
     */
    protected boolean rejectJarEntry(JarEntry je) {
        return je.isDirectory() || !je.getName().endsWith(".class");
    }

}
