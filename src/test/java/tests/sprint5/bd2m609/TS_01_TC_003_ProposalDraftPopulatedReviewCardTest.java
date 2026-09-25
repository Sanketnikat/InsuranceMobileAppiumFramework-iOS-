package tests.sprint5.bd2m609;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

public class TS_01_TC_003_ProposalDraftPopulatedReviewCardTest extends BaseTest {

    private WebDriverWait wait;

    private static final String MOBILE_NUMBER = "8921639271";
    private static final String OTP = "123456";
    private static final String EMAIL = "sanket.nikat@gmail.com";

    private static final String FIRST_NAME = "Sanket";
    private static final String MIDDLE_NAME = "Sudam";
    private static final String LAST_NAME = "Nikat";
    private static final String DOB = "05/05/2000";

    private static final int PRICE_BUTTON_INDEX = 1;

    @Test(
            description = "TS_01_TC_003 - Verify that the loading skeleton is replaced by the populated review card once the draft response is received",
            groups = {"sprint5", "BD2M-609", "proposal", "positive"}
    )
    public void proposalDraftPopulatedReviewCardTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Welcome Screen
        clickText("Login");

        // Login Screen
        clickText("Private Bike Insurance");

        // Bike Insurance
        clickText("Brand new bike?");

        waitForAnyVisibleText("View Prices", "Get your bike insurance quote", "Get your bike insuarance quote");

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

        // Contact Details
        enterMobileNumber();
        clickText("Get OTP");
        enterOTP();
        enterEmail();
        selectConsent();

        // Step 1: Navigate to the Summary & Consent screen
        clickText("Save & Next");

        // Step 2: Wait for the draft API response
        waitForDraftToFinishLoading();

        // Skeleton should be removed and all sections should render with the values returned by the API
        Assert.assertFalse(
                isLoadingSkeletonDisplayed(),
                "Loading skeleton is still displayed after the draft API response was received."
        );
        verifyProposalForm();
        verifyReviewCardPopulated();

        System.out.println();
        System.out.println("=================================================");
        System.out.println("TS_01_TC_003 EXECUTION COMPLETED SUCCESSFULLY");
        System.out.println("Review card rendered with the fetched draft values.");
        System.out.println("=================================================");
    }

    private void waitForDraftToFinishLoading() {
        Instant deadline = Instant.now().plusSeconds(30);
        while (Instant.now().isBefore(deadline)) {
            if (isProposalFormDisplayed() && !isLoadingSkeletonDisplayed()) {
                return;
            }
            sleep(500);
        }
        throw new AssertionError("Proposal draft did not finish loading within the expected time.");
    }

    private void verifyReviewCardPopulated() {
        String pageSource = driver.getPageSource();
        Assert.assertTrue(
                pageSource.contains(FIRST_NAME) && pageSource.contains(LAST_NAME),
                "Review card does not display the owner name returned by the draft API."
        );
        Assert.assertTrue(
                pageSource.contains(MOBILE_NUMBER),
                "Review card does not display the mobile number returned by the draft API."
        );
        Assert.assertTrue(
                pageSource.contains(EMAIL),
                "Review card does not display the email returned by the draft API."
        );
    }

    private boolean isLoadingSkeletonDisplayed() {
        List<WebElement> activityIndicators = driver.findElements(IOSLocators.LOADING_INDICATOR);
        for (WebElement indicator : activityIndicators) {
            if (indicator.isDisplayed()) {
                return true;
            }
        }

        String pageSource = driver.getPageSource();
        if (pageSource == null) {
            return false;
        }
        String lowerSource = pageSource.toLowerCase(Locale.ROOT);
        return lowerSource.contains("skeleton") || lowerSource.contains("shimmer") || lowerSource.contains("loading");
    }

    private boolean isProposalFormDisplayed() {
        List<WebElement> matches = driver.findElements(
                By.xpath("//*[contains(@text,'Proposal Form') or contains(@name,'Proposal Form') or contains(@label,'Proposal Form')]")
        );
        for (WebElement match : matches) {
            if (match.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    // ---------------------------------------------------------------
    // Shared navigation helpers (mirrors TS_01_TC_001 flow)
    // ---------------------------------------------------------------

    private void waitForAnyVisibleText(String... texts) {
        wait.until(webDriver -> {
            String pageSource = webDriver.getPageSource();
            for (String text : texts) {
                if (pageSource.contains(text)) {
                    return true;
                }
            }
            return false;
        });
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
    }

    private void waitForPickerResultsToSettle() {
        wait.until(webDriver -> {
            sleep(2000);
            return true;
        });
    }

    private void waitForQuoteFormToSettle() {
        sleep(5000);
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
        WebElement element = wait.until(webDriver -> {
            List<By> locators = List.of(
                    AppiumBy.accessibilityId(text),
                    By.xpath("//*[@name='" + text + "' or @label='" + text + "' or @value='" + text + "']"),
                    By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + text.toLowerCase(Locale.ROOT) + "')]")
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
                    By.xpath("//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "') or contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "') or contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedText + "')]")
            )) {
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

            return null;
        });

        element.click();
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
                        By.xpath("//*[@name='₹' or @label='₹' or contains(@text,'₹')]")));

        if (priceElements.size() <= index) {
            throw new AssertionError("Unable to select price index " + index + ". Only " + priceElements.size() + " price elements found.");
        }

        priceElements.get(index).click();
    }

    private void expandBasePlan() {
        List<WebElement> basePlan = driver.findElements(
                By.xpath("//*[contains(@text,\"What's included in your base plan?\") or contains(@name,\"What's included in your base plan?\") or contains(@label,\"What's included in your base plan?\")]"));

        if (!basePlan.isEmpty()) {
            WebElement element = basePlan.get(0);
            if (element.isDisplayed()) {
                element.click();
                return;
            }
        }

        List<WebElement> plusButtons = driver.findElements(By.xpath("//*[@name='+' or @label='+' or @text='+']"));
        if (!plusButtons.isEmpty()) {
            plusButtons.get(0).click();
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
    }

    private void verifyProposalForm() {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(@text,'Proposal Form') or contains(@name,'Proposal Form') or contains(@label,'Proposal Form')]")
                )
        );
        Assert.assertTrue(form.isDisplayed(), "Proposal Form was not displayed.");
    }
}
