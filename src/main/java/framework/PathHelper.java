package framework;

import java.nio.file.Files;
import java.nio.file.Path;

public final class PathHelper {
    private PathHelper() {}

    public static void ensureDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException("Unable to create directory: " + path, e);
        }
    }
}
