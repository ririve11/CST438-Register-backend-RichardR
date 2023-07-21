package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.controller.StudentController;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

public class EndToEndAddStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";
	
	public static final String URL = "http://localhost:3000";
	
	public static final String TEST_USER_EMAIL = "testing@csumb.edu";
	
	public static final String TEST_NAME = "test user";
	
	public static final int TEST_ID = 979;
	
	public static final int SLEEP_DURATION = 1000;
	
	
	@Autowired
	StudentRepository studentrepos;
	
	@Test
	public void AddStudentTest() throws Exception {
		
		Student tst = null;
		
		do {
			tst = studentrepos.findByEmail(TEST_USER_EMAIL);
			
			if (tst != null)
				studentrepos.delete(tst);
		} while (tst != null);
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			WebElement web = driver.findElement(By.xpath("//a[@href='/student']"));
			web.click();
			Thread.sleep(SLEEP_DURATION);
			
			driver.findElement(By.xpath("//input[@name='student_name']")).sendKeys(TEST_NAME);
			driver.findElement(By.xpath("//input[@name='student_email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);
			
			Student stud = studentrepos.findByEmail(TEST_USER_EMAIL);
			assertEquals(TEST_USER_EMAIL, stud.getEmail());
			
	    } catch(Exception e) {
	    	throw e;
	    } finally {
	    	Student stud = studentrepos.findByEmail(TEST_USER_EMAIL);
	    	
	    	if(stud != null) {
	    		studentrepos.delete(stud);
	    	}
	    	
	    	driver.quit();
	    }
	}

}
