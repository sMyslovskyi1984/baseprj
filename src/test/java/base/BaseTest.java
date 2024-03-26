package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;


public abstract class BaseTest {

    @BeforeSuite
    public void prepareEnv() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
                .includeSelenideSteps(false)
        );
    }

    @AfterSuite(alwaysRun = true)
    public void addEnvironmentVariablesToReport() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setHistoryId(null));
        try (FileOutputStream output = new FileOutputStream(System.getProperty("allure.results.directory") + "/environment.properties")) {
            Properties properties = new Properties();
//            Optional.ofNullable(getEnv()).ifPresent(p -> properties.setProperty("Environment: ", p));
            Optional.ofNullable(System.getProperty("os.name")).ifPresent(p -> properties.setProperty("OS: ", p));
            Optional.ofNullable(System.getProperty("java.version")).ifPresent(p -> properties.setProperty("Java version: ", p));
            Optional.ofNullable(Configuration.browser).ifPresent(p -> properties.setProperty("Browser: ", p));
            Optional.ofNullable(Configuration.browserVersion).ifPresent(p -> properties.setProperty("Browser version: ", p));
            Optional.ofNullable(System.getProperty("db_address")).ifPresent(p -> properties.setProperty("DB address: ", p));
            properties.store(output, "Test parameters: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}