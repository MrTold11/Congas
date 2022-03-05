package ru.congas.loader;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public class StorageManager extends ru.congas.core.loader.StorageManager {

    static final String testAppsName = "TestApps";
    static TestAppsLoader TEST_APPS_LOADER = null;

    private static boolean loadTestApps() {
        try {
            TEST_APPS_LOADER = new TestAppsLoader(testAppsName);
        } catch (Exception e) {
            logger.error("Couldn't load test games: ", e);
        }
        return TEST_APPS_LOADER != null;
    }

    public static boolean hasApps() {
        if (anthology.size() > 0) return true;
        return CongasClient.isDebug() && !anthology.containsKey(testAppsName)
                && (TEST_APPS_LOADER != null || loadTestApps()) && TEST_APPS_LOADER.appsCount() > 0;
    }

    public static String[] getLoadedAnthologies() {
        if (CongasClient.isDebug()) {
            if (TEST_APPS_LOADER == null) loadTestApps();
            if (TEST_APPS_LOADER != null && TEST_APPS_LOADER.appsCount() != 0 && !anthology.containsKey(testAppsName))
                anthology.put(testAppsName, TEST_APPS_LOADER);
        } else
            anthology.remove(testAppsName);
        return anthology.keySet().toArray(new String[0]);
    }

}
