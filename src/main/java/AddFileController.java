import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.apache.commons.io.FilenameUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;

public class AddFileController implements Initializable {

    private File selectedFile;
    @FXML
    private TextArea previewbox = new TextArea();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField pathname;
    @FXML
    private TextField textname;
    @FXML private javafx.scene.control.Button backbutton;
    @FXML private Button uploadbutton;
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    public void InputFile(javafx.event.ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);
        String filename=selectedFile.getAbsolutePath();

        if (selectedFile != null) {
            ArrayList<String> data = new ArrayList<>();
            // actionStatus.setText("File selected: " + selectedFile.getName());
            textname.setText(FilenameUtils.removeExtension(selectedFile.getName()));
            pathname.setText(selectedFile.getAbsolutePath());
            /*try {
                Scanner scanner = new Scanner(selectedFile);
                while(scanner.hasNextLine()) {
                    data.add(scanner.nextLine());
                }
                previewbox.setText(data.toString());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }*/
            String s = TxtLoader.readFile2(selectedFile.getAbsolutePath());
            previewbox.setText(s);


        }
        else {
            pathname.setText("File selection cancelled.");
        }
    }

    @FXML
    private void UploadFile(javafx.event.ActionEvent actionEvent){
        if(textname.getText() != null)
        {
            uploadbutton.setDisable(false);
            DbUtils.addText(textname.getText(), IOUtils.getStringFromFile(selectedFile.getAbsolutePath()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Uploaded to DB Successfully!");

            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Unable to load!");

            alert.showAndWait();

        }
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uploadbutton.setDisable(true);
    }

    public void BackMainScene(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backbutton.getScene().getWindow();
        // do what you have to do, CLOSE Motherfucker
        stage.close();

    }
}
