package ru.congas.loader;

import ru.congas.CongasClient;
import ru.congas.SimpleGame;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class for loading anthologies from a jar file
 * @author Mr_Told
 */
public class AnthologyJarLoader extends AnthologyLoader {

    /**
     * Load and store (in map) anthology's apps
     * @param file anthology jar file
     * @param name anthology name
     * @throws IOException if there are problems with jar file
     */
    public AnthologyJarLoader(File file, String name) throws IOException {
        super(name, file.toURI().toURL());

        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> e = jarFile.entries();

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
                    appsMap.put(className, appClass.asSubclass(SimpleGame.class));
                else
                    logger.warn("Couldn't add application " + className + " from " + name +
                            " anthology as class " + appClass.getName() + " doesn't have an empty constructor");
            }
        } catch (ClassNotFoundException ex) {
            logger.error("Error while loading anthology " + name + ": ", ex);
        } finally {
            jarFile.close();
        }
    }

    /**
     * Check if found class is an application class
     * @param c class
     * @return true if it's not application
     */
    protected boolean rejectClass(Class<?> c) {
        return c.getSuperclass() == null || !c.getSuperclass().equals(SimpleGame.class);
    }

    /**
     * Check if found jar entry is a possible application class
     * @param je jar entry
     * @return true if it's not possible class
     */
    protected boolean rejectJarEntry(JarEntry je) {
        return je.isDirectory() || !je.getName().endsWith(".class");
    }

    public boolean hasGames() {
        return appsMap.size() > 0;
    }

}
