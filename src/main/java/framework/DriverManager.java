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
        startDriver("Bima Sugam Test");
    }

    public static void startDriver(String testName) {

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
        System.out.println("TEST NAME: " + testName);
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

                startBrowserStackIOSDriver(testName);

            } else if (platform.equalsIgnoreCase("android")) {

                startBrowserStackAndroidDriver(testName);

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

    private static void startBrowserStackIOSDriver(String testName) {

        try {

            String username =
                    resolveProperty(
                            "BROWSERSTACK_USERNAME",
                            "browserstack.username"
                    );

            String accessKey =
                    resolveProperty(
                            "BROWSERSTACK_ACCESS_KEY",
                            "browserstack.access.key"
                    );

            String browserStackUrl =
                    config.getProperty(
                            "browserstack.url",
                            "https://hub-cloud.browserstack.com/wd/hub"
                    );

            String deviceName =
                    resolveProperty(
                            "BROWSERSTACK_IOS_DEVICE",
                            "browserstack.ios.device"
                    );

            String platformVersion =
                    resolveProperty(
                            "BROWSERSTACK_IOS_PLATFORM_VERSION",
                            "browserstack.ios.platform.version"
                    );

            String app =
                    resolveProperty(
                            "BROWSERSTACK_IOS_APP",
                            "browserstack.ios.app"
                    );

            validateBrowserStackCredentials(
                    username,
                    accessKey
            );

            validateBrowserStackApp(
                    app,
                    "BROWSERSTACK_IOS_APP"
            );

            URL remoteUrl =
                    getAuthenticatedBrowserStackUrl(
                            browserStackUrl,
                            username,
                            accessKey
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

            // BrowserStack credentials & options
            options.setCapability(
                    "bstack:options",
                    createBrowserStackOptions(
                            username,
                            accessKey,
                            testName != null && !testName.isEmpty() ? testName : "Bima Sugam iOS"
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
                    "Session: " + testName
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // CREATE BROWSERSTACK iOS DRIVER
            // =================================================

            driver.set(
                    new IOSDriver(
                            remoteUrl,
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

    private static void startBrowserStackAndroidDriver(String testName) {

        try {

            String username =
                    resolveProperty(
                            "BROWSERSTACK_USERNAME",
                            "browserstack.username"
                    );

            String accessKey =
                    resolveProperty(
                            "BROWSERSTACK_ACCESS_KEY",
                            "browserstack.access.key"
                    );

            String browserStackUrl =
                    config.getProperty(
                            "browserstack.url",
                            "https://hub-cloud.browserstack.com/wd/hub"
                    );

            String deviceName =
                    resolveProperty(
                            "BROWSERSTACK_ANDROID_DEVICE",
                            "browserstack.android.device"
                    );

            String platformVersion =
                    resolveProperty(
                            "BROWSERSTACK_ANDROID_PLATFORM_VERSION",
                            "browserstack.android.platform.version"
                    );

            String app =
                    resolveProperty(
                            "BROWSERSTACK_ANDROID_APP",
                            "browserstack.android.app"
                    );

            validateBrowserStackCredentials(
                    username,
                    accessKey
            );

            validateBrowserStackApp(
                    app,
                    "BROWSERSTACK_ANDROID_APP"
            );

            URL remoteUrl =
                    getAuthenticatedBrowserStackUrl(
                            browserStackUrl,
                            username,
                            accessKey
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

            // BrowserStack credentials & options
            options.setCapability(
                    "bstack:options",
                    createBrowserStackOptions(
                            username,
                            accessKey,
                            testName != null && !testName.isEmpty() ? testName : "Bima Sugam Android"
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
                    "Session: " + testName
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // CREATE BROWSERSTACK ANDROID DRIVER
            // =================================================

            driver.set(
                    new AndroidDriver(
                            remoteUrl,
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
    // AUTHENTICATED BROWSERSTACK HUB URL
    // =========================================================

    private static URL getAuthenticatedBrowserStackUrl(
            String browserStackUrl,
            String username,
            String accessKey
    ) throws Exception {

        if (browserStackUrl == null || browserStackUrl.trim().isEmpty()) {
            browserStackUrl = "https://hub-cloud.browserstack.com/wd/hub";
        }

        browserStackUrl = browserStackUrl.trim();

        if (browserStackUrl.contains("@")) {
            return new URL(browserStackUrl);
        }

        if (browserStackUrl.startsWith("https://")) {
            return new URL("https://" + username + ":" + accessKey + "@" + browserStackUrl.substring(8));
        } else if (browserStackUrl.startsWith("http://")) {
            return new URL("http://" + username + ":" + accessKey + "@" + browserStackUrl.substring(7));
        }

        return new URL(browserStackUrl);
    }

    // =========================================================
    // RESOLVE PROPERTY (Env > Sys Prop > config.properties)
    // =========================================================

    private static String resolveProperty(
            String envName,
            String configKey
    ) {

        // 1. Check System environment
        String value = envName != null ? System.getenv(envName) : null;

        // 2. Check System properties (-D)
        if ((value == null || value.trim().isEmpty()) && envName != null) {
            value = System.getProperty(envName);
        }

        if ((value == null || value.trim().isEmpty()) && configKey != null) {
            value = System.getProperty(configKey);
        }

        // 3. Check config.properties
        if ((value == null || value.trim().isEmpty()) && configKey != null) {
            value = config.getProperty(configKey);
        }

        // 4. Resolve template placeholder like ${VAR_NAME}
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String placeholder = value.substring(2, value.length() - 1).trim();
            String resolved = System.getenv(placeholder);
            if (resolved == null || resolved.trim().isEmpty()) {
                resolved = System.getProperty(placeholder);
            }
            value = resolved;
        }

        if (value != null) {
            value = value.trim();
            if (value.isEmpty()) {
                value = null;
            }
        }

        return value;
    }

    // =========================================================
    // GET ENVIRONMENT VARIABLE
    // =========================================================

    private static String getEnvironmentVariable(
            String variableName
    ) {

        return resolveProperty(variableName, null);
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