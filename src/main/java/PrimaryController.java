import java.io.IOException;
import java.net.URL;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.*;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.Text;


public class PrimaryController implements Initializable {
    boolean sceneClosed=false;

    @FXML private ListView textlistview = new ListView();
    @FXML private TextArea annotationtextarea;
    @FXML private Button viewtextbutton;
    @FXML private Button ALGObutton;
    @FXML private TextArea tagstextarea;
    @FXML private Label ALGOlabel;
    @FXML private TextArea ALGOannotationtextarea;
    @FXML private TextArea ALGOtagstextarea;
    @FXML private Button refreshbutton;
    public ArrayList<TextInfo> textInfoArrayList;
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    void UpdateListView(ListView listView){
        textInfoArrayList = (ArrayList<TextInfo>) DbUtils.getTextInfo();
        ArrayList<String> Names = new ArrayList<>();
        
        for(TextInfo items : textInfoArrayList){
            //listView.getItems().addAll(items.getName());
            Names.add(items.getName());
        }
        final ObservableList options = FXCollections.observableArrayList(Names);
        listView.setItems(options);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateListView(textlistview);
        textlistview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //ObservableList list = FXCollections.observableArrayList();
        //loadData(list);
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
    NewScenes.NewScene("AddFile"); sceneClosed=true;
        /*try{
            FXMLLoader addNewItemLoader = new FXMLLoader(getClass().getResource("AddFile.fxml"));
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(addNewItemLoader.load()));
            AddFileController addFileController = addNewItemLoader.getController();
            secondStage.showAndWait();
            Optional<String> result = addFileController.getNewItem();
            if(result.isPresent()){
                eventsList.add(result.get());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } */
    }
    @FXML
    private void displayDeleteFileScene(ActionEvent event) {
        NewScenes.NewScene("DeleteFile"); sceneClosed=true;
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
                    viewtextbutton.setOnMouseClicked(mouseEvent1 -> {
                        annotationtextarea.setText(textInfo.getText());
                    });
                    ALGObutton.setOnMouseClicked(mouseEvent1 -> {
                        String result = classifier.matchClass(textInfo.getText());
                        ALGOlabel.setText("Paraphraze of: "+result);
                        for(TextInfo c:textInfoArrayList){
                            if(result.equals(c.getName())){
                                ALGOtagstextarea.setText(c.getTags());
                                ALGOannotationtextarea.setText(c.getAnnotation());

                            }
                        }
                    });

                }

            }
        });
        }
        public void refreshListView(){

        }
        //textlistview.getSelectionModel().getSelectedItems();

    /*public static List<String> getNames(List<String> names){
        Map<String, List<String>> str = DbUtils.getTextNames();
        for (String key: str.keySet()) {
            names.add(key);
        }
        return names;
    }*/

}
