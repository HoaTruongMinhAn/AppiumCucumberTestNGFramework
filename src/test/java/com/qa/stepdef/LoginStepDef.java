package com.qa.stepdef;

import com.qa.pages.LoginPage;
import com.qa.pages.MenuPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginStepDef {
    @When("I enter username as {string}")
    public void iEnterUsernameAs(String username) throws InterruptedException {
        new LoginPage().enterUserName(username);
    }

    @When("I enter password as {string}")
    public void iEnterPasswordAs(String password) {
        new LoginPage().enterPassword(password);
    }

    @When("I login")
    public void iLogin() {
        new LoginPage().pressLoginBtn();
    }

    @Then("login should fail with an error {string}")
    public void loginShouldFailWithAnError(String errorMessage) {
        Assert.assertEquals(new LoginPage().getErrTxt(), errorMessage);
    }

    @Then("I should see Setting button displayed")
    public void iShouldSeeSettingButtonDisplayed() {
        Assert.assertTrue(new MenuPage().isSettingButtonDisplayed());
    }
}
