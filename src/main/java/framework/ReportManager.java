package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public final class ReportManager {
    private static ExtentReports extent;

    private ReportManager() {}

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            PathHelper.ensureDirectory("reports/extent");

            ExtentSparkReporter reporter =
                    new ExtentSparkReporter("reports/extent/ExtentReport.html");

            extent = new ExtentReports();
            extent.attachReporter(reporter);

            extent.setSystemInfo("Build", value("build"));
            extent.setSystemInfo("Environment", value("environment"));
            extent.setSystemInfo("Platform", value("platform"));
            extent.setSystemInfo("Device Type", value("deviceType"));
            extent.setSystemInfo("Device", value("device.name"));
        }

        return extent;
    }

    private static String value(String key) {
        String systemValue = System.getProperty(key);
        return systemValue != null ? systemValue : ConfigReader.get(key);
    }
}
