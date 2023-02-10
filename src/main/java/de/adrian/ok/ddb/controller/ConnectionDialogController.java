package de.adrian.ok.ddb.controller;

import de.adrian.ok.ddb.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class ConnectionDialogController {
    public Pane dialogPane;
    public TextField hostnameLabel;
    public TextField portLabel;
    public TextField usernameLabel;
    public PasswordField passwordLabel;
    public static boolean dataEntered = false;

    public void onSubmitClicked(ActionEvent actionEvent) {
        if(checkIfEmpty(hostnameLabel) | checkIfEmpty(portLabel) | checkIfEmpty(usernameLabel) | checkIfEmpty(passwordLabel)) return;


        int port = Integer.parseInt(portLabel.getText());

        DatabaseManager.get().setConnectionInfo(hostnameLabel.getText(), port, usernameLabel.getText(), passwordLabel.getText());
        dataEntered = true;
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.close();
    }

    private boolean checkIfEmpty(TextField portLabel) {
        if(portLabel.getText().equals("")) {
            portLabel.setStyle("-fx-border-color: RED");
            new Thread(() -> {
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                portLabel.setStyle(null);
            }).start();
            return true;
        }
        return false;
    }

    public void onAbortClicked(ActionEvent actionEvent) {
        dataEntered = false;
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.close();
    }

    public void onPortKeyTyped(KeyEvent keyEvent) {
        portLabel.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
    }

}
