package com.shreyashsinha.notepad;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;


public class Controller {
    @FXML
    private BorderPane mainPane;
    @FXML
    private TextArea textArea;

    private Boolean isSaved = false;
    private File file;
    private String currentText = "";


    @FXML
    public void handleSaveAs() throws IOException {
        if (isSaved) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File already saved");
            alert.setHeaderText(null);
            alert.setContentText("File is already saves at location: " + file.toString());
            alert.showAndWait();
            return;
        }

        String text = textArea.getText();

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save As");
        chooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        try {
            File selectedFile = chooser.showSaveDialog(mainPane.getScene().getWindow());
            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write(text);
            }
            this.file = selectedFile;
            this.isSaved = true;
            this.currentText = text;
        } catch (Exception ignored) {}
    }


    @FXML
    public void handleSave() throws IOException {
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                String text = textArea.getText();
                writer.write(text);
                this.currentText = text;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save The File");
            alert.setHeaderText(null);
            alert.setContentText("Please save the file first by Save As");
            alert.showAndWait();
        }
    }


    @FXML
    public void handleClose() throws IOException, InterruptedException {
        if (currentText.equals(textArea.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File not Saved");
            alert.setHeaderText(null);
            alert.setContentText("File is not saved. Do you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        }
    }


    @FXML
    public void handleOpen() throws IOException {
        if (!textArea.getText().equals(currentText)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File not Saved");
            alert.setHeaderText(null);
            alert.setContentText("File is not saved. Do you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );

        try {
            File selectedFile = chooser.showOpenDialog(mainPane.getScene().getWindow());
            this.isSaved = true;
            this.file = selectedFile;
            try (Scanner scanner = new Scanner(new FileReader(selectedFile))) {
                textArea.clear();
                StringBuilder text = new StringBuilder();
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine());
                    text.append("\n");
                }
                textArea.setText(text.toString());
                this.currentText = text.toString();
            }
        } catch (Exception ignored) {}
    }


    @FXML
    public void handleNew() {
        if (!currentText.equals(textArea.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File not Saved");
            alert.setHeaderText(null);
            alert.setContentText("File is not saved. Do you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        file = null;
        currentText = "";
        isSaved = false;
        textArea.clear();
    }
}
