package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseE2ETest extends BaseTest {
    public final String BASE_URL = "https://dou.ua";
    public SoftAssertions soft = new SoftAssertions();

    @BeforeSuite
    public void prepareSuite() {
    }

    @BeforeTest
    public void prepareTests() {
    }

    @Step
    private void prepareBrowser() {
        var isHeadless = System.getProperty("headless");
        Configuration.reportsFolder = System.getProperty("project.parent.basedir");
        Configuration.headless = isHeadless != null ? Boolean.parseBoolean(isHeadless) : Boolean.parseBoolean(System.getProperty("selenide.headless"));
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 8000;
        Configuration.pageLoadTimeout = 15000;
        Configuration.screenshots = true;

        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.cookie_controls_mode", 0);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;

        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .includeSelenideSteps(false)
                        .savePageSource(true));
    }


    @AfterClass(alwaysRun = true, description = "After test actions")
    public void after(){
        screenshot();
        getBrowserLogs();
        closeWebDriver();
    }
    public void screenshot() {
        if(WebDriverRunner.hasWebDriverStarted()) {
            Allure.attachment("Screenshot:", new ByteArrayInputStream(Objects.requireNonNull(Selenide.screenshot(OutputType.BYTES))));
        }
    }

    public void getBrowserLogs(){
        List<String> logList= Selenide.getWebDriverLogs(LogType.BROWSER);
        Allure.attachment("Browser Console Logs:", logList.toString());
    }

}
