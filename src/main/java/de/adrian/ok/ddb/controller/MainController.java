package de.adrian.ok.ddb.controller;

import de.adrian.ok.ddb.DatabaseBrowserApplication;
import de.adrian.ok.ddb.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {
    public ListView<String> itemList;
    public Label connectionStatus;
    public Label loadStatus;
    public Label backLabel;
    private SQLException lastError;


    public void onCreateConnectionClicked(ActionEvent actionEvent) throws IOException {
        Pane connectionDialogPane = new FXMLLoader(MainController.class.getResource("/de/adrian/ok/ddb/dialogs/ConnectionDialog.fxml")).load();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Datenbankverbindung erstellen");
        stage.setScene(new Scene(connectionDialogPane, connectionDialogPane.getPrefWidth(), connectionDialogPane.getPrefHeight()));
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(DatabaseBrowserApplication.getPrimaryStage());
        stage.showAndWait();
        if(!ConnectionDialogController.dataEntered) return;
        try(Connection connection = DatabaseManager.get().getConnection(); PreparedStatement statement = connection.prepareStatement("SHOW DATABASES;")) {
            connectionStatus.setText("Connection established successfully");
            connectionStatus.setCursor(Cursor.DEFAULT);
            connectionStatus.setUnderline(false);
            lastError = null;
            ResultSet set = statement.executeQuery();
            while (set.next()) itemList.getItems().add(set.getString(1));
            set.close();
            loadStatus.setText("Databases loaded");
        } catch (SQLException e) {
            connectionStatus.setText("Error while connecting");
            connectionStatus.setCursor(Cursor.HAND);
            connectionStatus.setUnderline(true);
            lastError = e;
        }
    }

    public void onItemlistClicked(MouseEvent mouseEvent) {
        String database = itemList.getSelectionModel().getSelectedItem();
        if(database == null) return;
        DatabaseManager.get().setDatabase(database);
        try(Connection connection = DatabaseManager.get().getConnection(); PreparedStatement statement = connection.prepareStatement("SHOW TABLES;")) {
            ResultSet set = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onConnectionStatusClicked(MouseEvent mouseEvent) {
        if(lastError == null) return;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(lastError.getClass().getName());
        alert.setContentText(lastError.getLocalizedMessage());
        alert.show();
    }

    public void onLoadStatusClicked(MouseEvent mouseEvent) {
    }

    public void onBackLabelClicked(MouseEvent mouseEvent) {

    }

}
