 package com.udacity.jwdnd.course1.cloudstorage;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
	//	WebDriverManager.chromedriver().setup();
	SafariOptions options = new SafariOptions();
		options.setAutomaticInspection(false);
		options.setAutomaticProfiling(false);
	}

	@BeforeEach
	public void beforeEach() {
		//this.driver = new ChromeDriver();
			SafariOptions options = new SafariOptions();
		options.setAutomaticInspection(false); // Optional: Modify options as needed
		options.setAutomaticProfiling(false);
		//this.driver = new ChromeDriver();
		driver = new SafariDriver(options);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		//Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */

@Test
public void testLargeUpload() {
	// Create a test account
	doMockSignUp("Large File","Test","LFT","12345678");
	doLogIn("LFT", "12345678");

	// Try to upload an arbitrary large file
	WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
	String fileName = "upload5m.zip";

	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
	WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
	fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

	WebElement uploadButton = driver.findElement(By.id("uploadButton"));
	uploadButton.click();
	try {
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
	} catch (org.openqa.selenium.TimeoutException e) {
		System.out.println("Large File upload failed");
	}
	Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
}

	

public void addNote(){
	WebDriverWait wait = new WebDriverWait(driver, 10);
	driver.findElement(By.id("nav-notes-tab")).click();
	WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
	Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());
		// Creating note
		var launchDialogButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAddNewNote")));
		launchDialogButton.click();

		var noteTitleInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		noteTitleInput.clear();
		noteTitleInput.sendKeys("test");

		var noteDescriptionInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
		noteDescriptionInput.clear();
		noteDescriptionInput.sendKeys("Description written in note for testing");

		var saveNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSaveChanges")));
		saveNoteButton.click();
		driver.findElement(By.id("nav-notes-tab")).click();
		//WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());
}

	@Test
	public void testCreateNote() {

		
		doMockSignUp("Notes","Notes","Notes","123");
		doLogIn("Notes", "123");
	
  	    addNote();

	}

	@Test
	public void testEditNote() {
	WebDriverWait wait = new WebDriverWait(driver, 10);
    doMockSignUp("Notes","Notes","Notes","123");
		doLogIn("Notes", "123");
	addNote();
    var launchDialogButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEditNote")));
		launchDialogButton.click();

	var noteTitleInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
	noteTitleInput.clear();
		noteTitleInput.sendKeys("testEdit");

	var noteDescriptionInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
		noteDescriptionInput.clear();
		noteDescriptionInput.sendKeys("Description written in note for testing");

	var saveNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSaveChanges")));
		saveNoteButton.click();
}

@Test
public void testDeleteNote() {
    doMockSignUp("Notes","Notes","Notes","123");
		doLogIn("Notes", "123");
	addNote();
    
    WebElement deleteLink = driver.findElement(By.id("ancDeleteNote"));
    deleteLink.click();
}


public void addCredential(){
	WebDriverWait wait = new WebDriverWait(driver, 10);
	driver.findElement(By.id("nav-credentials-tab")).click();
	WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
	Assertions.assertTrue(driver.findElement(By.id("nav-credentials-tab")).isDisplayed());
		// Creating note
		var launchDialogButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAddNewCredential")));
		launchDialogButton.click();

		var credentialURLInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		credentialURLInput.clear();
		credentialURLInput.sendKeys("https://test.com");

		var credentialUsernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
		credentialUsernameInput.clear();
		credentialUsernameInput.sendKeys("default");

		var credentialPasswordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
		credentialPasswordInput.clear();
		credentialPasswordInput.sendKeys("abc123456");

		var saveCredentialButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCredentialSaveChanges")));
		saveCredentialButton.click();
	
		driver.findElement(By.id("nav-credentials-tab")).click();
		//WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		Assertions.assertTrue(driver.findElement(By.id("nav-credentials-tab")).isDisplayed());
}

@Test
public void testCreateCredential() {
	doMockSignUp("Credentials","Credentials","Credentials","123");
	doLogIn("Credentials", "123");
	addCredential();
}

@Test
public void testEditCredential() {
	WebDriverWait wait = new WebDriverWait(driver, 10);
    doMockSignUp("Credentials","Credentials","Credentials","123");
		doLogIn("Credentials", "123");
	addCredential();

    var launchDialogButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEditCredential")));
		launchDialogButton.click();

var credentialURLInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		credentialURLInput.clear();
		credentialURLInput.sendKeys("https://test.com");

		var credentialUsernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
		credentialUsernameInput.clear();
		credentialUsernameInput.sendKeys("edit");

		var credentialPasswordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
		credentialPasswordInput.clear();
		credentialPasswordInput.sendKeys("abc123456");

		var saveCredentialButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCredentialSaveChanges")));
		saveCredentialButton.click();
	
}

@Test
public void testDeleteCredential() {
    doMockSignUp("Credentials","Credentials","Credentials","123");
		doLogIn("Credentials", "123");
	addCredential();
    
    WebElement deleteLink = driver.findElement(By.id("aDeleteCredential"));
    deleteLink.click();
}


	@Test
	public void unauthorizedAccesToHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void logout() {
		driver.get("http://localhost:" + this.port + "/signup");

		// Create a test account
		doMockSignUp("test","test","logout","123");
		doLogIn("logout", "123");;


		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/logout");
		Assertions.assertNotSame("Home", driver.getTitle());
	}
}


