package framework;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found.");
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties.", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        return systemValue != null ? systemValue : PROPERTIES.getProperty(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }
}
