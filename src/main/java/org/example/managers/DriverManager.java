package org.example.managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {
    private WebDriver driver;
    private static DriverManager INSTANCE = null;
    private final PropManager propManager = PropManager.getPropManager();

    private DriverManager() {
    }

    public static DriverManager getDriverManager() {
        if (INSTANCE == null) {
            INSTANCE = new DriverManager();
        }
        return INSTANCE;
    }
    public WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    private void initDriver() {
        if ("remote".equalsIgnoreCase(propManager.getProperty("type.driver"))){
            initRemoteDriver();
        }else{
            initLocalDriver();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
    }
    private void initRemoteDriver(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", propManager.getProperty("type.browser"));
        capabilities.setCapability("browserVersion", "109.0");
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", false
        ));
        try {
            driver = new RemoteWebDriver(
                    URI.create(propManager.getProperty("selenoid.url")).toURL(),
                    capabilities
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    private void initLocalDriver(){
        System.setProperty("webdriver.chromedriver.driver","src/test/resources/chrome.exe");
        driver = new ChromeDriver();
    }
}
