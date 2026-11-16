package com.hireflow.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {

    private WebDriver driver;

    // Enter your Admin login details here
    private String adminEmail = "admin@hireflow.com";
    private String adminPassword = System.getenv("ADMIN_PASSWORD");

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
    }

    @Test
    public void verifyAdminLogin() {

        driver.get("http://localhost:8080/login");

        driver.findElement(By.id("email")).sendKeys(adminEmail);
        driver.findElement(By.id("password")).sendKeys(adminPassword);

        driver.findElement(By.className("login-button")).click();

        // Wait briefly for login processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Display actual result
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("Page Content: " +
                driver.findElement(By.tagName("body")).getText());

        System.out.println("Admin login diagnostic completed!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}