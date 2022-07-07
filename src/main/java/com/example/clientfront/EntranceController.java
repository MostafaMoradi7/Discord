package com.example.clientfront;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.IOException;

public class EntranceController {
    @FXML
    private Button setting;
    @FXML
    private Button friends;
    @FXML
    private Button directs;
    @FXML
    private Button server;

    public void serverButton(MouseEvent event)throws IOException {
        Image image = new Image(new FileInputStream("group-people.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        server.setGraphic(imageView);
        server.setText(null);
    }

    public void directButton(MouseEvent event)throws IOException {
        Image image = new Image(new FileInputStream("385832-200.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        directs.setGraphic(imageView);
        directs.setText(null);
    }


    public void settingButton(MouseEvent event)throws IOException {
        Image image = new Image(new FileInputStream("81020.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        setting.setGraphic(imageView);
        setting.setText(null);
    }

    public void friendsButton(MouseEvent event)throws IOException {
        Image image = new Image(new FileInputStream("img_199933.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        friends.setGraphic(imageView);
        friends.setText(null);
    }

}
