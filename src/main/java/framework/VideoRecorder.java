package framework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Base64;

public final class VideoRecorder {
    private AppiumDriver driver;
    private Process recordingProcess;
    private String remoteFile;
    private boolean appiumRecording;

    public void start(AppiumDriver driver, String testName) {
        if (!ConfigReader.getBoolean("video.enabled", true)) {
            return;
        }

        try {
            this.driver = driver;
            PathHelper.ensureDirectory("videos");

            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");

            if (ConfigReader.get("platform").equalsIgnoreCase("ios")) {
                screenRecorder().startRecordingScreen(
                        new IOSStartScreenRecordingOptions()
                                .withTimeLimit(Duration.ofSeconds(180))
                );
                appiumRecording = true;
                return;
            }

            remoteFile = "/sdcard/" + safeName + ".mp4";
            recordingProcess = new ProcessBuilder(
                    "adb", "shell", "screenrecord",
                    "--time-limit",
                    videoMaxSeconds(),
                    remoteFile
            ).redirectErrorStream(true).start();

        } catch (Exception e) {
            System.err.println("Video recording could not start: " + e.getMessage());
        }
    }

    public String stopAndPull(String testName) {
        if (appiumRecording) {
            return stopAppiumRecording(testName);
        }

        if (recordingProcess == null || remoteFile == null) {
            return null;
        }

        try {
            recordingProcess.destroy();
            recordingProcess.waitFor();

            Path localFile = Path.of("videos",
                    testName.replaceAll("[^a-zA-Z0-9._-]", "_") + ".mp4");
            Process pullProcess = new ProcessBuilder(
                    "adb", "pull", remoteFile, localFile.toString())
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(pullProcess.getInputStream()))) {
                while (reader.readLine() != null) {
                    // Consume output.
                }
            }

            pullProcess.waitFor();
            new ProcessBuilder("adb", "shell", "rm", remoteFile)
                    .start()
                    .waitFor();

            return Files.exists(localFile) ? localFile.toString() : null;

        } catch (Exception e) {
            System.err.println("Video could not be saved: " + e.getMessage());
            return null;
        } finally {
            recordingProcess = null;
            remoteFile = null;
        }
    }

    private String stopAppiumRecording(String testName) {
        Path localFile = Path.of(
                "videos",
                testName.replaceAll("[^a-zA-Z0-9._-]", "_") + ".mp4"
        );

        try {
            String recording = screenRecorder().stopRecordingScreen();

            if (recording == null || recording.isBlank()) {
                return null;
            }

            Files.write(
                    localFile,
                    Base64.getDecoder().decode(recording),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return Files.exists(localFile) ? localFile.toString() : null;
        } catch (Exception e) {
            System.err.println("iOS video could not be saved: " + e.getMessage());
            return null;
        } finally {
            driver = null;
            appiumRecording = false;
        }
    }

    private String videoMaxSeconds() {
        String configuredValue = ConfigReader.get("video.max.seconds");
        return configuredValue == null ? "180" : configuredValue;
    }

    private CanRecordScreen screenRecorder() {
        return (CanRecordScreen) driver;
    }
}
