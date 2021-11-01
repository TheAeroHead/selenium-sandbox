package selenium.sandbox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class App 
{
    public static void main( String[] args )
    {
    	// Initialize driver and time to wait before throwing error
    	WebDriver driver = initializeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		// Maximize Window
		driver.manage().window().maximize();
		
		// Navigate to web application
		driver.get("https://google.com");
        
		// Identify search bar
        wait.until(ExpectedConditions.visibilityOfElementLocated(
        		By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input")));
		WebElement searchBar = driver.findElement(
				By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input"));
		
		// Input search
		searchBar.sendKeys("brainjack"+Keys.ENTER);
	
    }
    
    // Tests
 	@Test(priority = 0, groups = { "google" })
 	public void testGoogleSearch() {
 		// Initialize driver and time to wait before throwing error
    	WebDriver driver = initializeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		// Maximize Window
		driver.manage().window().maximize();
		
		// Navigate to web application
		driver.get("https://google.com");
		        
		// Identify search bar
		wait.until(ExpectedConditions.visibilityOfElementLocated(
		    	By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input")));
		WebElement searchBar = driver.findElement(
				By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input"));
				
		// Input search
		searchBar.sendKeys("brainjack"+Keys.ENTER);
		
		// Custom expected condition to check whether we have navigated away from google homepage.
 		final String previousURL = "https://google.com";
 		ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
 			public Boolean apply(WebDriver d) {
 				return (d.getCurrentUrl() != previousURL);
 			}
 		};
 		wait.until(e);
 		System.out.println("Current URL: " + driver.getCurrentUrl());
 		
 		// Check title of first listing.
 		String expectedTitle = "Brainjack by Brian Falkner - Goodreads";
 		
 		// h3 is a header, a is the actual link.
 		WebElement firstResultTitle = driver.findElement(By.xpath("/html/body/div[7]/div/div[9]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div/div[1]/a/h3"));
 		System.out.println("Result Title: " + firstResultTitle.getText());
 		Assert.assertEquals(firstResultTitle.getText(), expectedTitle);
 	}
    
    public static WebDriver initializeDriver() {
		WebDriverManager.chromedriver().setup();
		
		// Initialize Chrome properties
		ChromeOptions chromeOptions = new ChromeOptions();
		
		// Add a custom download directory to chrome options
		String downloadDirectory = System.getProperty("user.dir") + "\\data\\"; // "C:\\WebDriver\\data";
		HashMap<String, Object> chromePref = new HashMap<String, Object>();
		chromePref.put("profile.default_content_settings.popups", 0);
		chromePref.put("download.default_directory", downloadDirectory);
		chromePref.put("safebrowsing.enabled", true);
		chromePref.put("download.prompt_for_download", false);
		chromePref.put("download.extensions_to_open", "xlsx");
		
		chromeOptions.setExperimentalOption("prefs", chromePref);
		
		// Return chromeDriver
		return new ChromeDriver(chromeOptions);
	}
    
    /**
	 * Helper method to switch tabs to whatever was the original window the test was operating from.
	 * This is helpful for our current test design which is very process based, navigating and manipulating
	 * objects on the DOM in a ordered and specified manner. Even though most tests can be accomplished within
	 * a single window, some actions may open a dialog box stored in an iframe or even a new tab in the browser. 
	 * Such actions require manual specification to switch to said tab and return to the original tab after accomplishing
	 * the desired action.
	 * 
	 * @param driver
	 * @return driver (pointing to the correct tab/window)
	 */
	public static WebDriver returnToInitialTab(WebDriver driver) {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));

		return driver;
	}
	
	/**
	 * 
	 * This function will take screenshot and store it at the location specified in the second parameter.
	 * 
	 * @param webdriver
	 * 
	 * @param fileWithPath
	 * 
	 * @throws Exception
	 * 
	 */
	public static void takeSnapShot(WebDriver webDriver, String fileWithPath) throws Exception {
		// Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
		
		// Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

		// Move image file to new destination
		File DestFile = new File(fileWithPath);

		// Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);
	}
}
