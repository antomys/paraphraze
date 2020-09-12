import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeleteFileController implements Initializable {
    @FXML private javafx.scene.control.Button backbutton;
    @FXML private ComboBox textcombobox ;
    @FXML private javafx.scene.control.TextArea previewtextarea;
    public ArrayList<TextInfo> textInfoArrayList;


    public void DeleteText(ActionEvent actionEvent) {
        DbUtils.removeText(textcombobox.getSelectionModel().getSelectedItem().toString());
        if(previewtextarea.getText() != null) {
            DbUtils.removeText(textcombobox.getSelectionModel().getSelectedItem().toString());
            UpdateCombobox(textcombobox);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Removed Successfully!");
            alert.showAndWait();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Unable to load to DB!");

            alert.showAndWait();

        }
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void GoBackScene(ActionEvent actionEvent) {
        Stage stage = (Stage) backbutton.getScene().getWindow();
        // do what you have to do, CLOSE Motherfucker
        stage.close();
    }
    @FXML
    void Select(ActionEvent actionEvent) throws NullPointerException{
        String s = textcombobox.getSelectionModel().getSelectedItem().toString();
        for(TextInfo textInfo:textInfoArrayList){
            if(s.equals(textInfo.getName())){
                previewtextarea.setText(textInfo.getAnnotation());
                break;
            }
        }


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateCombobox(textcombobox);


    }
    void UpdateCombobox(ComboBox comboBox){
        ArrayList<String> Names = new ArrayList<>();
        textInfoArrayList = (ArrayList<TextInfo>) DbUtils.getTextInfo();
        for(TextInfo items : textInfoArrayList){
            Names.add(items.getName());
        }
        final ObservableList options = FXCollections.observableArrayList(Names);
        textcombobox.setItems(options);

    }
}
