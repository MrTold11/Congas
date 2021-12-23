package ru.congas.loader;

import ru.congas.CongasClient;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;

/**
 * Test applications loader
 * @author Mr_Told
 */
public class TestAppsLoader extends AnthologyJarLoader {

    final String path = "ru/congas/pages/testApps";

    public TestAppsLoader(String name) throws IOException {
        super(new File(TestAppsLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath()), name);
        if (CongasClient.isDebug()) logger.info("Loaded " + appsCount() + " test applications");
    }

    /**
     * Check if found class is a test application class
     * @param c class
     * @return true if it's not application
     */
    @Override
    protected boolean rejectClass(Class<?> c) {
        return super.rejectClass(c) || c.getPackage() == null || !c.getPackage().getName().equals(path.replace('/', '.'));
    }

    /**
     * Check if found jar entry is a possible test app class
     * @param je jar entry
     * @return true if it's not possible test app class
     */
    @Override
    protected boolean rejectJarEntry(JarEntry je) {
        return !je.getName().startsWith(path) || super.rejectJarEntry(je);
    }

}
