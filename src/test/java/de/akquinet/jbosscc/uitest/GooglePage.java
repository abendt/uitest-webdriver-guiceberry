package de.akquinet.jbosscc.uitest;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;

import javax.inject.Inject;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * see http://code.google.com/p/selenium/wiki/PageObjects and
 * http://code.google.com/p/selenium/wiki/LoadableComponent for details
 * about the PageObject Pattern.
 *
 * @author Alphonse Bendt, akquinet tech@spree GmbH
 */
public class GooglePage extends LoadableComponent<GooglePage> {

    @Inject
    private WebDriver driver;

    @FindBy(name = "q")
    private WebElement searchField;

    @FindBy(css = ".r a")
    private List<WebElement> searchResults;

    @Override
    protected void load() {
        driver.get("http://www.google.com");
    }

    @Override
    protected void isLoaded() throws Error {
        assertThat(driver.getTitle()).contains("Google");
    }

    public void search(String query) {
        searchField.sendKeys(query);
        searchField.submit();
    }

    public List<String> getSearchResults() {
        Function<WebElement, String> transformToText = new Function<WebElement, String>() {
            @Override
            public String apply(WebElement from) {
                return from.getText();
            }
        };

        return Lists.transform(
                searchResults,
                transformToText
        );
    }
}