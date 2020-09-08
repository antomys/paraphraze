public class Main {
    public static void main(String[] args) {
//        DbUtils.addText("tygrolovu", IOUtils.getStringFromFile("src/main/resources/tygrolovu.txt"));
//        DbUtils.removeText("tygrolovu");
        TextClassifier classifier = new TextClassifier();
        System.out.println("misto-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/misto-paraphraz.txt")));
        System.out.println("tygrolovu-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/tygrolovu-paraphraz.txt")));
        System.out.println("zemlya-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/zemlya-paraphraz.txt")));
    }
}
