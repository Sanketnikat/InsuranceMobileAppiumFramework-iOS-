package framework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.FileInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {

    private static final ThreadLocal<AppiumDriver> driver =
            new ThreadLocal<>();

    private static Properties config;

    // =========================================================
    // LOAD CONFIGURATION
    // =========================================================

    static {
        try {

            config = new Properties();

            FileInputStream file = new FileInputStream(
                    "src/test/resources/config.properties"
            );

            config.load(file);
            file.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not load config.properties",
                    e
            );
        }
    }

    // =========================================================
    // START DRIVER
    // =========================================================

    public static void startDriver() {

        String platform =
                config.getProperty("platform");

        if (platform.equalsIgnoreCase("ios")) {

            startIOSDriver();

        } else {

            startAndroidDriver();
        }
    }

    // =========================================================
    // iOS DRIVER
    // Supports:
    // 1. Physical iPhone
    // 2. iOS Simulator
    // =========================================================

    private static void startIOSDriver() {

        try {

            String serverUrl =
                    config.getProperty("appium.server");

            String deviceType =
                    config.getProperty(
                            "deviceType",
                            "real"
                    );

            String bundleId =
                    config.getProperty(
                            "ios.bundle.id"
                    );

            XCUITestOptions options =
                    new XCUITestOptions();

            options.setPlatformName("iOS");
            options.setAutomationName("XCUITest");

            // =================================================
            // PHYSICAL iPHONE
            // =================================================

            if (deviceType.equalsIgnoreCase("real")) {

                String deviceName =
                        config.getProperty(
                                "ios.real.device.name"
                        );

                String platformVersion =
                        config.getProperty(
                                "ios.real.platform.version"
                        );

                String udid =
                        config.getProperty(
                                "ios.real.udid"
                        );

                options.setDeviceName(deviceName);

                options.setPlatformVersion(
                        platformVersion
                );

                options.setUdid(udid);

                options.setBundleId(bundleId);

                // ---------------------------------------------
                // WebDriverAgent Signing
                // ---------------------------------------------

                String xcodeOrgId =
                        config.getProperty(
                                "ios.xcode.org.id"
                        );

                String xcodeSigningId =
                        config.getProperty(
                                "ios.xcode.signing.id",
                                "Apple Development"
                        );

                options.setCapability(
                        "appium:xcodeOrgId",
                        xcodeOrgId
                );

                options.setCapability(
                        "appium:xcodeSigningId",
                        xcodeSigningId
                );

                options.setCapability(
                        "appium:updatedWDABundleId",
                        "com.sanket.WebDriverAgentRunner"
                );

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "iOS PHYSICAL DEVICE"
                );

                System.out.println(
                        "Device: " + deviceName
                );

                System.out.println(
                        "iOS Version: " + platformVersion
                );

                System.out.println(
                        "UDID: " + udid
                );

                System.out.println(
                        "======================================"
                );
            }

            // =================================================
            // iOS SIMULATOR
            // =================================================

            else if (
                    deviceType.equalsIgnoreCase("simulator")
            ) {

                String deviceName =
                        config.getProperty(
                                "ios.sim.device.name"
                        );

                String platformVersion =
                        config.getProperty(
                                "ios.sim.platform.version"
                        );

                String udid =
                        config.getProperty(
                                "ios.sim.udid"
                        );

                options.setDeviceName(deviceName);

                options.setPlatformVersion(
                        platformVersion
                );

                options.setUdid(udid);

                options.setBundleId(bundleId);

                // Simulator is NOT a real device
                options.setCapability(
                        "appium:isRealDevice",
                        false
                );

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "iOS SIMULATOR"
                );

                System.out.println(
                        "Device: " + deviceName
                );

                System.out.println(
                        "iOS Version: " + platformVersion
                );

                System.out.println(
                        "UDID: " + udid
                );

                System.out.println(
                        "======================================"
                );
            }

            // =================================================
            // INVALID DEVICE TYPE
            // =================================================

            else {

                throw new RuntimeException(
                        "Invalid deviceType: "
                                + deviceType
                                + ". Use 'real' or 'simulator'."
                );
            }

            // =================================================
            // COMMON iOS SETTINGS
            // =================================================

            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            options.setShowXcodeLog(true);

            // =================================================
            // CREATE iOS DRIVER
            // =================================================

            driver.set(
                    new IOSDriver(
                            new URL(serverUrl),
                            options
                    )
            );

            System.out.println(
                    "iOS Appium driver started successfully."
            );

            System.out.println(
                    "Bima Sugam Bundle ID: "
                            + bundleId
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not start iOS Appium driver.",
                    e
            );
        }
    }

    // =========================================================
    // ANDROID DRIVER
    // =========================================================

    private static void startAndroidDriver() {

        try {

            String serverUrl =
                    config.getProperty(
                            "appium.server"
                    );

            String deviceName =
                    config.getProperty(
                            "android.device.name"
                    );

            String platformVersion =
                    config.getProperty(
                            "android.platform.version"
                    );

            String appPath =
                    config.getProperty(
                            "android.app.path"
                    );

            UiAutomator2Options options =
                    new UiAutomator2Options();

            options.setPlatformName("Android");

            options.setAutomationName(
                    "UiAutomator2"
            );

            options.setDeviceName(
                    deviceName
            );

            if (platformVersion != null
                    && !platformVersion.isEmpty()) {

                options.setPlatformVersion(
                        platformVersion
                );
            }

            if (appPath != null
                    && !appPath.isEmpty()) {

                options.setApp(appPath);
            }

            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            driver.set(
                    new AndroidDriver(
                            new URL(serverUrl),
                            options
                    )
            );

            System.out.println(
                    "Android Appium driver started successfully."
            );

            System.out.println(
                    "Android Device: "
                            + deviceName
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not start Android Appium driver.",
                    e
            );
        }
    }

    // =========================================================
    // GET DRIVER
    // =========================================================

    public static AppiumDriver getDriver() {

        return driver.get();
    }

    // =========================================================
    // QUIT DRIVER
    // =========================================================

    public static void quitDriver() {

        if (driver.get() != null) {

            try {

                driver.get().quit();

            } finally {

                driver.remove();
            }
        }
    }

    // =========================================================
    // GET CONFIG
    // =========================================================

    public static Properties getConfig() {

        return config;
    }
}