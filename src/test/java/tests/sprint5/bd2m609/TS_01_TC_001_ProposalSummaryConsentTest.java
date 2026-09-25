package tests.sprint5.bd2m609;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Locale;
import java.util.List;

public class TS_01_TC_001_ProposalSummaryConsentTest extends BaseTest {
private WebDriverWait wait;

    private static final String MOBILE_NUMBER = "8921639271";
    private static final String OTP = "123456";
    private static final String EMAIL = "sanket.nikat@gmail.com";

    private static final String FIRST_NAME = "Sanket";
    private static final String MIDDLE_NAME = "Sudam";
    private static final String LAST_NAME = "Nikat";
    private static final String DOB = "05/05/2000";
    private static final String GENDER = "Male";

    private static final int PRICE_BUTTON_INDEX = 1;

    @Test(
            description = "TS_01_TC_001 - Validate loading of the complete proposal draft on the Summary & Consent screen",
            groups = {"sprint5", "BD2M-609", "proposal", "positive"}
    )
    public void proposalSummaryConsentTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Welcome Screen
        clickText("Login");

        // Login Screen
        clickText("Private Bike Insurance");

        // Bike Insurance
        clickText("Brand new bike?");

        // Quote page is reached after selecting Brand new bike?; the exact title text can be inconsistent in iOS
        waitForAnyVisibleText("View Prices", "Get your bike insurance quote", "Get your bike insuarance quote");
        System.out.println("STEP 4: Quote page reached successfully.");

        // Required quote form fields must be selected before View Prices is enabled.
        // The real iOS form is order-driven after the first selection, so prefer the next visible
        // dropdown instead of relying on a static label that may not be present yet.
        
        selectMakeAndModel("ADMS GTR");
        selectNextVisibleQuoteField("ELECTRIC");
        selectNextVisibleQuoteField("STANDARD");
        selectNextVisibleQuoteField("Chittoor");
        selectNextVisibleQuoteField("Individual");

        waitForQuoteFormToSettle();
        wait.until(webDriver -> findVisibleElement(
            webDriver,
            List.of(
                AppiumBy.accessibilityId("View Prices"),
                By.xpath("//*[@name='View Prices' or @label='View Prices' or @value='View Prices']")
            )
        ));
        clickText("View Prices");

        // Personal Accident Cover
        clickText("3 Years");
        clickText("Confirm");

        // Insurer Listing
        selectPricePack(PRICE_BUTTON_INDEX);

        // Confirm Plan
        expandBasePlan();

        // Pre-Proposal Form - Bike Owner Details
        fillBikeOwnerDetails();
        verifyTextDisplayed(GENDER);

        // Contact Details
        enterMobileNumber();
        clickText("Get OTP");
        enterOTP();
        enterEmail();
        selectConsent();

        // Submit Pre-Proposal
        clickText("Save & Next");

        // Result
        verifyProposalForm();

        System.out.println();
        System.out.println("=================================================");
        System.out.println("TS_01_TC_001 EXECUTION COMPLETED SUCCESSFULLY");
        System.out.println("Proposal Summary & Consent flow completed.");
        System.out.println("=================================================");
    }

    private void selectValueIfRequired(String value) {
        List<By> locators = List.of(
                By.xpath(String.format("//*[@name='%s' or @label='%s' or @value='%s']", value, value, value)),
                By.xpath(String.format("//*[contains(@name, '%s') or contains(@label, '%s') or contains(@value, '%s') or contains(@text, '%s')]", value, value, value, value))
        );

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element != null && element.isDisplayed()) {
                    element.click();
                    System.out.println("Selected: " + value);
                    return;
                }
            }
        }
        System.out.println("Skipped optional value: " + value + " (not visible on this screen).");
    }

    private void waitForAnyVisibleText(String... texts) {
        wait.until(webDriver -> {
            String pageSource = webDriver.getPageSource();
            for (String text : texts) {
                if (pageSource.contains(text)) {
                    System.out.println("Detected page text: " + text);
                    return true;
                }
            }
            return false;
        });
    }

    private void selectQuoteValue(String fieldLabel, String optionText) {
        clickText(fieldLabel);

        WebElement searchInput = wait.until(webDriver -> findVisibleElement(
            webDriver,
            List.of(
                AppiumBy.accessibilityId("Search " + fieldLabel),
                By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search')]"),
                By.xpath("//XCUIElementTypeTextField | //android.widget.EditText")
            )
        ));

        if (searchInput == null) {
            throw new AssertionError("Search field for '" + fieldLabel + "' was not found.");
        }

        try {
            searchInput.clear();
        } catch (Exception ignored) {
            // Some iOS inputs do not support clear() directly; continue with sendKeys.
        }
        searchInput.sendKeys(optionText);

        List<By> optionLocators = List.of(
            By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "')]"),
            By.xpath("//*[contains(translate(normalize-space(@name), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(normalize-space(@label), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(normalize-space(@value), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(normalize-space(@text), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "')]" )
        );

        WebElement option = wait.until(webDriver -> {
            for (By locator : optionLocators) {
                List<WebElement> matches = webDriver.findElements(locator);
                for (WebElement candidate : matches) {
                    try {
                        if (candidate != null && candidate.isDisplayed() && candidate.isEnabled()) {
                            return candidate;
                        }
                    } catch (Exception ignored) {
                        // continue
                    }
                }
            }
            List<WebElement> candidates = webDriver.findElements(By.xpath("//XCUIElementTypeCell | //XCUIElementTypeButton | //XCUIElementTypeStaticText | //android.widget.TextView"));
            for (WebElement candidate : candidates) {
                try {
                    String text = candidate.getText();
                    if (text != null && text.toLowerCase(Locale.ROOT).contains(optionText.toLowerCase(Locale.ROOT))) {
                        return candidate;
                    }
                } catch (Exception ignored) {
                    // Ignore non-text elements.
                }
            }
            return null;
        });

        option.click();
        System.out.println("Selected quote option: " + fieldLabel + " -> " + optionText);
    }

    private void selectMakeAndModel(String optionText) {
        tapSelectButton();

        WebElement searchInput = wait.until(webDriver -> findVisibleElement(
                webDriver,
                List.of(
                        AppiumBy.accessibilityId("Search Your Make & Model"),
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search')]"),
                        By.xpath("//XCUIElementTypeTextField | //android.widget.EditText")
                )
        ));

        searchInput.clear();
        searchInput.sendKeys(optionText);
        waitForPickerResultsToSettle();
        clickPickerOption(optionText);
        System.out.println("Selected make and model: " + optionText);
    }

    private void waitForPickerResultsToSettle() {
        wait.until(webDriver -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return false;
            }
            return true;
        });
    }

    private void waitForQuoteFormToSettle() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the quote form to settle.", interruptedException);
        }
    }

    private void tapSelectButton() {
        WebElement selectButton = wait.until(webDriver -> findVisibleElement(
                webDriver,
                List.of(
                        AppiumBy.accessibilityId("Select"),
                        By.xpath("//*[@name='Select' or @label='Select' or @value='Select' or @text='Select']"),
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'select') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'select') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'select') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'select')]")
                )
        ));

        selectButton.click();
        System.out.println("Clicked make and model Select button.");
    }

    private void clickPickerOption(String optionText) {
        WebElement option = wait.until(webDriver -> {
            List<By> optionLocators = List.of(
                    By.xpath("//*[@name='" + optionText + "' or @label='" + optionText + "' or @value='" + optionText + "']"),
                    By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "')]")
            );

            for (By locator : optionLocators) {
                for (WebElement candidate : webDriver.findElements(locator)) {
                    try {
                        if (candidate.isDisplayed() && candidate.isEnabled()) {
                            return candidate;
                        }
                    } catch (Exception ignored) {
                        // Continue searching the picker options.
                    }
                }
            }
            return null;
        });

        option.click();
    }

    private void selectNextVisibleQuoteField(String optionText) {
        System.out.println("Attempting next quote field with option: " + optionText);
        WebElement field = wait.until(webDriver -> {
            List<WebElement> candidates = webDriver.findElements(By.xpath("//XCUIElementTypeTextField | //XCUIElementTypeButton | //XCUIElementTypeCell | //android.widget.EditText"));
            for (WebElement candidate : candidates) {
                try {
                    if (candidate != null && candidate.isDisplayed() && candidate.isEnabled()) {
                        String value = candidate.getAttribute("value");
                        String name = candidate.getAttribute("name");
                        String label = candidate.getAttribute("label");
                        String text = candidate.getText();
                        if (value == null || value.isBlank()) {
                            return candidate;
                        }
                        if (name != null && (name.toLowerCase(Locale.ROOT).contains("select") || name.toLowerCase(Locale.ROOT).contains("fuel") || name.toLowerCase(Locale.ROOT).contains("variant") || name.toLowerCase(Locale.ROOT).contains("city") || name.toLowerCase(Locale.ROOT).contains("owner"))) {
                            return candidate;
                        }
                        if ((label != null && (label.toLowerCase(Locale.ROOT).contains("fuel") || label.toLowerCase(Locale.ROOT).contains("variant") || label.toLowerCase(Locale.ROOT).contains("city") || label.toLowerCase(Locale.ROOT).contains("owner") || label.toLowerCase(Locale.ROOT).contains("make") || label.toLowerCase(Locale.ROOT).contains("model")))
                                || (text != null && !text.isBlank())) {
                            return candidate;
                        }
                    }
                } catch (Exception ignored) {
                    // continue
                }
            }
            String source = webDriver.getPageSource();
            if (source != null && source.length() > 0) {
                System.out.println("Current page source before next field selection:\n" + source.substring(0, Math.min(2500, source.length())));
            }
            return null;
        });

        field.click();

        WebElement searchInput = wait.until(webDriver -> findVisibleElement(
                webDriver,
                List.of(
                        By.xpath("//XCUIElementTypeTextField | //android.widget.EditText"),
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search')]"))
        ));

        searchInput.clear();
        searchInput.sendKeys(optionText);

        WebElement option = wait.until(webDriver -> {
            List<By> optionLocators = List.of(
                    By.xpath("//*[@name='" + optionText + "' or @label='" + optionText + "' or @value='" + optionText + "']"),
                    By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + optionText.toLowerCase(Locale.ROOT) + "')]"),
                    By.xpath("//XCUIElementTypeCell | //XCUIElementTypeStaticText | //android.widget.TextView")
            );

            for (By locator : optionLocators) {
                List<WebElement> matches = webDriver.findElements(locator);
                for (WebElement candidate : matches) {
                    try {
                        String text = candidate.getText();
                        String name = candidate.getAttribute("name");
                        String label = candidate.getAttribute("label");
                        String value = candidate.getAttribute("value");
                        if ((text != null && text.toLowerCase(Locale.ROOT).contains(optionText.toLowerCase(Locale.ROOT))) ||
                                (name != null && name.toLowerCase(Locale.ROOT).contains(optionText.toLowerCase(Locale.ROOT))) ||
                                (label != null && label.toLowerCase(Locale.ROOT).contains(optionText.toLowerCase(Locale.ROOT))) ||
                                (value != null && value.toLowerCase(Locale.ROOT).contains(optionText.toLowerCase(Locale.ROOT)))) {
                            return candidate;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            return null;
        });

        option.click();
        System.out.println("Selected next visible quote field option: " + optionText);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT)
                .replace("&", "and")
                .replace("-", " ")
                .replace("_", " ")
                .replace("/", " ")
                .replace(".", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private void clickText(String text) {
        WebElement element;
        try {
            element = wait.until(webDriver -> {
                List<By> locators = List.of(
                        AppiumBy.accessibilityId(text),
                        By.xpath("//*[@name='" + text + "' or @label='" + text + "' or @value='" + text + "']"),
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "')]" )
                );

                for (By locator : locators) {
                    List<WebElement> matches = webDriver.findElements(locator);
                    for (WebElement candidate : matches) {
                        try {
                            if (candidate != null && candidate.isDisplayed() && candidate.isEnabled()) {
                                return candidate;
                            }
                        } catch (Exception ignored) {
                            // Continue trying other candidates
                        }
                    }
                }

                String normalizedText = normalize(text);
                for (By locator : List.of(
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "')]"),
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize("Select " + text) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize("Select " + text) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize("Select " + text) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize("Select " + text) + "')]"),
                        By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize(text.replace("Select ", "")) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize(text.replace("Select ", "")) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize(text.replace("Select ", "")) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalize(text.replace("Select ", "")) + "')]"))) {
                    List<WebElement> matches = webDriver.findElements(locator);
                    for (WebElement candidate : matches) {
                        try {
                            if (candidate != null && candidate.isDisplayed() && candidate.isEnabled()) {
                                return candidate;
                            }
                        } catch (Exception ignored) {
                            // continue
                        }
                    }
                }

                String pageSource = webDriver.getPageSource();
                String lowerPageSource = pageSource.toLowerCase(Locale.ROOT);
                if (lowerPageSource.contains(normalizedText) || lowerPageSource.contains(normalize("Select " + text)) || lowerPageSource.contains(normalize(text.replace("Select ", "")))) {
                    for (By locator : List.of(
                            By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "')]" )
                    )) {
                        List<WebElement> matches = webDriver.findElements(locator);
                        if (!matches.isEmpty()) {
                            return matches.get(0);
                        }
                    }
                }

                return null;
            });
        } catch (Exception e) {
            System.out.println("CLICK_TEXT_TIMEOUT for: " + text);
            String pageSource = driver.getPageSource();
            if (pageSource != null) {
                String snippet = pageSource.length() > 3000 ? pageSource.substring(0, 3000) : pageSource;
                System.out.println(snippet);
            }
            throw e;
        }

        element.click();
        System.out.println("Clicked: " + text);
    }

    private void verifyTextDisplayed(String text) {
        WebElement element = wait.until(webDriver -> findVisibleElement(
                webDriver,
                List.of(
                        By.xpath("//*[@name='" + text + "' or @label='" + text + "' or @value='" + text + "']"),
                By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "')]" )
                )
        ));
        Assert.assertTrue(element.isDisplayed(), text + " is not displayed.");
        System.out.println("Verified: " + text);
    }

    private WebElement findVisibleElement(WebDriver webDriver, List<By> locators) {
        for (By locator : locators) {
            List<WebElement> elements = webDriver.findElements(locator);
            for (WebElement element : elements) {
                if (element != null && element.isDisplayed()) {
                    return element;
                }
            }
        }
        return null;
    }

    private void selectPricePack(int index) {
        List<WebElement> priceElements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//*[@name='₹' or @label='₹' or contains(@text,'₹')]")))
        ;

        if (priceElements.size() <= index) {
            throw new AssertionError("Unable to select price index " + index + ". Only " + priceElements.size() + " price elements found.");
        }

        WebElement selectedPrice = priceElements.get(index);
        System.out.println("Selected price: " + selectedPrice.getText());
        selectedPrice.click();
    }

    private void expandBasePlan() {
        List<WebElement> basePlan = driver.findElements(
                By.xpath("//*[contains(@text,\"What's included in your base plan?\") or contains(@name,\"What's included in your base plan?\") or contains(@label,\"What's included in your base plan?\")]"));

        if (!basePlan.isEmpty()) {
            WebElement element = basePlan.get(0);
            if (element.isDisplayed()) {
                element.click();
                System.out.println("Opened base plan details.");
                return;
            }
        }

        List<WebElement> plusButtons = driver.findElements(By.xpath("//*[@name='+' or @label='+' or @text='+']"));
        if (!plusButtons.isEmpty()) {
            plusButtons.get(0).click();
            System.out.println("Clicked + to open base plan.");
        }
    }

    private void fillBikeOwnerDetails() {
        List<WebElement> fields = driver.findElements(By.xpath("//XCUIElementTypeTextField | //android.widget.EditText"));

        if (fields.size() < 4) {
            throw new AssertionError("Expected at least 4 text fields for bike owner details but found " + fields.size());
        }

        fields.get(0).click();
        fields.get(0).clear();
        fields.get(0).sendKeys(FIRST_NAME);

        fields.get(1).click();
        fields.get(1).clear();
        fields.get(1).sendKeys(MIDDLE_NAME);

        fields.get(2).click();
        fields.get(2).clear();
        fields.get(2).sendKeys(LAST_NAME);

        fields.get(3).click();
        fields.get(3).clear();
        fields.get(3).sendKeys(DOB);

        System.out.println("Bike owner details entered.");
    }

    private void enterMobileNumber() {
        List<WebElement> fields = driver.findElements(By.xpath("//XCUIElementTypeTextField | //android.widget.EditText"));
        for (WebElement field : fields) {
            if (!field.isDisplayed()) {
                continue;
            }
            try {
                String hint = field.getAttribute("hint");
                String name = field.getAttribute("name");
                String label = field.getAttribute("label");
                if ((hint != null && hint.toLowerCase().contains("phone")) ||
                        (name != null && name.toLowerCase().contains("mobile")) ||
                        (label != null && label.toLowerCase().contains("mobile"))) {
                    field.click();
                    field.clear();
                    field.sendKeys(MOBILE_NUMBER);
                    System.out.println("Mobile number entered.");
                    return;
                }
            } catch (Exception ignored) {
            }
        }

        throw new AssertionError("Mobile number field not found.");
    }

    private void enterOTP() {
        List<WebElement> fields = driver.findElements(By.xpath("//XCUIElementTypeTextField | //android.widget.EditText"));
        for (WebElement field : fields) {
            if (!field.isDisplayed()) {
                continue;
            }
            try {
                String hint = field.getAttribute("hint");
                String name = field.getAttribute("name");
                String label = field.getAttribute("label");
                if ((hint != null && hint.toLowerCase().contains("otp")) ||
                        (name != null && name.toLowerCase().contains("otp")) ||
                        (label != null && label.toLowerCase().contains("otp"))) {
                    field.click();
                    field.clear();
                    field.sendKeys(OTP);
                    System.out.println("OTP entered: " + OTP);
                    return;
                }
            } catch (Exception ignored) {
            }
        }

        throw new AssertionError("OTP field not found.");
    }

    private void enterEmail() {
        List<WebElement> fields = driver.findElements(By.xpath("//XCUIElementTypeTextField | //android.widget.EditText"));
        for (WebElement field : fields) {
            if (!field.isDisplayed()) {
                continue;
            }
            try {
                field.click();
                field.clear();
                field.sendKeys(EMAIL);
                System.out.println("Email entered: " + EMAIL);
                return;
            } catch (Exception ignored) {
            }
        }

        throw new AssertionError("Email field not found.");
    }

    private void selectConsent() {
        List<WebElement> consentElements = driver.findElements(
                By.xpath("//*[contains(@text,'consent') or contains(@name,'consent') or contains(@label,'consent')]")
        );
        if (consentElements.isEmpty()) {
            throw new AssertionError("Consent checkbox not found.");
        }

        WebElement consentElement = consentElements.get(0);
        if (!consentElement.isSelected()) {
            consentElement.click();
        }
        System.out.println("Consent checkbox verified.");
    }

    private void verifyProposalForm() {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(@text,'Proposal Form') or contains(@name,'Proposal Form') or contains(@label,'Proposal Form')]")
                )
        );
        Assert.assertTrue(form.isDisplayed(), "Proposal Form was not displayed.");
        System.out.println("Proposal Form displayed successfully.");
    }
}