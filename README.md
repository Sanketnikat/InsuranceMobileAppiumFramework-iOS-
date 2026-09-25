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

## BrowserStack SDK

The BrowserStack Java SDK dependency and project-root `browserstack.yml` are included for the BrowserStack integration. The current framework creates its Appium sessions through `DriverManager`, so the SDK Java agent is not enabled in Surefire; enabling it with the existing suite causes TestNG to report zero executed tests.

Export credentials and the sprint metadata before running:

export BROWSERSTACK_USERNAME=YOUR_USERNAME
export BROWSERSTACK_ACCESS_KEY=YOUR_ACCESS_KEY
export BROWSERSTACK_IOS_APP=bs://YOUR_IOS_APP_ID
export BROWSERSTACK_ANDROID_APP=bs://YOUR_ANDROID_APP_ID
export BSTACK_BUILD_NAME=Sprint-1
export BSTACK_BUILD_TAG=BD2M-609

Run one test case:

mvn test -Dtest=tests.sprint1.TC05_ValidIndianMobileNumberTest#TC05_validIndianMobileNumberIsAccepted

Run the configured suite:

mvn clean test

The SDK groups sessions under `BSTACK_BUILD_NAME`, while the framework adds the sprint, test method, and TestNG description to each session name.

## BrowserStack PWA mobile web execution

The same framework supports the native app execution types and opt-in PWA browser sessions:

- `ANDROID_APP` - BrowserStack Android APK session
- `IOS_APP` - BrowserStack iOS IPA session
- `PWA_ANDROID` - BrowserStack real Android Chrome session
- `PWA_IOS` - BrowserStack real iPhone Safari session

The PWA URL is configured with `pwa.url`. PWA sessions use browser capabilities and do not use a `bs://` app ID.

Run the PWA smoke test on Android Chrome:

```sh
mvn test -Dtest=tests.pwa.PwaLoginPageLoadTest \
	-DexecutionType=PWA_ANDROID \
	-DBROWSERSTACK_USERNAME="$BROWSERSTACK_USERNAME" \
	-DBROWSERSTACK_ACCESS_KEY="$BROWSERSTACK_ACCESS_KEY"
```

Run it on iOS Safari by changing `PWA_ANDROID` to `PWA_IOS`. Native tests continue to use their existing APK/IPA configuration when `executionType` is `ANDROID_APP` or `IOS_APP`.

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
