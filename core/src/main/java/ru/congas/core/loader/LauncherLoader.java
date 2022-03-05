package ru.congas.core.loader;

import org.apache.logging.log4j.LogManager;
import ru.congas.core.CongasCore;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Mr_Told
 */
public class LauncherLoader extends URLClassLoader {

    final File file;

    public LauncherLoader() throws MalformedURLException {
        super(new URL[] {new File(CongasCore.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toURI().toURL()},
                getSystemClassLoader());
        this.file = new File(CongasCore.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    public Class<? extends CongasCore> findLoader() {
        try {
            JarFile jar = new JarFile(file);
            Manifest mf = jar.getManifest();
            Attributes attr = mf.getMainAttributes();
            String className = attr.getValue("Congas-Launcher");
            Class<?> loaderClass = loadClass(className);
            if (loaderClass.getSuperclass() == null
                    || Modifier.isAbstract(loaderClass.getModifiers())
                    || !loaderClass.getSuperclass().equals(CongasCore.class))
                return null;
            if (Arrays.stream(loaderClass.getDeclaredConstructors()).anyMatch(dc -> dc.getParameterCount() == 0))
                return loaderClass.asSubclass(CongasCore.class);

        } catch (Exception e) {
            LogManager.getLogger(LauncherLoader.class).warn("Exception during loader search: ", e);
        }
        return null;
    }

}
