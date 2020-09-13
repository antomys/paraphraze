import java.io.IOException;
import java.net.URL;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class PrimaryController implements Initializable {

    @FXML public ListView<String> textlistview;
    @FXML private TextArea annotationtextarea;
    @FXML private Button viewtextbutton;
    @FXML private Button ALGObutton;
    @FXML private TextArea tagstextarea;
    @FXML private Label ALGOlabel;
    @FXML private TextArea ALGOannotationtextarea;
    @FXML private TextArea ALGOtagstextarea;
    @FXML private Button refreshbutton;
    public ArrayList<TextInfo> textInfoArrayList;
    ObservableList<String> options;
    public void UpdateListView(ListView<String> listView){
        textInfoArrayList = (ArrayList<TextInfo>) DbUtils.getTextInfo();
        ArrayList<String> Names = new ArrayList<>();
        for(TextInfo items : textInfoArrayList){
            Names.add(items.getName());
        }
        options= FXCollections.observableArrayList(Names);
        listView.setItems(options);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateListView(textlistview);
        textlistview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        handleItemClicks();
        BooleanBinding booleanBind = tagstextarea.textProperty().isEmpty()
                .or(annotationtextarea.textProperty().isEmpty());
        viewtextbutton.disableProperty().bind(booleanBind);
        refreshbutton.setOnAction(actionEvent -> {
            UpdateListView(textlistview);
            ALGOannotationtextarea.clear();
            ALGOtagstextarea.clear();
            ALGOlabel.setText("Algorithm result");
        });

    }
    @FXML
    private void displayAddFileScene() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("AddFile.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle("Add File");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/add.png")));
            AddFileController addFileController = fxmlLoader.getController();
            stage.showAndWait();
            Optional<String> result = addFileController.getNewItem();
            if (result.isPresent()){
                UpdateListView(textlistview);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void displayDeleteFileScene() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteFile.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle("Delete File");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/del.png")));
            DeleteFileController deleteFileController = fxmlLoader.getController();
            stage.showAndWait();
            Optional<String> result = deleteFileController.getNewItem();
            if (result.isPresent()){
                UpdateListView(textlistview);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void CloseApp(){
        Platform.exit();
        System.exit(0);
    }
    private void handleItemClicks() {
        TextClassifier classifier = new TextClassifier();
        textlistview.setOnMouseClicked(mouseEvent -> {
            String selected = textlistview.getSelectionModel().getSelectedItem();
            textInfoArrayList= (ArrayList<TextInfo>) DbUtils.getTextInfo();
            for(TextInfo textInfo: textInfoArrayList){
                if(selected.equals(textInfo.getName())){
                    tagstextarea.setText(textInfo.getTags());
                    annotationtextarea.setText(textInfo.getAnnotation());
                    ALGOtagstextarea.clear();
                    ALGOannotationtextarea.clear();
                    ALGOlabel.setText("Algorithm result");
                    viewtextbutton.setOnMouseClicked(mouseEvent1 -> {
                        if(annotationtextarea.getText().equals(textInfo.getAnnotation())){
                            annotationtextarea.setText(textInfo.getText());
                        }
                        else{annotationtextarea.setText(textInfo.getAnnotation());}
                    });
                    ALGObutton.setOnMouseClicked(mouseEvent1 -> {
                        String result = classifier.matchClass(textInfo.getText());
                        ALGOlabel.setText("Paraphraze of: "+result);
                        for(TextInfo c:textInfoArrayList){
                            if(result.equals(c.getName())){
                                ALGOtagstextarea.setText(c.getAnnotation());
                                ALGOannotationtextarea.setText(c.getText());

                            }
                        }
                    });
                }
            }
        });
        }

    public void ShowHelp() {
        NewScenes.NewScene("HelpScene");
    }
}
