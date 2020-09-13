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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class PrimaryController implements Initializable {

    @FXML public ListView textlistview = new ListView();
    @FXML private TextArea annotationtextarea;
    @FXML private Button viewtextbutton;
    @FXML private Button ALGObutton;
    @FXML private TextArea tagstextarea;
    @FXML private Label ALGOlabel;
    @FXML private TextArea ALGOannotationtextarea;
    @FXML private TextArea ALGOtagstextarea;
    @FXML private Button refreshbutton;
    @FXML private SplitPane splitpane;
    public ArrayList<TextInfo> textInfoArrayList;
    ObservableList options;


    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public void UpdateListView(ListView listView){
        textInfoArrayList = (ArrayList<TextInfo>) DbUtils.getTextInfo();
        ArrayList<String> Names = new ArrayList<>();
        for(TextInfo items : textInfoArrayList){
            //listView.getItems().addAll(items.getName());
            Names.add(items.getName());
        }
//        final ObservableList options = FXCollections.observableArrayList(Names);
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
    private void displayAddFileScene(ActionEvent event) {
    //NewScenes.NewScene("AddFile");

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("AddFile.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            AddFileController addFileController = fxmlLoader.getController();
            stage.showAndWait();
            Optional<String> result = addFileController.getNewItem();
            //stage.show();
            if (result.isPresent()){
                UpdateListView(textlistview);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void displayDeleteFileScene(ActionEvent event) {
        //NewScenes.NewScene("DeleteFile");
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteFile.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            DeleteFileController deleteFileController = fxmlLoader.getController();
            stage.showAndWait();
            Optional<String> result = deleteFileController.getNewItem();
            //stage.show();
            if (result.isPresent()){
                //System.out.println("Your name: " + result.get());
                UpdateListView(textlistview);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
    private void handleItemClicks() {
        TextClassifier classifier = new TextClassifier();
        textlistview.setOnMouseClicked(mouseEvent -> {
            String selected = textlistview.getSelectionModel().getSelectedItem().toString();
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

    public void ShowHelp(ActionEvent actionEvent) {
        NewScenes.NewScene("HelpScene");
    }
}
