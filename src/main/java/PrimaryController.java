import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.util.*;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.w3c.dom.Text;


public class PrimaryController implements Initializable {

    @FXML private ListView<String> ListViewDB;
    @FXML private TextArea TextAreaDB;
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ObservableList list = FXCollections.observableArrayList();
        //loadData(list);

    }
    @FXML
    private void displayAddFileScene(ActionEvent event) {
    NewScenes.NewScene("AddFile");
    }
    @FXML
    private void displayDeleteFileScene(ActionEvent event) {
        NewScenes.NewScene("DeleteFile");
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
