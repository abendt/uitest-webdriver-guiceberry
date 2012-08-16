package de.akquinet.jbosscc.uitest;

import com.google.guiceberry.junit4.GuiceBerryRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Alphonse Bendt
 */
public class SearchGoogleTest {

    @Rule
    public final GuiceBerryRule guiceBerryRule = new GuiceBerryRule(UiTestConfiguration.class);

    @Inject
    private WebDriver driver;

    @Inject
    private GooglePage googlePage;

    @Test
    public void canDirectlyUseWebDriver() {
        driver.get("http://www.google.com");

        assertThat(driver.getTitle()).contains("Google");
    }

    @Test
    public void canUsePageObjectToSearchGoogle() {
        googlePage.get();
        googlePage.search("akquinet.de");

        System.out.println("Results: " + googlePage.getSearchResults());
        assertThat(googlePage.getSearchResults()).contains("akquinet AG: akquinet AG Deutschland");
    }
}
