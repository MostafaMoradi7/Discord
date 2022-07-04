package com.example.clientfront;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController {
    private String username1;
    private String password1;
    private String email1;
    private String phone_Number1;
    private ClientHandler clientHandler;
    private String usernameLogin1;
    private String passwordLogin1;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label username_error_Login;
    @FXML
    private Label password_error_Login;
    @FXML
    private Button login;
    @FXML
    private TextField usernameLogin;
    @FXML
    private TextField passwordLogin;
    @FXML
    private TextField email;
    @FXML
    private TextField phone_Number;
    @FXML
    private Label username_error;
    @FXML
    private Label password_error;
    @FXML
    private Label email_error;
    @FXML
    private Label phone_Number_error;
    @FXML
    private Button backToLogin;
    @FXML
    private Button backToRegister;



    public void register() {
        //username should be at least 6 characters and only contain letters and numbers
        boolean userNameIsValid = (username.getText().length() > 5) && (!username.getText().contains(" "));
        //password should be at least 6 characters and only contain letters and numbers
        boolean passwordIsValid = (password.getText().length() > 5) && Pattern.matches("[a-zA-Z0-9]+", password.getText());
        //email should contain @ and . and can contain letters and numbers657
        boolean emailIsValid = email.getText().contains("@") && email.getText().contains(".") && !email.getText().contains(" ") && !email.getText().contains(";");
        //phone number should be at least 10 characters and only contain numbers
        boolean phone_NumberIsValid = (phone_Number.getText().length() > 9) && Pattern.matches("[0-9]+", phone_Number.getText());
        if (userNameIsValid && passwordIsValid && emailIsValid && phone_NumberIsValid) {
            username1 = username.getText();
            password1 = password.getText();
            email1 = email.getText();
            phone_Number1 = phone_Number.getText();
            clientHandler = new ClientHandler();
            clientHandler.sendAndReceive(new PortableData("registration",new Client(username1, password1, email1, phone_Number1,Status.ONLINE)));
            clientHandler.run();
        } else {
            if (!userNameIsValid){
                username_error.setText("username is invalid");
            }
            if (!passwordIsValid){
                password_error.setText("password is invalid");
            }
            if (!emailIsValid){
                email_error.setText("email is invalid");
            }
            if (!phone_NumberIsValid){
                phone_Number_error.setText("phone number is invalid");
            }
        }
    }
    public void login() {
        boolean userNameIsValid = (username.getText().length() > 5) && (!username.getText().contains(" "));
        boolean passwordIsValid = (password.getText().length() > 5) && Pattern.matches("[a-zA-Z0-9]+", password.getText());

        if (userNameIsValid && passwordIsValid) {
            ClientHandler clientHandler = new ClientHandler();
            PortableData response = clientHandler.sendAndReceive(new PortableData("login", new Client(username.getText(), password.getText(), null, null, Status.ONLINE)));
            if(response.getOrder().equals("200")){
                System.out.println("login success");
            }
            else{
                System.out.println("login failed");
            }

        } else {
            if (!userNameIsValid) {
                username_error.setText("username is invalid");
            }
            if (!passwordIsValid) {
                password_error.setText("password is invalid");
            }
        }
    }
    public void setBackToRegister(ActionEvent event)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void setBackToLogin(ActionEvent event)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
