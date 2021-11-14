package com.gb.cs.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("client.fxml"));
        primaryStage.setTitle("MyCloudClientApp");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("STOP");
        Platform.exit();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
