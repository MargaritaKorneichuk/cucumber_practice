package org.example.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropManager {
    private final Properties properties = new Properties();
    private static PropManager INSTANCE = null;

    private PropManager() {
        loadApplicationProperties();
    }
    public static PropManager getPropManager() {
        if (INSTANCE == null) {
            INSTANCE = new PropManager();
        }
        return INSTANCE;
    }

    private void loadApplicationProperties() {
        try {
            properties.load(new FileInputStream(
                    new File("src/main/resources/" +
                            System.getProperty("propFile", "application") + ".properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
