package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseTest {

    protected AppiumDriver driver;

    protected VideoRecorder videoRecorder;

    private final Map<String, ExtentTest> extentTests =
            new LinkedHashMap<>();

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {

        // Start Android or iOS based on configuration
        DriverManager.startDriver(result.getMethod().getMethodName());

        driver = DriverManager.getDriver();

        for (String sprint : sprintGroups(result)) {
            ExtentReports extent = ReportManager.getExtent(sprint);
            extentTests.put(
                    sprint,
                    extent.createTest(
                            result.getMethod().getMethodName(),
                            result.getMethod().getDescription()
                    )
            );
        }

        addExecutionInfo();

        // Start video recording
        videoRecorder = new VideoRecorder();

        videoRecorder.start(
                driver,
                result.getMethod().getMethodName()
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        try {

            if (result.getStatus()
                    == ITestResult.FAILURE) {

                String screenshot =
                        ScreenshotUtils.capture(
                                driver,
                                testName
                        );

                extentTests.values().forEach(test -> test.fail("Test failed"));

                if (screenshot != null) {

                                        extentTests.values().forEach(test -> {
                                                try {
                                                        test.addScreenCaptureFromPath(screenshot);
                                                } catch (Exception ignored) {
                                                }
                                        });

                    attachScreenshotToAllure(
                            screenshot
                    );
                }

                Allure.addAttachment(
                        "Failure Reason",
                        "text/plain",
                        result.getThrowable() == null
                                ? "Unknown failure"
                                : result.getThrowable()
                                    .toString()
                );

            } else if (
                    result.getStatus()
                            == ITestResult.SUCCESS
            ) {

                extentTests.values().forEach(test -> test.pass("Test passed"));

            } else {

                extentTests.values().forEach(test -> test.skip("Test skipped"));
            }

        } catch (Exception reportingException) {

            extentTests.values().forEach(test -> test.warning(
                    "Reporting issue: " + reportingException.getMessage()
            ));

        } finally {

            // Stop video
            if (videoRecorder != null) {

                String video =
                        videoRecorder.stopAndPull(
                                testName
                        );

                if (video != null) {

                    extentTests.values().forEach(test -> test.info(
                            "Video: " + video
                    ));

                    attachVideoToAllure(video);
                }
            }

            // Update session status on BrowserStack if running in cloud
            updateBrowserStackStatus(result);

            // Quit Appium
            DriverManager.quitDriver();

            // Flush Extent report
            ReportManager.flushAll();
        }
    }

    private void updateBrowserStackStatus(ITestResult result) {
        if (driver == null) {
            return;
        }

        String execution = ConfigReader.get("execution");
        if (!"browserstack".equalsIgnoreCase(execution)) {
            return;
        }

        try {
            org.openqa.selenium.JavascriptExecutor jse =
                    (org.openqa.selenium.JavascriptExecutor) driver;

            if (result.getStatus() == ITestResult.SUCCESS) {
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Test passed successfully\"}}");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                String failureReason = result.getThrowable() != null
                        ? result.getThrowable().getMessage()
                        : "Test failed";
                if (failureReason != null) {
                    failureReason = failureReason.replaceAll("[\"\r\n]", " ");
                    if (failureReason.length() > 200) {
                        failureReason = failureReason.substring(0, 200) + "...";
                    }
                } else {
                    failureReason = "Test failed";
                }
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"" + failureReason + "\"}}");
            }
        } catch (Exception ignored) {
            // Ignore if driver session is already closed or does not support javascript execution
        }
    }

    /**
     * Records a test step in Allure and Extent.
     */
    protected void step(String message) {

        Allure.step(message);

                extentTests.values().forEach(test -> test.info(message));
    }

        private String[] sprintGroups(ITestResult result) {
        String[] groups = result.getMethod().getGroups();

        if (groups != null && groups.length > 0) {
            String[] matched = java.util.Arrays.stream(groups)
                    .filter(group -> group != null && !group.trim().isEmpty())
                    .map(String::trim)
                    .filter(group -> group.toLowerCase().contains("sprint"))
                    .distinct()
                    .toArray(String[]::new);

            if (matched.length > 0) {
                return matched;
            }
        }

        String fallback = defaultSprintName(result);
        return fallback != null && !fallback.trim().isEmpty()
                ? new String[]{fallback}
                : new String[]{"mixed-sprint"};
    }

    private String defaultSprintName(ITestResult result) {
        String className = result.getTestClass() != null
                ? result.getTestClass().getName()
                : "";

        if (className != null && className.toLowerCase().contains("sprint")) {
            String[] parts = className.split("\\.");
            for (String part : parts) {
                if (part.toLowerCase().contains("sprint")) {
                    return part.trim();
                }
            }
        }

        return "sprint 1 and 2";
    }

    /**
     * Adds execution information to reports.
     */
    private void addExecutionInfo() {

        extentTests.values().forEach(test -> {
            test.info("Build: " + value("build"));
            test.info("Environment: " + value("environment"));
            test.info("Platform: " + value("platform"));
            test.info("Device Type: " + value("deviceType"));
            test.info("Device: " + value("device.name"));
        });
    }

    /**
     * Adds screenshot to Allure.
     */
    private void attachScreenshotToAllure(
            String screenshot
    ) {

        try (
                InputStream input =
                        Files.newInputStream(
                                Path.of(screenshot)
                        )
        ) {

            Allure.addAttachment(
                    "Failure Screenshot",
                    "image/png",
                    input,
                    ".png"
            );

        } catch (Exception ignored) {
        }
    }

    /**
     * Adds video to Allure.
     */
    private void attachVideoToAllure(
            String video
    ) {

        try (
                InputStream input =
                        Files.newInputStream(
                                Path.of(video)
                        )
        ) {

            Allure.addAttachment(
                    "Execution Video",
                    "video/mp4",
                    input,
                    ".mp4"
            );

        } catch (Exception ignored) {
        }
    }

    /**
     * Gets JVM property first,
     * otherwise config.properties value.
     */
    private String value(String key) {

        String systemValue =
                System.getProperty(key);

        return systemValue != null
                ? systemValue
                : ConfigReader.get(key);
    }
}