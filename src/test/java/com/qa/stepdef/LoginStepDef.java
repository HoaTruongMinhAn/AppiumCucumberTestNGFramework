package com.qa.stepdef;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepDef {
    @When("I enter username as {string}")
    public void iEnterUsernameAs(String username) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I enter password as {string}")
    public void iEnterPasswordAs(String password) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I login")
    public void iLogin() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("login should fail with an error {string}")
    public void loginShouldFailWithAnError(String errorMessage) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("I should see Setting button displayed")
    public void iShouldSeeSettingButtonDisplayed() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
