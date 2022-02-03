package testRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions
		(
		 features={".//Features/Customers.feature"},
		 glue={"stepDefinitions"},
		 dryRun=false,
		 monochrome=true
		 //plugin= "pretty"
		)

public class TestRunCutomers {

	
}
//"html:test-output"