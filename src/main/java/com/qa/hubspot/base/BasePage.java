package com.qa.hubspot.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;


import com.qa.hubspot.utils.OptionsManager;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BasePage {
	
	
	
	public WebDriver driver;
	public Properties prop;
   public OptionsManager OP;
	public static ThreadLocal<WebDriver> TLDriver= new ThreadLocal<WebDriver>();
	
	
	/**
	 * This method is used to initialize the driver on the basis of given browser name
	 * @param BrowserName
	 * @return driver 
	 */
	public WebDriver init_Driv(Properties prop ) {
		
		//OP=new OptionsManager(prop);
		
		
		
		String BrowserName= prop.getProperty("browser").trim();
		
		System.out.println("Launching Browser" + BrowserName);
		  OP=new OptionsManager(prop);
		if(BrowserName.equalsIgnoreCase("CHROME")) {
			WebDriverManager.chromedriver().setup();
			//driver=new ChromeDriver();	
			TLDriver.set(driver=new ChromeDriver(OP.getChromeOptions()));
		}
		else if (BrowserName.equalsIgnoreCase("FireFox")) {
			WebDriverManager.firefoxdriver().setup();
			TLDriver.set(driver=new FirefoxDriver(OP.getFirefoxOptions()));
		}
		else if (BrowserName.equalsIgnoreCase("SAFARI")) {
			WebDriverManager.safaridriver().setup();
			//driver= new SafariDriver();
			TLDriver.set(driver =new SafariDriver());
		}	
		else {
			System.out.println("Browser not configured");
		}
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getDriver().get(prop.getProperty("url"));
		return getDriver();
		
	}
	
	

	public static synchronized WebDriver getDriver(){
		return TLDriver.get();
	}
	/**
	 * This method is used to initialize config properties
	 * @return prop
	 */
	public Properties init_Prop() {
		prop = new Properties();
		String path = null;
		String env = null;
		
		try {
			env = System.getProperty("env");
			System.out.println("Running on Environment: ---->" + env);
			if(env == null){
				path = "./src/main/java/com/qa/hubspot/config/config.properties";
			}
			else{
				switch (env) {
				case "qa":
					path = "./src/main/java/com/qa/hubspot/config/qa.config.properties";
					break;
				case "dev":
					path = "./src/main/java/com/qa/hubspot/config/dev.config.properties";
					break;
				case "stage":
					path = "./src/main/java/com/qa/hubspot/config/stage.config.properties";
					break;
				default:
					System.out.println("Please pass the correct env value....");
					break;
				}
			}
			FileInputStream ip = new FileInputStream(path);
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}
	
	
	
	//take Screenshot
	public String getScreenshot() {
		
	File src=	((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
	String path=System.getProperty("user.dir")+"/ScreenShots/"+System.currentTimeMillis()+".png";	
		File dest =new File(path);
		
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return path;
	}
	
	
	
	
	

}
 