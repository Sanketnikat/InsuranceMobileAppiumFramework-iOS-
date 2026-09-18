package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ReportManager {
    private static final Map<String, ExtentReports> EXTENTS =
            new ConcurrentHashMap<>();

    private ReportManager() {}

    public static synchronized ExtentReports getExtent(String sprint) {
        return EXTENTS.computeIfAbsent(sprint, ReportManager::createExtent);
    }

    public static synchronized void flushAll() {
        EXTENTS.values().forEach(ExtentReports::flush);
    }

    private static ExtentReports createExtent(String sprint) {
        String reportDirectory = "reports/extent/" + sprint;
        PathHelper.ensureDirectory(reportDirectory);

        ExtentSparkReporter reporter =
                new ExtentSparkReporter(
                        reportDirectory + "/ExtentReport.html"
                );

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);

        extent.setSystemInfo("Sprint", sprint);
        extent.setSystemInfo("Build", value("build"));
        extent.setSystemInfo("Environment", value("environment"));
        extent.setSystemInfo("Platform", value("platform"));
        extent.setSystemInfo("Device Type", value("deviceType"));
        extent.setSystemInfo("Device", value("device.name"));

        return extent;
    }

    private static String value(String key) {
        String systemValue = System.getProperty(key);
        return systemValue != null ? systemValue : ConfigReader.get(key);
    }
}
