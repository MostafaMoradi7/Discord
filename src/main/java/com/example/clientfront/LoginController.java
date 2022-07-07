package com.example.clientfront;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {
    boolean userNameIsValid;
    boolean passwordIsValid;
    private ClientHandler clientHandler;
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
    private Button backToRegister;
    public void login(ActionEvent event)throws IOException {
         userNameIsValid = (usernameLogin.getText().length() > 5) && (!usernameLogin.getText().contains(" "));
         passwordIsValid = (passwordLogin.getText().length() > 5) && Pattern.matches("[a-zA-Z0-9]+", passwordLogin.getText());

        if (userNameIsValid && passwordIsValid) {
            clientHandler = new ClientHandler();
            PortableData response = clientHandler.sendAndReceive(new PortableData("login", new Client(usernameLogin.getText(), passwordLogin.getText(), null, null, Status.ONLINE)));
            if(response.getOrder().equals("200")){
                System.out.println("login success");
//                Image image = new Image(new FileInputStream("direct.jpg"));
//                ImageView imageView = new ImageView(image);
//                imageView.setFitHeight(90);
//                imageView.setFitWidth(95);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("entrance.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else{
                System.out.println("login failed");
            }

        } else {
            if (!userNameIsValid) {
                username_error_Login.setText("username is invalid");
            }
            if (!passwordIsValid) {
                password_error_Login.setText("password is invalid");
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

}
