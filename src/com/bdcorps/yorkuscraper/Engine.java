package com.bdcorps.yorkuscraper;

import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class Engine {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://w2prod.sis.yorku.ca/Apps/WebObjects/cdm";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testYorku() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("Subject")).click();
		int session = 0;

		Select a = new Select(driver.findElement(By.name("subjectPopUp")));
		List<WebElement> l = a.getOptions();

		for (int j = 0; j < l.size(); j++) {
			new Select(driver.findElement(By.name("subjectPopUp"))).selectByIndex(j);
			driver.findElement(By.name("1.10.7.5")).click();
			for (int i = 1; isElementPresent(
					By.xpath("(//a[contains(text(),'Summer 2016 Course Schedule')])[" + i + "]")); i++) {
				driver.findElement(By.xpath("(//a[contains(text(),'Summer 2016 Course Schedule')])[" + i + "]"))
						.click();

				Thread.sleep(250);
				try {
					FileWriter fstream = new FileWriter(j + "_" + i + ".html");
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(driver.getPageSource().toString());
					// Close the output stream
					out.close();
				} catch (Exception e) {// Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}

				driver.navigate().back();
			}
			System.out.println(j);
			driver.get(baseUrl);
			driver.findElement(By.linkText("Subject")).click();
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
