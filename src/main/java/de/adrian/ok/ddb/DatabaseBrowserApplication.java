package de.adrian.ok.ddb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class DatabaseBrowserApplication extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(DatabaseBrowserApplication.class.getResource("MainWindow.fxml"));
        VBox box = loader.load();
        primaryStage.setScene(new Scene(box, box.getPrefWidth(), box.getPrefHeight()));
        primaryStage.setTitle("Datenbank Browser");
        primaryStage.setResizable(false);
        primaryStage.show();
        DatabaseBrowserApplication.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
