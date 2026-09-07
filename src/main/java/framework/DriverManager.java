package framework;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class DriverManager {
    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static void startAndroidDriver() {
        try {
            String udid = resolveDeviceUdid();

            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setDeviceName(ConfigReader.get("device.name"))
                    .setApp(ConfigReader.get("app.path"))
                    .setNewCommandTimeout(Duration.ofSeconds(120));

            String platformVersion = ConfigReader.get("platform.version");
            if (platformVersion != null && !platformVersion.isBlank()) {
                options.setPlatformVersion(platformVersion);
            }

            if (udid != null && !udid.isBlank()) {
                options.setUdid(udid);
            }

            AndroidDriver driver = new AndroidDriver(
                    URI.create(ConfigReader.get("appium.server")).toURL(),
                    options
            );

            DRIVER.set(driver);
        } catch (Exception e) {
            throw new RuntimeException("Could not start Android Appium driver.", e);
        }
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized.");
        }
        return driver;
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    private static String resolveDeviceUdid() {
        String configured = ConfigReader.get("device.udid");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }

        List<String> devices = new ArrayList<>();

        try {
            Process process = new ProcessBuilder("adb", "devices")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("^\\S+\\s+device$")) {
                        devices.add(line.split("\\s+")[0]);
                    }
                }
            }

            process.waitFor();

            if (devices.size() == 1) {
                return devices.get(0);
            }

            if (devices.size() > 1) {
                throw new IllegalStateException(
                        "Multiple Android devices are connected. Use -Ddevice.udid=<deviceId>.");
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("Unable to detect Android devices using ADB.", e);
        }
    }
}
