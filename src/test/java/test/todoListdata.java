package test;

import org.testng.annotations.DataProvider;

public class todoListdata {
	
    @DataProvider(name="todoData")	
	public Object[][] todoListvalues() {
		return new Object[][] { 
			{ "Drink Water Every Hour", 1 }, 
			{ "Exercise Daily", 2 }, 
			{ "Clean the house", 3 },
			{ "Meditate Daily", 4 } };
	}

}
