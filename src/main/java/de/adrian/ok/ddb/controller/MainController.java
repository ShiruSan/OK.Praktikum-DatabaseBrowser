package de.adrian.ok.ddb.controller;

import de.adrian.ok.ddb.DatabaseBrowserApplication;
import de.adrian.ok.ddb.database.DatabaseManager;
import de.adrian.ok.ddb.database.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    public Label viewLabel;
    public TableView<String> dbTable;
    private SQLException lastError;
    private boolean tableView = false;


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
        loadDatabases();
    }

    private void loadDatabases() {
        try(Connection connection = DatabaseManager.get().getConnection(); PreparedStatement statement = connection.prepareStatement("SHOW DATABASES;")) {
            connectionStatus.setText("Connection established successfully");
            connectionStatus.setCursor(Cursor.DEFAULT);
            connectionStatus.setUnderline(false);
            lastError = null;
            tableView = false;
            ResultSet set = statement.executeQuery();
            itemList.getItems().clear();
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
        if(itemList.getSelectionModel().getSelectedItem() == null) return;
        if(tableView) {
            String tableName = itemList.getSelectionModel().getSelectedItem();
            dbTable.getItems().clear();
            dbTable.getColumns().clear();
            try(Connection connection = DatabaseManager.get().getConnection(); PreparedStatement statement =  connection.prepareStatement("SELECT * from `" + tableName + "` LIMIT ?;")) {
                statement.setInt(1, 15);
                Table table = new Table(statement.executeQuery());
                for(int i = 0; i < table.getColumnLength(); i++) {
                    TableColumn col = new TableColumn(table.getColumnName(i));
                    int finalI = i;
                    col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(finalI).toString()));
                    dbTable.getColumns().addAll(col);
                }
                dbTable.setItems(table.asObservableList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String database = itemList.getSelectionModel().getSelectedItem();
            DatabaseManager.get().setDatabase(database);
            try(Connection connection = DatabaseManager.get().getConnection(); PreparedStatement statement = connection.prepareStatement("SHOW TABLES;")) {
                ResultSet set = statement.executeQuery();
                itemList.getItems().clear();
                while(set.next()) itemList.getItems().add(set.getString(1));
                set.close();
                tableView = true;
                viewLabel.setText("Tabellen");
                loadStatus.setText("Tables loaded");
                backLabel.setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
        backLabel.setVisible(false);
        viewLabel.setText("Datenbanken");
        tableView = false;
        loadDatabases();
    }

}
