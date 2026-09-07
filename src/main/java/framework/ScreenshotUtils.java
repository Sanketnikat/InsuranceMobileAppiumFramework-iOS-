package framework;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {
    private ScreenshotUtils() {}

    public static String capture(AppiumDriver driver, String testName) {
        try {
            PathHelper.ensureDirectory("screenshots");

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path target = Path.of("screenshots",
                    testName + "_" + timestamp + ".png");

            Files.copy(source.toPath(), target,
                    StandardCopyOption.REPLACE_EXISTING);

            return target.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
