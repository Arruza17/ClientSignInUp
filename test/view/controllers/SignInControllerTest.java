/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controllers;

import application.Application;
import exceptions.MaxCharactersException;
import exceptions.PasswordFormatException;
import exceptions.ServerDownException;
import exceptions.UserNotFoundException;

import static org.testfx.matcher.base.NodeMatchers.*;
import static org.testfx.matcher.control.TextInputControlMatchers.*;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Testing class for SignIn view and controller. Tests SignIn view behavior
 * using TestFX framework
 *
 * @author Adrián Pérez, Jorge Crespo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignInControllerTest extends ApplicationTest {

    //private attributes to manage javaFx window components.
    private TextField tfUser;
    private PasswordField passwd;

    /**
     * This method sets the primary stage of the javaFx window and sets up the
     * application.
     *
     * @throws TimeoutException
     */
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Application.class);
    }

    @Test
    public void test00_serverOk() {

        clickOn("#tfUser");
        write("test");

        clickOn("#tfPassword");
        write("abcd*1234");

        clickOn("#primaryButton");

        verifyThat(new ServerDownException().getMessage(), isVisible());

        clickOn("Aceptar");
        clickOn("#tfUser");
        eraseText(4);
        clickOn("#tfPassword");
        eraseText(9);

    }

    /**
     * Tests a login with correct credentials checking if the welcome window is
     * visible.
     */
    @Test
    public void test01_signInOk() {
        clickOn("#btnSignOut");
        clickOn("Aceptar");
        clickOn("#tfUser");
        write("test");

        clickOn("#tfPassword");
        write("abcd*1234");

        clickOn("#primaryButton");

        /*Verify that the greeting label is visible on the welcome window
        in order to check if it's open.      
         */
        //Verify that the user's name is the correct one
        String fullName = "Welcome back, test test";
        verifyThat(fullName, isVisible());
        clickOn("#btnSignOut");
        clickOn("Aceptar");

    }

    @Test
    public void test021_SignInBadUser() {
        String alert = new UserNotFoundException().getMessage();
        clickOn("#tfUser");
        write("incorrect");
        clickOn("#tfPassword");
        write("abcd*1234");
        clickOn("#primaryButton");
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        clickOn("#tfUser");
        eraseText(9);
        clickOn("#tfPassword");
        eraseText(9);
    }

    @Test
    public void test022_SignInBadPasswd() {
        //Verifying if password does not exist in db
        String alert = new UserNotFoundException().getMessage();
        clickOn("#tfUser");
        write("test");
        clickOn("#tfPassword");
        write("incorrect");
        clickOn("#primaryButton");
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        clickOn("#tfUser");
        eraseText(4);
        clickOn("#tfPassword");
        eraseText(9);

        //Inputting wrong password format
        alert = new PasswordFormatException().getMessage();
        clickOn("#tfUser");
        write("test");
        clickOn("#tfPassword");
        write("inco rect");
        clickOn("#primaryButton");
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        clickOn("#tfUser");
        eraseText(4);
        clickOn("#tfPassword");
        eraseText(9);

    }

    /**
     * Tests if Alert shows when at least one field is empty.
     */
    @Test
    public void test03_fieldsEmpty() {

        //Exception message string.
        String alert = "At least one field is empty.";

        //test empty passwordField.
        tfUser = lookup​("#tfUser").query();
        clickOn(tfUser);
        write("test");
        clickOn("#primaryButton");

        //verify that the exception message appears in the alert.
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        clickOn(tfUser);
        eraseText(tfUser.getText().length());

        //test empty tfUser.
        passwd = lookup​("#tfPassword").query();
        clickOn(passwd);
        write("test");
        clickOn("#primaryButton");
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        clickOn(passwd);
        eraseText(passwd.getText().length());

        //test both empty fields.
        clickOn("#primaryButton");
        verifyThat(alert, isVisible());
        clickOn("Aceptar");

    }

    /**
     * Tests if Alert shows when typing max characters in at least one field.
     */
    @Test
    public void test04_maxChars255() {

        //String that contains 255 chars.
        String maxChar = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        //Exception message string.
        String alert = new MaxCharactersException().getMessage();
        tfUser = lookup​("#tfUser").query();
        passwd = lookup​("#tfPassword").query();
        //Test max characters (255) on userField.
        tfUser.setText(maxChar);
        passwd.setText("prueba");

        clickOn("#primaryButton");

        //verify that the exception message appears in the alert.
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        tfUser.setText("");
        passwd.setText("");

        tfUser = lookup​("#tfUser").query();
        passwd = lookup​("#tfPassword").query();
        //Test max characters (255) on password field.
        passwd.setText(maxChar);
        tfUser.setText("prueba");

        clickOn("#primaryButton");

        //verify that the exception message appears in the alert.
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        tfUser.setText("");
        passwd.setText("");

        tfUser = lookup​("#tfUser").query();
        passwd = lookup​("#tfPassword").query();
        //Test max chaarcters (255) on both fields.
        tfUser.setText(maxChar);
        passwd.setText(maxChar);

        clickOn("#primaryButton");

        //verify that the exception message appears in the alert.
        verifyThat(alert, isVisible());
        clickOn("Aceptar");
        tfUser.setText("");
        passwd.setText("");
    }

    /**
     * Tests sign Up hyperlink and verifies that the sign up window is visible.
     */
    @Test
    public void test05_signUpLink() {
        clickOn("#hlSignUp");

        /*Verify that "Sign Up" string is visible on the welcome window
        in order to check if it is open.
         */
        verifyThat("Sign Up", isVisible());

    }

}
