# Insurance Mobile Appium Automation Framework

A simple sprint-wise Appium automation framework for local Android testing.

## Technology

- Java 25
- Maven
- Appium Java Client
- TestNG
- Android UiAutomator2
- Allure
- Extent Reports
- ADB screen recording
- Android emulator
- Physical Android device

## Sprint structure

Sprint 1 = Login
Sprint 2 = Two Wheeler
Sprint 3 = Quote
Sprint 4 = Policy
Sprint 5 = End-to-End / Regression

Each sprint gets its own test package and test-data folder.

## Important

The application package/activity and Login locators are placeholders until the real application is available.

Update:

src/test/resources/config.properties

and:

src/test/java/tests/sprint1/LoginPage.java

with the actual values.

## Start Appium

appium

## Check device

adb devices

A single connected emulator or phone is detected automatically.

If multiple devices are connected:

mvn clean test -Ddevice.udid=YOUR_DEVICE_ID

## Run

mvn clean test

## Build number

mvn clean test -Dbuild=1.0.25

## Environment

mvn clean test -Denvironment=QA

## Physical Android device

1. Enable Developer Options and USB Debugging.
2. Connect the phone.
3. Run adb devices.
4. If only one device is connected, run mvn clean test.
5. If multiple devices are connected, use -Ddevice.udid=DEVICE_ID.

## Reports

Extent:
reports/extent/ExtentReport.html

Allure results:
target/allure-results

Allure report:
mvn allure:report

Allure server:
mvn allure:serve

## Screenshots

Failure screenshots:
screenshots/

## Videos

Local Android execution videos:
videos/

The framework starts/stops adb screenrecord automatically for each test.
Android screenrecord has OS/device limitations and a maximum duration configured in config.properties.

## Adding future sprints

Create:

src/test/java/tests/sprint2
src/test/java/tests/sprint3
src/test/java/tests/sprint4
src/test/java/tests/sprint5

and matching test-data folders.

Do not copy the framework classes into each sprint. BaseTest, DriverManager, waits, screenshots, video, and reports are common to all sprints.
