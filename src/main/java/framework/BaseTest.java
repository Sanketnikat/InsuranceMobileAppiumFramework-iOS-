package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseTest {
    protected AndroidDriver driver;
    protected VideoRecorder videoRecorder;

    private static final ExtentReports EXTENT = ReportManager.getExtent();
    private ExtentTest extentTest;

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        DriverManager.startAndroidDriver();
        driver = DriverManager.getDriver();

        extentTest = EXTENT.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );

        addExecutionInfo();

        videoRecorder = new VideoRecorder();
        videoRecorder.start(result.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshot = ScreenshotUtils.capture(driver, testName);

                extentTest.fail("Test failed");

                if (screenshot != null) {
                    extentTest.addScreenCaptureFromPath(screenshot);
                    attachScreenshotToAllure(screenshot);
                }

                Allure.addAttachment("Failure Reason",
                        "text/plain",
                        result.getThrowable() == null
                                ? "Unknown failure"
                                : result.getThrowable().toString());

            } else if (result.getStatus() == ITestResult.SUCCESS) {
                extentTest.pass("Test passed");
            } else {
                extentTest.skip("Test skipped");
            }

        } catch (Exception reportingException) {
            extentTest.warning("Reporting issue: " +
                    reportingException.getMessage());
        } finally {
            String video = videoRecorder.stopAndPull(testName);

            if (video != null) {
                extentTest.info("Video: " + video);
                attachVideoToAllure(video);
            }

            DriverManager.quitDriver();
            EXTENT.flush();
        }
    }

    protected void step(String message) {
        Allure.step(message);
        if (extentTest != null) {
            extentTest.info(message);
        }
    }

    private void addExecutionInfo() {
        extentTest.info("Build: " + value("build"));
        extentTest.info("Environment: " + value("environment"));
        extentTest.info("Platform: " + value("platform"));
        extentTest.info("Device Type: " + value("deviceType"));
        extentTest.info("Device: " + value("device.name"));
    }

    private void attachScreenshotToAllure(String screenshot) {
        try (InputStream input = Files.newInputStream(Path.of(screenshot))) {
            Allure.addAttachment("Failure Screenshot",
                    "image/png", input, ".png");
        } catch (Exception ignored) {
        }
    }

    private void attachVideoToAllure(String video) {
        try (InputStream input = Files.newInputStream(Path.of(video))) {
            Allure.addAttachment("Execution Video",
                    "video/mp4", input, ".mp4");
        } catch (Exception ignored) {
        }
    }

    private String value(String key) {
        String systemValue = System.getProperty(key);
        return systemValue != null ? systemValue : ConfigReader.get(key);
    }
}
