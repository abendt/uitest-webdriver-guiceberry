package de.akquinet.jbosscc.uitest;

import com.google.guiceberry.junit4.GuiceBerryRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

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
        assertNotNull(driver);
    }

    @Test
    public void elementsAreInjectedIntoPage() {
        googlePage.get();
        googlePage.search("akquinet.de");

        assertThat(googlePage.getSearchResults()).contains("akquinet AG: akquinet AG Deutschland");
        System.out.println("Search Results: " + googlePage.getSearchResults());
    }
}
