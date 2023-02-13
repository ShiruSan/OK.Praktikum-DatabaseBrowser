package de.adrian.ok.ddb.controller;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.result.Field;
import de.adrian.ok.ddb.DatabaseBrowserApplication;
import de.adrian.ok.ddb.database.DatabaseManager;
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
        if(tableView) {
            dbTable.getItems().clear();

            String table = itemList.getSelectionModel().getSelectedItem();
            try(Connection connection = DatabaseManager.get().getConnection(); PreparedStatement statement =  connection.prepareStatement("SELECT * from `" + table + "` LIMIT ?;")) {
                statement.setInt(1, 15);
                ResultSet set = statement.executeQuery();
                ResultSetMetaData metaData = (ResultSetMetaData) set.getMetaData();
                ObservableList<String> data = FXCollections.observableArrayList();
                for(int i = 0; i < metaData.getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(metaData.getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    while(set.next()){
                        //Iterate Row
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for(i = 1 ; i<=set.getMetaData().getColumnCount(); i++){
                            //Iterate Column
                            row.add(set.getString(i));
                        }
                        System.out.println("Row [1] added "+row );
                        data.add(row.toString());

                    }

                    dbTable.setItems(data);

                    dbTable.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String database = itemList.getSelectionModel().getSelectedItem();
            if(database == null) return;
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
