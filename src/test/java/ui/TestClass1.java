package ui;

import base.BaseE2ETest;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class TestClass1 extends BaseE2ETest {

    @Test
    public void verifyBaseUrl() {
        open(BASE_URL);

        assertThat(Selenide.webdriver().driver().url()).isEqualTo("https://dou.ua/");
    }
}