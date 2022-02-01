package com.saucedemo.tests;

import com.saucedemo.AbstractTestBase;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucelabs.saucebindings.Browser;
import com.saucelabs.saucebindings.SaucePlatform;
import com.saucelabs.saucebindings.SauceSession;
import com.saucelabs.saucebindings.junit4.SauceBaseTest;
import com.saucelabs.saucebindings.options.SauceOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.TimeoutException;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Desktop Tests. */
@RunWith(Parameterized.class)
public class DesktopTests extends AbstractTestBase {
    /*
     * Configure our data driven parameters
     * */
    @Parameterized.Parameter
    public Browser browserName;
    @Parameterized.Parameter(1)
    public String browserVersion;
    @Parameterized.Parameter(2)
    public SaucePlatform platform;

    @Parameterized.Parameters(name="{2} – {0} {1}")
    public static Collection<Object[]> crossBrowserData() {
        return Arrays.asList(new Object[][] {
                { Browser.CHROME, "latest", SaucePlatform.WINDOWS_10 },
                { Browser.CHROME, "latest-1", SaucePlatform.WINDOWS_10 },
                { Browser.SAFARI, "latest", SaucePlatform.MAC_MOJAVE },
                { Browser.CHROME, "latest", SaucePlatform.MAC_MOJAVE }
        });
    }

    @Before
    public void setup() {
        SauceOptions sauceOptions = new SauceOptions();
        sauceOptions.setBrowserName(browserName);
        sauceOptions.setBrowserVersion(browserVersion);
        sauceOptions.setPlatformName(platform);
        sauceOptions.sauce().setBuild(buildName);
        sauceOptions.sauce().setName(testName.getMethodName());

        driver = new SauceSession(sauceOptions).start();
    }

    @Test()
    public void loginWorks() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.visit();
        loginPage.login("standard_user");
        assertTrue(new ProductsPage(driver).isDisplayed());
    }

    @Test(expected = TimeoutException.class)
    public void lockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.visit();
        loginPage.login("locked_out_user");
        assertFalse(new ProductsPage(driver).isDisplayed());
    }

    @Test(expected = TimeoutException.class)
    public void invalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.visit();
        loginPage.login("foo_bar_user");
        assertFalse(new ProductsPage(driver).isDisplayed());
    }
}
