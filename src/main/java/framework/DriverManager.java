package framework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.FileInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {

        private static final ThreadLocal<AppiumDriver> driver =
            new ThreadLocal<>();

            private static final ThreadLocal<WebDriver> sessionDriver =
                    new ThreadLocal<>();

    private static Properties config;

    // =========================================================
    // LOAD CONFIGURATION
    // =========================================================

    static {
        try {
            config = new Properties();

            java.io.InputStream input = null;
            try {
                input = DriverManager.class.getResourceAsStream("/config.properties");
                if (input == null) {
                    input = new FileInputStream("src/test/resources/config.properties");
                }
                if (input != null) {
                    config.load(input);
                }
            } finally {
                if (input != null) {
                    input.close();
                }
            }

            if (config.isEmpty()) {
                throw new RuntimeException("config.properties is empty or not found.");
            }

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
                startDriver(testName, "mixed-sprint");
        }

        public static void startDriver(String testName, String sprint) {
                startDriver(testName, sprint, null, null, null, null);
            }

            public static void startDriver(
                    String testName,
                    String sprint,
                    String platformOverride,
                    String deviceNameOverride,
                    String platformVersionOverride,
                    String appOverride
            ) {

                startDriver(
                        testName,
                        sprint,
                        platformOverride,
                        deviceNameOverride,
                        platformVersionOverride,
                        appOverride,
                        null
                );
            }

            public static void startDriver(
                    String testName,
                    String sprint,
                    String platformOverride,
                    String deviceNameOverride,
                    String platformVersionOverride,
                    String appOverride,
                    String executionTypeOverride
            ) {

        String execution =
                config.getProperty(
                        "execution",
                        "local"
                );

                String executionType = firstConfiguredValue(
                        executionTypeOverride,
                        value("executionType", "")
                );

                if (executionType != null && !executionType.isBlank()) {
                    if (executionType.equalsIgnoreCase("PWA_ANDROID")) {
                        platformOverride = "android";
                    } else if (executionType.equalsIgnoreCase("PWA_IOS")) {
                        platformOverride = "ios";
                    }
                }

        String platform = firstConfiguredValue(platformOverride, "platform");
        if (platform != null && (platform.equalsIgnoreCase("platform") || platform.equalsIgnoreCase("deviceName") || platform.equalsIgnoreCase("platformVersion") || platform.equalsIgnoreCase("app"))) {
            platform = value("platform", null);
        }

        System.out.println("======================================");
        System.out.println("EXECUTION: " + execution);
        System.out.println("EXECUTION TYPE: " + (executionType == null || executionType.isBlank() ? "NATIVE_APP" : executionType));
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

                if (executionType != null && executionType.equalsIgnoreCase("PWA_ANDROID")) {
                        requireBrowserStack(execution);
                        startBrowserStackPwaAndroidDriver(testName, sprint, deviceNameOverride, platformVersionOverride);

                } else if (executionType != null && executionType.equalsIgnoreCase("PWA_IOS")) {
                        requireBrowserStack(execution);
                        startBrowserStackPwaIosDriver(testName, sprint, deviceNameOverride, platformVersionOverride);

                } else if (execution.equalsIgnoreCase("local")) {

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

                startBrowserStackIOSDriver(
                        testName,
                        sprint,
                        deviceNameOverride,
                        platformVersionOverride,
                        appOverride
                );

            } else if (platform.equalsIgnoreCase("android")) {

                startBrowserStackAndroidDriver(
                        testName,
                        sprint,
                        deviceNameOverride,
                        platformVersionOverride,
                        appOverride
                );

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

            private static void startBrowserStackIOSDriver(
                    String testName,
                    String sprint,
                    String deviceNameOverride,
                    String platformVersionOverride,
                    String appOverride
            ) {

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

            String deviceName = firstConfiguredValue(
                    deviceNameOverride,
                    resolveProperty(
                            "BROWSERSTACK_IOS_DEVICE",
                            "browserstack.ios.device"
                    )
            );

            String platformVersion = firstConfiguredValue(
                    platformVersionOverride,
                    resolveProperty(
                            "BROWSERSTACK_IOS_PLATFORM_VERSION",
                            "browserstack.ios.platform.version"
                    )
            );

            String app = firstConfiguredValue(
                    appOverride,
                    resolveProperty(
                            "BROWSERSTACK_IOS_APP",
                            "browserstack.ios.app"
                    )
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
                            testName != null && !testName.isEmpty() ? testName : "Bima Sugam iOS",
                            sprint
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

        private static void startBrowserStackAndroidDriver(
                String testName,
                String sprint,
                String deviceNameOverride,
                String platformVersionOverride,
                String appOverride
        ) {

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

            String deviceName = firstConfiguredValue(
                    deviceNameOverride,
                    resolveProperty(
                            "BROWSERSTACK_ANDROID_DEVICE",
                            "browserstack.android.device"
                    )
            );

            String platformVersion = firstConfiguredValue(
                    platformVersionOverride,
                    resolveProperty(
                            "BROWSERSTACK_ANDROID_PLATFORM_VERSION",
                            "browserstack.android.platform.version"
                    )
            );

            String app = firstConfiguredValue(
                    appOverride,
                    resolveProperty(
                            "BROWSERSTACK_ANDROID_APP",
                            "browserstack.android.app"
                    )
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
                            testName != null && !testName.isEmpty() ? testName : "Bima Sugam Android",
                            sprint
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

    private static void startBrowserStackPwaAndroidDriver(
            String testName,
            String sprint,
            String deviceNameOverride,
            String platformVersionOverride
    ) {
        try {
            String username = resolveProperty("BROWSERSTACK_USERNAME", "browserstack.username");
            String accessKey = resolveProperty("BROWSERSTACK_ACCESS_KEY", "browserstack.access.key");
            String browserStackUrl = config.getProperty(
                    "browserstack.url",
                    "https://hub-cloud.browserstack.com/wd/hub"
            );
            String deviceName = firstConfiguredValue(
                    deviceNameOverride,
                    resolveProperty("BROWSERSTACK_PWA_ANDROID_DEVICE", "pwa.android.device")
            );
            String platformVersion = firstConfiguredValue(
                    platformVersionOverride,
                    resolveProperty("BROWSERSTACK_PWA_ANDROID_PLATFORM_VERSION", "pwa.android.platform.version")
            );

            validateBrowserStackCredentials(username, accessKey);

            URL remoteUrl = getAuthenticatedBrowserStackUrl(browserStackUrl, username, accessKey);
            ChromeOptions options = new ChromeOptions();
            options.setCapability("browserName", "Chrome");
            options.setCapability(
                    "bstack:options",
                    createBrowserStackBrowserOptions(
                            username,
                            accessKey,
                            testName,
                            sprint,
                            "android",
                            deviceName,
                            platformVersion
                    )
            );
            sessionDriver.set(new RemoteWebDriver(remoteUrl, options));
            navigateToPwa("PWA_ANDROID", testName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start BrowserStack Android PWA driver.", e);
        }
    }

    private static void startBrowserStackPwaIosDriver(
            String testName,
            String sprint,
            String deviceNameOverride,
            String platformVersionOverride
    ) {
        try {
            String username = resolveProperty("BROWSERSTACK_USERNAME", "browserstack.username");
            String accessKey = resolveProperty("BROWSERSTACK_ACCESS_KEY", "browserstack.access.key");
            String browserStackUrl = config.getProperty(
                    "browserstack.url",
                    "https://hub-cloud.browserstack.com/wd/hub"
            );
            String deviceName = firstConfiguredValue(
                    deviceNameOverride,
                    resolveProperty("BROWSERSTACK_PWA_IOS_DEVICE", "pwa.ios.device")
            );
            String platformVersion = firstConfiguredValue(
                    platformVersionOverride,
                    resolveProperty("BROWSERSTACK_PWA_IOS_PLATFORM_VERSION", "pwa.ios.platform.version")
            );

            validateBrowserStackCredentials(username, accessKey);

            URL remoteUrl = getAuthenticatedBrowserStackUrl(browserStackUrl, username, accessKey);
            SafariOptions options = new SafariOptions();
            options.setCapability("browserName", "Safari");
            options.setCapability(
                    "bstack:options",
                    createBrowserStackBrowserOptions(
                            username,
                            accessKey,
                            testName,
                            sprint,
                            "ios",
                            deviceName,
                            platformVersion
                    )
            );

            sessionDriver.set(new RemoteWebDriver(remoteUrl, options));
            navigateToPwa("PWA_IOS", testName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start BrowserStack iOS PWA driver.", e);
        }
    }

    private static java.util.Map<String, Object> createBrowserStackBrowserOptions(
            String username,
            String accessKey,
            String testName,
            String sprint,
            String os,
            String deviceName,
            String platformVersion
    ) {
        java.util.Map<String, Object> options = createBrowserStackOptions(
                username,
                accessKey,
                testName,
                sprint
        );
        options.put("deviceName", deviceName);
        options.put("os", os);
        options.put("osVersion", platformVersion);
        return options;
    }

    private static void navigateToPwa(String executionType, String testName) {
        String pwaUrl = value(
                "pwa.url",
                "https://marketplace-test.bsifinternal.com/#/login"
        );
        driver.get().get(pwaUrl);
        System.out.println("BrowserStack " + executionType + " session started: " + testName);
        System.out.println("PWA URL: " + pwaUrl);
    }

    private static void requireBrowserStack(String execution) {
        if (!"browserstack".equalsIgnoreCase(execution)) {
            throw new RuntimeException(
                    "PWA execution requires execution=browserstack because the PWA is configured for BrowserStack mobile web sessions."
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
            String testName,
            String sprint
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
                value("projectName", "Bima-Sugam-Mobile")
        );

        String configuredBuildName = value("buildName", "");
        String buildName = configuredBuildName.isEmpty()
                ? "Sprint-" + sprint
                : configuredBuildName;

        options.put(
                "buildName",
                buildName
        );

        String buildTag = System.getenv("BSTACK_BUILD_TAG");
        if (buildTag == null || buildTag.trim().isEmpty()) {
            buildTag = value("buildTag", "");
        }
        if (!buildTag.isEmpty()) {
            options.put("buildTag", buildTag);
        }

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

        String networkProfile = value("networkProfile", "");
        if (!networkProfile.isEmpty()) {
            options.put("networkProfile", networkProfile);
        }

        String local = resolveProperty("BROWSERSTACK_LOCAL", "browserstack.local");
        if (local != null && Boolean.parseBoolean(local.trim())) {
            options.put("local", true);

            String localIdentifier = resolveProperty("BROWSERSTACK_LOCAL_IDENTIFIER", "browserstack.local.identifier");
            if (localIdentifier != null && !localIdentifier.isEmpty()) {
                options.put("localIdentifier", localIdentifier);
            }
        }

        return options;
    }

        private static String value(String key, String defaultValue) {
                String value = System.getProperty(key);
                if (value == null || value.trim().isEmpty()) {
                        value = config.getProperty(key);
                }
                return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
        }

        private static String firstConfiguredValue(String override, String fallback) {
                String candidate = override;

                if (candidate == null || candidate.trim().isEmpty() || candidate.equalsIgnoreCase("null")) {
                        candidate = fallback;
                } else {
                        String trimmed = candidate.trim();
                        if (trimmed.equalsIgnoreCase("platform")
                                || trimmed.equalsIgnoreCase("deviceName")
                                || trimmed.equalsIgnoreCase("platformVersion")
                                || trimmed.equalsIgnoreCase("app")
                                || trimmed.equalsIgnoreCase(fallback == null ? "" : fallback)) {
                                candidate = fallback;
                        } else {
                                candidate = trimmed;
                        }
                }

                if (candidate == null || candidate.trim().isEmpty()) {
                        return null;
                }

                return candidate.trim();
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

        java.util.List<String> candidateKeys = new java.util.ArrayList<>();

        if (envName != null) {
            candidateKeys.add(envName);
            candidateKeys.add(envName.toUpperCase());
            candidateKeys.add(envName.toLowerCase());
            candidateKeys.add(envName.replace('_', '.'));
        }

        if (configKey != null) {
            candidateKeys.add(configKey);
            candidateKeys.add(configKey.toUpperCase());
            candidateKeys.add(configKey.toLowerCase());
            candidateKeys.add(configKey.replace('.', '_'));
        }

        for (String candidate : candidateKeys) {
            if (candidate == null || candidate.trim().isEmpty()) {
                continue;
            }

            String value = System.getenv(candidate);
            if (value == null || value.trim().isEmpty()) {
                value = System.getProperty(candidate);
            }
            if (value == null || value.trim().isEmpty()) {
                value = config.getProperty(candidate);
            }

            if (value != null && !value.trim().isEmpty()) {
                value = value.trim();
                if (value.startsWith("${") && value.endsWith("}")) {
                    String placeholder = value.substring(2, value.length() - 1).trim();
                    String resolved = System.getenv(placeholder);
                    if (resolved == null || resolved.trim().isEmpty()) {
                        resolved = System.getProperty(placeholder);
                    }
                    if (resolved != null && !resolved.trim().isEmpty()) {
                        value = resolved.trim();
                    }
                }
                return value;
            }
        }

        return null;
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

        public static WebDriver getSessionDriver() {
                WebDriver activeSession = sessionDriver.get();
                return activeSession != null ? activeSession : driver.get();
        }

    // =========================================================
    // QUIT DRIVER
    // =========================================================

    public static void quitDriver() {

                WebDriver activeSession = sessionDriver.get();
                if (activeSession != null) {

            try {
                                activeSession.quit();

            } finally {
                                sessionDriver.remove();
            }
        }

                driver.remove();
    }

    // =========================================================
    // GET CONFIG
    // =========================================================

    public static Properties getConfig() {

        return config;
    }
}