import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.util.*;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


public class PrimaryController implements Initializable {

    @FXML private ListView<String> ListViewDB = new ListView<>();
    @FXML private TextArea TextAreaDB;
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<String, List<String>> str = DbUtils.getTextNames();
    ListViewDB.getItems().addAll(str.keySet());
    ListViewDB.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    @FXML
    private void displayAddFileScene(ActionEvent event) throws IOException {
    NewScenes.NewScene("AddFile");
    }

    /*private static Parent loadFXML(String fxml) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }*/

    @FXML
    private void CloseApp(){
        Platform.exit();
        System.exit(0);
    }
    /*public static List<String> getNames(List<String> names){
        Map<String, List<String>> str = DbUtils.getTextNames();
        for (String key: str.keySet()) {
            names.add(key);
        }
        return names;
    }*/

}
