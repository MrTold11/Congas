package ru.congas.core.loader;

import ru.congas.core.CongasCore;
import ru.congas.core.application.Activity;
import ru.congas.core.application.GameActivity;
import ru.congas.core.application.PageActivity;
import ru.congas.core.pages.ErrorActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

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
        try {
            Manifest mf = jar.getManifest();
            Attributes attr = mf.getMainAttributes();
            String className = attr.getValue("Applications");
            for (String n : className.split(";")) {
                Class<?> appClass = loadClass(n);

                if (rejectClass(appClass))
                    return false;

                addApp(appClass, n);
            }
            return appsMap.size() != 0;
        } catch (Exception e) {
            logger.warn("Exception during package load: ", e);
        }
        return false;
    }

    @Deprecated
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
                    if (CongasCore.isDebug()) logger.warn(er);
                    continue;
                }

                if (appClass == null)
                    throw new ClassNotFoundException("Class with name " + className + " is null!");

                if (rejectClass(appClass)) continue;

                addApp(appClass, className);
            }
        } catch (ClassNotFoundException ex) {
            logger.error("Error while loading package " + name + ": ", ex);
        }
    }

    private void addApp(Class<?> appClass, String className) {
        int li = className.lastIndexOf('.');
        if (li != -1)
            className = className.substring(className.endsWith(".") ? li : li + 1);

        if (Arrays.stream(appClass.getDeclaredConstructors()).anyMatch(dc -> dc.getParameterCount() == 0))
            appsMap.put(className, appClass.asSubclass(Activity.class));
        else
            logger.warn("Couldn't add application " + className + " from " + name +
                    " package as class " + appClass.getName() + " doesn't have an empty constructor");
    }

    /**
     * Check if found class is an application class
     * @param c class
     * @return true if it's not application
     */
    protected boolean rejectClass(Class<?> c) {
        return c.getSuperclass() == null || Modifier.isAbstract(c.getModifiers()) || (
                !c.getSuperclass().equals(Activity.class)
                && !c.getSuperclass().equals(PageActivity.class) && !c.getSuperclass().equals(GameActivity.class)
                && !c.getSuperclass().equals(ErrorActivity.class));
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
