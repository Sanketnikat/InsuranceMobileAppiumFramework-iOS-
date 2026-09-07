package framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public final class VideoRecorder {
    private Process recordingProcess;
    private String remoteFile;

    public void start(String testName) {
        if (!ConfigReader.getBoolean("video.enabled", true)) {
            return;
        }

        try {
            PathHelper.ensureDirectory("videos");

            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            remoteFile = "/sdcard/" + safeName + ".mp4";

            recordingProcess = new ProcessBuilder(
                    "adb", "shell", "screenrecord",
                    "--time-limit",
                    ConfigReader.get("video.max.seconds"),
                    remoteFile
            ).redirectErrorStream(true).start();

        } catch (Exception e) {
            System.err.println("Video recording could not start: " + e.getMessage());
        }
    }

    public String stopAndPull(String testName) {
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
}
