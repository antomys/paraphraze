/*
import java.util.*;

public class Main {
    public static void main(String[] args) {
        //System.out.println(DbUtils.getTextInfo());
       // DbUtils.addText("tygrolovu", IOUtils.getStringFromFile("src/main/resources/tygrolovu.txt"));
        TextClassifier classifier = new TextClassifier();

        ArrayList<TextInfo> textInfoArrayList = (ArrayList<TextInfo>) DbUtils.getTextInfo();
        String name;
        for(int i=0;i<textInfoArrayList.size();i++){
            name=textInfoArrayList.get(i).getText();
            System.out.println(classifier.matchClass(name));
        }


 //
        System.out.println("tygrolovu-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/tygrolovu-paraphraz.txt")));
        System.out.println("zemlya-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/zemlya-paraphraz.txt")));
    }

}
*/
