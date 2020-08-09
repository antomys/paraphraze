public class Main {
    public static void main(String[] args) {
        TextClassifier classifier = new TextClassifier();
        classifier.addDocument("kateruna", "src/main/resources/kateruna.txt");
        classifier.addDocument("misto", "src/main/resources/misto.txt");
        classifier.addDocument("tygrolovu", "src/main/resources/tygrolovu.txt");
        classifier.addDocument("marusya", "src/main/resources/marusya.txt");
        classifier.addDocument("zemlya", "src/main/resources/zemlya.txt");
        System.out.println("kateruna-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/kateruna-paraphraz.txt")));
        System.out.println("misto-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/misto-paraphraz.txt")));
        System.out.println("tygrolovu-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/tygrolovu-paraphraz.txt")));
        System.out.println("marusya-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/marusya-paraphraz.txt")));
        System.out.println("zemlya-paraphraz: " + classifier.matchClass(IOUtils.getStringFromFile("src/main/resources/zemlya-paraphraz.txt")));
    }
}
