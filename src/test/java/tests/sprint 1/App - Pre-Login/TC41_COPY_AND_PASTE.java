package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class TC41_COPY_AND_PASTE extends BaseTest {

    @Test(
            description = "TC41 - Inspect Mobile Number field for Copy/Paste automation",
            groups = {"sprint1", "login"}
    )
    public void TC41_copyAndPasteMobileNumber() {

        step("Tap Login");
        driver.findElement(IOSLocators.LOGIN).click();

        step("Tap Mobile Number field");
        WebElement mobileNumber =
                driver.findElement(IOSLocators.MOBILE_NUMBER_INPUT);

        mobileNumber.click();

        step("Enter mobile number");
        mobileNumber.sendKeys("8921639271");

        System.out.println("======================================");
        System.out.println("MOBILE NUMBER FIELD PAGE SOURCE");
        System.out.println("======================================");

        System.out.println(driver.getPageSource());

        System.out.println("======================================");
        System.out.println("END PAGE SOURCE");
        System.out.println("======================================");
    }
}