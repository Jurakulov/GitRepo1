package stepDefinitions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import pageObjects.AddCustomerPage;
import pageObjects.LoginPage;
import pageObjects.SearchCustomerPage;


public class Steps extends BaseClass {
	
	@Before
	public void setup() throws IOException
	{

		Logger=Logger.getLogger("CucumberJava");  //Added logger
		PropertyConfigurator.configure("log4j.properties");  //Added logger
		//Reading properties
		configProp=new Properties();
		FileInputStream configPropfile=new FileInputStream("config.properties");
		configProp.load(configPropfile);
		
		String br=configProp.getProperty("browser");
		if(br.equals("chrome"))
		{
	    System.setProperty("webdriver.chrome.driver",configProp.getProperty("chromepath"));
	    driver=new ChromeDriver();
	  
		}
		
		else if(br.equals("firefox"))
		{
			
			
			System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
			String Firefoxdriverpath = "C:\\Users\\nasim\\Downloads\\geckodriver-v0.30.0-win64 (2)\\geckodriver.exe";
			System.setProperty("webdriver.gecko.driver", Firefoxdriverpath);
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setCapability("marionette", true);
			driver = new FirefoxDriver(firefoxOptions);	
			
	    //System.setProperty("webdriver.firefox.bin",configProp.getProperty("firefoxpath"));
	   // driver=new FirefoxDriver();
		}
		
		else if(br.equals("edge"))
		{
	    System.setProperty("webdriver.edge.driver",configProp.getProperty("edgepath"));
	    driver=new EdgeDriver();
		}
		
		Logger.info("********Launching browser**********");	
	}
	
	
@Given("User Launch Chrome browser")
public void user_launch_chrome_browser() {
	
    lp=new LoginPage(driver);
}

@When("User opens URL {string}")
public void user_opens_url(String url) {
	Logger.info("********Opening URL**********");
    driver.get(url);
    driver.manage().window().maximize();
}

@When("User enters Email as {string} and Password as {string}")
public void user_enters_email_as_and_password_as(String email, String password) {
	Logger.info("********Providing login details**********");
    lp.setUserName(email);
    lp.setPassword(password);
}
@When("Click on Login")
public void click_on_login() {
	Logger.info("********Started login**********");
	lp.clickLogin();
}

@Then("Page Title should be {string}")
public void page_title_should_be(String title) {
    if(driver.getPageSource().contains("Login was unsuccessful.")) {
    	driver.close();
    	Logger.info("********Login passed**********");
    	Assert.assertTrue(false);
    }else {
    	Logger.info("********Login failed**********");
    	Assert.assertEquals(title, driver.getTitle());
    }
    	
}

@When("User click on Log out link")
public void user_click_on_log_out_link() throws InterruptedException {
   Logger.info("********Click on logout link**********");
   lp.clickLogout();
   Thread.sleep(3000);
}

@Then("close browser")
public void close_browser() {
   Logger.info("********Closing browser**********");
   driver.close();
}

   //Customers feature step definitions....


@Then("User can view Dashboard")
public void user_can_view_dashboard() {
    addCust=new AddCustomerPage(driver);
    Assert.assertEquals("Dashboard / nopCommerce administration", addCust.getPageTitle());
    
}

@When("User click on customers Menu")
public void user_click_on_customers_menu() throws InterruptedException {
	Thread.sleep(3000);
	addCust.clickOnCustomersMenu(); 
}

@When("click on customers Menu Item")
public void click_on_customers_menu_item() throws InterruptedException {
	Thread.sleep(2000);
	addCust.clickOnCustomersMenuItem();
}

@When("click on Add new button")
public void click_on_add_new_button() throws InterruptedException {
	addCust.clickOnAddnew();
	Thread.sleep(2000);
}

@Then("User can view Add new customer page")
public void user_can_view_add_new_customer_page() {
   Assert.assertEquals("Add a new customer / nopCommerce administration",addCust.getPageTitle() ); 
}
@When("User enter customer info")
public void user_enter_customer_info() throws InterruptedException {
   Logger.info("********Adding new costumer**********");
   Logger.info("********Providing costumer details**********");
   String email=randomestring()+"@gmail.com";
   addCust.setEmail(email);
   addCust.setPassword("test123");
   //Registered-default
   //The customer cannot be in both 'Guests' and 'Registered' Customer roles
   //Add the Customer to 'Guests'  or 'Registered' customer role
   addCust.setCustomerRoles("Guests");
   Thread.sleep(3000);
   
   addCust.setManagerOfVendor("Vendor 2");
   addCust.setGender("Male");
   addCust.setFirstName("Nasimjon");
   addCust.setLastName("Jurakulov");
   addCust.setDob("02/10/1990");   //Format DD/MM/YYYY
   addCust.setCompanyName("Google");
   addCust.setAdminContent("This is for testing..............");
   
}
@When("click on Save button")
public void click_on_save_button() throws InterruptedException {
	Logger.info("********Saving costumer data**********");
	addCust.clickOnSave();
	 Thread.sleep(3000);
}
@Then("User can view confirmation message {string}")
public void user_can_view_confirmation_message(String msg) {
   Assert.assertTrue(driver.findElement(By.tagName("body")).getText().contains("The new customer has been added successfully"));  
}


   //steps for searching a customer using Email ID..................................


@When("Enter customer Email")
public void enter_customer_email() {
  Logger.info("********Searching costumer by email id**********");
  searchCust=new SearchCustomerPage(driver);
  searchCust.setEmail("victoria_victoria@nopCommerce.com");
}
@When("Click on search button")
public void click_on_search_button() throws InterruptedException {
	searchCust.clickSearch();
	Thread.sleep(3000);
}
@Then("User should found Email in the Search table")
public void user_should_found_email_in_the_search_table() {
	boolean status=searchCust.searchCustomerByEmail("victoria_victoria@nopCommerce.com");
	Assert.assertEquals(true, status);
}



   //steps for searching a customer using First Name & Last Name


@When("Enter customer FirstName")
public void enter_customer_first_name() {
	 Logger.info("********Searching costumer by Name**********");
	 searchCust=new SearchCustomerPage(driver);
	 searchCust.setFirstName("Victoria");
}
@When("Enter customer LastName")
public void enter_customer_last_name() {
	searchCust.setLastName("Terces");

}
@Then("User should found Name in the Search table")
public void user_should_found_name_in_the_search_table() {
	boolean status=searchCust.searchCustomerByName("Victoria Terces");
	Assert.assertEquals(true, status);

}



}
