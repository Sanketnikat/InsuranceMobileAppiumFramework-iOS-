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

        String execution =
                config.getProperty(
                        "execution",
                        "local"
                );

        String platform =
                config.getProperty("platform");

        System.out.println("======================================");
        System.out.println("EXECUTION: " + execution);
        System.out.println("PLATFORM : " + platform);
        System.out.println("======================================");

        if (platform == null || platform.trim().isEmpty()) {

            throw new RuntimeException(
                    "Platform is not configured in config.properties. " +
                    "Use 'ios' or 'android'."
            );
        }

        // =====================================================
        // LOCAL APPIUM
        // =====================================================

        if (execution.equalsIgnoreCase("local")) {

            if (platform.equalsIgnoreCase("ios")) {

                startIOSDriver();

            } else if (platform.equalsIgnoreCase("android")) {

                startAndroidDriver();

            } else {

                throw new RuntimeException(
                        "Invalid platform: "
                                + platform
                                + ". Use 'ios' or 'android'."
                );
            }

        }

        // =====================================================
        // BROWSERSTACK
        // =====================================================

        else if (execution.equalsIgnoreCase("browserstack")) {

            if (platform.equalsIgnoreCase("ios")) {

                startBrowserStackIOSDriver();

            } else if (platform.equalsIgnoreCase("android")) {

                startBrowserStackAndroidDriver();

            } else {

                throw new RuntimeException(
                        "Invalid platform: "
                                + platform
                                + ". Use 'ios' or 'android'."
                );
            }

        }

        // =====================================================
        // INVALID EXECUTION
        // =====================================================

        else {

            throw new RuntimeException(
                    "Invalid execution: "
                            + execution
                            + ". Use 'local' or 'browserstack'."
            );
        }
    }

    // =========================================================
    // LOCAL iOS DRIVER
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
                        "LOCAL iOS PHYSICAL DEVICE"
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
                        "LOCAL iOS SIMULATOR"
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
            // COMMON LOCAL iOS SETTINGS
            // =================================================

            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            options.setShowXcodeLog(true);

            // =================================================
            // CREATE LOCAL iOS DRIVER
            // =================================================

            driver.set(
                    new IOSDriver(
                            new URL(serverUrl),
                            options
                    )
            );

            System.out.println(
                    "Local iOS Appium driver started successfully."
            );

            System.out.println(
                    "Bima Sugam Bundle ID: "
                            + bundleId
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not start local iOS Appium driver.",
                    e
            );
        }
    }

    // =========================================================
    // LOCAL ANDROID DRIVER
    // Physical Android Device
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

            String appPackage =
                    config.getProperty(
                            "android.app.package"
                    );

            String appActivity =
                    config.getProperty(
                            "android.app.activity"
                    );

            UiAutomator2Options options =
                    new UiAutomator2Options();

            // =================================================
            // ANDROID CAPABILITIES
            // =================================================

            options.setPlatformName("Android");

            options.setAutomationName(
                    "UiAutomator2"
            );

            options.setDeviceName(
                    deviceName
            );

            // Use the connected physical device
            options.setUdid(
                    deviceName
            );

            // Platform version is optional
            if (platformVersion != null
                    && !platformVersion.isEmpty()) {

                options.setPlatformVersion(
                        platformVersion
                );
            }

            // =================================================
            // INSTALLED ANDROID APP
            // =================================================

            options.setAppPackage(
                    appPackage
            );

            options.setAppActivity(
                    appActivity
            );

            // Do not reinstall the application
            options.setNoReset(true);

            // Keep application data
            options.setFullReset(false);

            // Automatically grant permissions where possible
            options.setAutoGrantPermissions(true);

            // =================================================
            // COMMAND TIMEOUT
            // =================================================

            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            // =================================================
            // CREATE ANDROID DRIVER
            // =================================================

            driver.set(
                    new AndroidDriver(
                            new URL(serverUrl),
                            options
                    )
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "LOCAL ANDROID PHYSICAL DEVICE"
            );

            System.out.println(
                    "Device: " + deviceName
            );

            System.out.println(
                    "Package: " + appPackage
            );

            System.out.println(
                    "Activity: " + appActivity
            );

            System.out.println(
                    "Automation: UiAutomator2"
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Local Android Appium driver started successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not start local Android Appium driver.",
                    e
            );
        }
    }

    // =========================================================
    // BROWSERSTACK iOS DRIVER
    // =========================================================

    private static void startBrowserStackIOSDriver() {

        try {

            String username =
                    getEnvironmentVariable(
                            "BROWSERSTACK_USERNAME"
                    );

            String accessKey =
                    getEnvironmentVariable(
                            "BROWSERSTACK_ACCESS_KEY"
                    );

            String browserStackUrl =
                    config.getProperty(
                            "browserstack.url",
                            "https://hub-cloud.browserstack.com/wd/hub"
                    );

            String deviceName =
                    config.getProperty(
                            "browserstack.ios.device"
                    );

            String platformVersion =
                    config.getProperty(
                            "browserstack.ios.platform.version"
                    );

            String app =
                    getEnvironmentVariable(
                            "BROWSERSTACK_IOS_APP"
                    );

            validateBrowserStackCredentials(
                    username,
                    accessKey
            );

            validateBrowserStackApp(
                    app,
                    "BROWSERSTACK_IOS_APP"
            );

            XCUITestOptions options =
                    new XCUITestOptions();

            // =================================================
            // BROWSERSTACK iOS CAPABILITIES
            // =================================================

            options.setPlatformName("iOS");

            options.setAutomationName(
                    "XCUITest"
            );

            options.setDeviceName(
                    deviceName
            );

            options.setPlatformVersion(
                    platformVersion
            );

            options.setCapability(
                    "appium:app",
                    app
            );

            // BrowserStack credentials
            options.setCapability(
                    "bstack:options",
                    createBrowserStackOptions(
                            username,
                            accessKey,
                            "Bima Sugam iOS"
                    )
            );

            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "BROWSERSTACK iOS"
            );

            System.out.println(
                    "Device: " + deviceName
            );

            System.out.println(
                    "iOS Version: " + platformVersion
            );

            System.out.println(
                    "App: " + app
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // CREATE BROWSERSTACK iOS DRIVER
            // =================================================

            driver.set(
                    new IOSDriver(
                            new URL(browserStackUrl),
                            options
                    )
            );

            System.out.println(
                    "BrowserStack iOS driver started successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not start BrowserStack iOS driver.",
                    e
            );
        }
    }

    // =========================================================
    // BROWSERSTACK ANDROID DRIVER
    // =========================================================

    private static void startBrowserStackAndroidDriver() {

        try {

            String username =
                    getEnvironmentVariable(
                            "BROWSERSTACK_USERNAME"
                    );

            String accessKey =
                    getEnvironmentVariable(
                            "BROWSERSTACK_ACCESS_KEY"
                    );

            String browserStackUrl =
                    config.getProperty(
                            "browserstack.url",
                            "https://hub-cloud.browserstack.com/wd/hub"
                    );

            String deviceName =
                    config.getProperty(
                            "browserstack.android.device"
                    );

            String platformVersion =
                    config.getProperty(
                            "browserstack.android.platform.version"
                    );

            String app =
                    getEnvironmentVariable(
                            "BROWSERSTACK_ANDROID_APP"
                    );

            validateBrowserStackCredentials(
                    username,
                    accessKey
            );

            validateBrowserStackApp(
                    app,
                    "BROWSERSTACK_ANDROID_APP"
            );

            UiAutomator2Options options =
                    new UiAutomator2Options();

            // =================================================
            // BROWSERSTACK ANDROID CAPABILITIES
            // =================================================

            options.setPlatformName("Android");

            options.setAutomationName(
                    "UiAutomator2"
            );

            options.setDeviceName(
                    deviceName
            );

            options.setPlatformVersion(
                    platformVersion
            );

            options.setCapability(
                    "appium:app",
                    app
            );

            // BrowserStack credentials
            options.setCapability(
                    "bstack:options",
                    createBrowserStackOptions(
                            username,
                            accessKey,
                            "Bima Sugam Android"
                    )
            );

            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "BROWSERSTACK ANDROID"
            );

            System.out.println(
                    "Device: " + deviceName
            );

            System.out.println(
                    "Android Version: " + platformVersion
            );

            System.out.println(
                    "App: " + app
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // CREATE BROWSERSTACK ANDROID DRIVER
            // =================================================

            driver.set(
                    new AndroidDriver(
                            new URL(browserStackUrl),
                            options
                    )
            );

            System.out.println(
                    "BrowserStack Android driver started successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not start BrowserStack Android driver.",
                    e
            );
        }
    }

    // =========================================================
    // BROWSERSTACK OPTIONS
    // =========================================================

    private static java.util.Map<String, Object>
    createBrowserStackOptions(
            String username,
            String accessKey,
            String testName
    ) {

        java.util.Map<String, Object> options =
                new java.util.HashMap<>();

        options.put(
                "userName",
                username
        );

        options.put(
                "accessKey",
                accessKey
        );

        options.put(
                "projectName",
                "Insurance Mobile Appium Framework"
        );

        options.put(
                "buildName",
                "Bima Sugam Mobile Build"
        );

        options.put(
                "sessionName",
                testName
        );

        options.put(
                "debug",
                true
        );

        options.put(
                "networkLogs",
                true
        );

        options.put(
                "video",
                true
        );

        options.put(
                "deviceLogs",
                true
        );

        return options;
    }

    // =========================================================
    // GET ENVIRONMENT VARIABLE
    // =========================================================

    private static String getEnvironmentVariable(
            String variableName
    ) {

        String value =
                System.getenv(variableName);

        // Also allow Maven/system-property override
        if (value == null || value.trim().isEmpty()) {

            value =
                    System.getProperty(variableName);
        }

        if (value != null) {

            value = value.trim();
        }

        return value;
    }

    // =========================================================
    // VALIDATE BROWSERSTACK CREDENTIALS
    // =========================================================

    private static void validateBrowserStackCredentials(
            String username,
            String accessKey
    ) {

        if (username == null
                || username.isEmpty()) {

            throw new RuntimeException(
                    "BROWSERSTACK_USERNAME is not set. " +
                    "Please export BROWSERSTACK_USERNAME."
            );
        }

        if (accessKey == null
                || accessKey.isEmpty()) {

            throw new RuntimeException(
                    "BROWSERSTACK_ACCESS_KEY is not set. " +
                    "Please export BROWSERSTACK_ACCESS_KEY."
            );
        }
    }

    // =========================================================
    // VALIDATE BROWSERSTACK APP
    // =========================================================

    private static void validateBrowserStackApp(
            String app,
            String environmentVariableName
    ) {

        if (app == null
                || app.isEmpty()) {

            throw new RuntimeException(
                    environmentVariableName
                            + " is not set.\n"
                            + "Upload/select the application in "
                            + "BrowserStack and set the corresponding "
                            + "bs:// App ID."
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