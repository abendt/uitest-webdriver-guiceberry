package de.akquinet.jbosscc.uitest;

import com.google.common.testing.TearDown;
import com.google.common.testing.TearDownAccepter;
import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.TestScoped;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.concurrent.TimeUnit;

/**
 * @author Alphonse Bendt
 */
public class UiTestConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        install(new GuiceBerryModule());

        enablePageFactoryInitializationForLoadableComponents();
    }

    @Provides
    @TestScoped
    public WebDriver getWebDriver(TearDownAccepter tda) {
        final WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        tda.addTearDown(new TearDown() {
            @Override
            public void tearDown() throws Exception {
                driver.close();
            }
        });

        return driver;
    }

    private void enablePageFactoryInitializationForLoadableComponents() {
        bindListener(subclassesOf(LoadableComponent.class), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, final TypeEncounter<I> encounter) {

                final Provider<WebDriver> driver = encounter.getProvider(WebDriver.class);

                encounter.register(new InjectionListener() {
                    @Override
                    public void afterInjection(Object injectee) {
                        PageFactory.initElements(driver.get(), injectee);
                    }
                });
            }
        });
    }

    // taken from

    /**
     * A factory of {@link Matcher}s that can be used in
     * {@link AbstractModule}.bindListener(matcher,listener)
     * @author ooktay
     */
    private static class SubClassesOf extends AbstractMatcher<TypeLiteral<?>> {
        private final Class<?> baseClass;

        private SubClassesOf(Class<?> baseClass) {
            this.baseClass = baseClass;
        }

        @Override
        public boolean matches(TypeLiteral<?> t) {
            return baseClass.isAssignableFrom(t.getRawType());
        }
    }

    /**
     * Matcher matches all classes that extends, implements or is the same as baseClass
     */
    private static Matcher<TypeLiteral<?>> subclassesOf(Class<?> baseClass) {
        return new SubClassesOf(baseClass);
    }
}
