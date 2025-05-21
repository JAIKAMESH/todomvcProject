package test;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestSuite {
WebDriver driver;

@BeforeSuite
public void setup() throws InterruptedException {
	driver=new ChromeDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
	//Navigation for todomvc Angular Page
	driver.get("https://todomvc.com/");
    Assert.assertTrue(driver.getTitle().equals("TodoMVC"),"TodoMVC page is not loading");
	
	driver.findElement(By.xpath("//a[@data-source=\"https://angular.dev\"]")).click();
    Assert.assertTrue(driver.getTitle().equals("TodoMVC: Angular"),"Angular page is not loading");	
}

@Test(priority = 1,dataProvider ="todoData",dataProviderClass = todoListdata.class)
public void TS01_Add_to_do(String todovalue,int count) throws InterruptedException {	
   //Add New Value
   WebElement todolist=driver.findElement(By.xpath("//input[@placeholder='What needs to be done?']"));
   todolist.sendKeys(todovalue);
   todolist.sendKeys(Keys.ENTER);
   Thread.sleep(1000);
   
   //Validate New value
   String listValues=driver.findElement(By.xpath("(//div[@class=\"view\"])["+count+"]")).getText();
   Assert.assertTrue(listValues.equals(todovalue),todovalue+" is not added in todoList (Actual-"+listValues);
   
  //Extract Total count
  WebElement todoCount=driver.findElement(By.xpath("//span[@class=\"todo-count\"]"));
  String errorMessage=count+" is mismatch";
  
  //Validate toDoList count
  if(count==1) {
	  Assert.assertTrue(todoCount.getText().equals(count+" item left"),errorMessage);}
  else {
  Assert.assertTrue(todoCount.getText().equals(count+" items left"),errorMessage);}
}

@Test(priority = 2)
public void TS02_Mark_Completed() {
	
	//Extract Total Values before completed
	String beforeCount=driver.findElement(By.xpath("(//span[@class=\"todo-count\"])/strong")).getText();
	int beforecount=Integer.parseInt(beforeCount);
	driver.findElement(By.xpath("//input[@class='toggle']")).click();

	//Extract Total Values after completed
	String afterCount=driver.findElement(By.xpath("(//span[@class=\"todo-count\"])/strong")).getText();
	int aftercount=Integer.parseInt(afterCount);
	
	//Validate Total Count before and after deleted
	Assert.assertTrue((beforecount-1)==aftercount,"Completed Record Count is not updated");
}

@Test(priority = 3)
public void TS03_Delete_the_item() throws InterruptedException {
	//Store Delete value
	WebElement beforedeleteItem=driver.findElement(By.xpath("(//div[@class='view'])[2]"));
	String beforeDeletedValue=beforedeleteItem.getText();
	
	//Deleted second Value
	Actions action=new Actions(driver);
	action.moveToElement(beforedeleteItem).perform();
	driver.findElement(By.xpath("(//button[@class='destroy'])[2]")).click();
	
	//Validate Deleted Value
	WebElement deletedItem=driver.findElement(By.xpath("(//div[@class='view'])[2]"));
	String DeletedValue=deletedItem.getText();
    Assert.assertTrue(!(DeletedValue.equals(beforeDeletedValue)),"Not able to delete items");
    Thread.sleep(3000);
}


@Test(priority = 4)
public void TS04_Delete_the_item() throws InterruptedException {
	//Extract All values before click Active and Completed Value
	List<WebElement> activeItem=driver.findElements(By.xpath("(//li[not(@class='completed')])/div/label"));
	String completedItems=driver.findElement(By.xpath("(//li[@class='completed'])/div/label")).getText();

	//Validate Active Item after click Active tab
	driver.findElement(By.linkText("Active")).click();
	List<WebElement> actualActiveItem=driver.findElements(By.xpath("(//li[not(@class='completed')])/div/label"));
	Assert.assertTrue(activeItem.equals(actualActiveItem),"Active Item is mismatch");
	Thread.sleep(5000);
    
	//Validate Completed Item after Click Completed tab
    driver.findElement(By.linkText("Completed")).click();
    String actualCompletedItem=driver.findElement(By.xpath("(//div[@class='view'])/label")).getText();
    Assert.assertTrue(completedItems.contentEquals(actualCompletedItem),"Completed Items are mistmatch");
   
}

@AfterSuite
public void close() {
	driver.quit();
}
}
