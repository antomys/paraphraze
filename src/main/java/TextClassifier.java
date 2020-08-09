import java.util.*;
import java.util.stream.Collectors;

public class TextClassifier {
    private int accuracy = 3;

    private Map<String, Integer> documentsPerClass = new HashMap<>();
    private Map<String, Map<String, Integer>> wordCountPerClass = new HashMap<>();

    private boolean isParamsCalculated = false;
    private Map<String, Map<String, Double>> wordPosPerClass = new HashMap<>();
    private Map<String, Double> classPos = new HashMap<>();
    private Map<String, Double> unknownWordPosPerClass = new HashMap<>();

    public TextClassifier() {
    }

    public void addDocument(String name, String classFile) {
        Map<String, Integer> newWordCount = countWords(classFile);
        Map<String, Integer> wordCount = wordCountPerClass.get(name);
        if (Objects.isNull(wordCount)) {
            wordCountPerClass.put(name, newWordCount);
            documentsPerClass.put(name, 1);
        } else {
            newWordCount.forEach((word, count) ->
                    wordCount.merge(word, count, Integer::sum));
            documentsPerClass.compute(name, (s, c) -> c + 1);
            isParamsCalculated = false;
        }
    }

    public void addDocumentDir(String name, String classDir) {
        IOUtils.getFilesInDir(classDir)
                .forEach(file -> addDocument(name, file));
    }

    public String matchClass(String str) {
        calculate();
        List<String> words = IOUtils.getWords(str);
        Pair<String, Possibility> biggestPos = wordCountPerClass.keySet().stream()
                .map(clas -> new Pair<>(clas, getPos(clas, words)))
                .reduce((pair, pair2) -> pair.rigth.more(pair2.rigth) ? pair : pair2)
                .orElseThrow();

        return biggestPos.left;
    }

    private void calculate() {
        if (isParamsCalculated) {
            return;
        }
        calculateClassPos();
        Set<String> vocab = getVocabulary();
        calculateWordPosPerClass(vocab);
        calculateUnknownWordPosPerClass(vocab);
        isParamsCalculated = true;
    }

    private void calculateUnknownWordPosPerClass(Set<String> vocab) {
        unknownWordPosPerClass = wordCountPerClass.entrySet().stream()
                .map(en -> new Pair<>(en.getKey(),
                        calculateUnknownWordPos(en.getValue(), vocab)))
                .collect(Collectors.toMap(pair -> pair.left, pair -> pair.rigth));
    }

    private Double calculateUnknownWordPos(Map<String, Integer> wordCount,
                                           Set<String> vocab) {
        int numOfAllWordsInClass = wordCount.values().stream().mapToInt(c -> c).sum();
        return 1d / (numOfAllWordsInClass + vocab.size() + 1);
    }

    private void calculateWordPosPerClass(Set<String> vocab) {
        wordPosPerClass = wordCountPerClass.entrySet().stream()
                .map(en -> new Pair<>(en.getKey(),
                        calculateWordPos(en.getValue(), vocab)))
                .collect(Collectors.toMap(pair -> pair.left, pair -> pair.rigth));
    }

    private Map<String, Double> calculateWordPos(Map<String, Integer> wordCount,
                                                 Set<String> vocab) {
        int numOfAllWordsInClass = wordCount.values().stream().mapToInt(i -> i).sum();
        int vocabSize = vocab.size();

        return vocab.stream()
                .map(word -> new Pair<>(
                        word,
                        Objects.isNull(wordCount.get(word)) ? 0 : wordCount.get(word)))
                .map(pair -> new Pair<>(
                        pair.left,
                        ((double) pair.rigth + 1) /
                                (numOfAllWordsInClass + vocabSize)))
                .collect(Collectors.toMap(pair -> pair.left, pair -> pair.rigth));

    }

    private Set<String> getVocabulary() {
        return wordCountPerClass.values().stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());
    }

    private void calculateClassPos() {
        int numOfAllDocs = documentsPerClass.values().stream()
                .mapToInt(c -> c)
                .sum();

        classPos = documentsPerClass.keySet().stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> ((double) documentsPerClass.get(c)) / numOfAllDocs));
    }

    private Possibility getPos(String clas, List<String> words) {
        Map<String, Double> wordPos = wordPosPerClass.get(clas);

        Possibility posWithoutUnknown = words.stream()
                .map(wordPos::get)
                .filter(Objects::nonNull)
                .map(Possibility::new)
                .reduce(new Possibility(classPos.get(clas)), Possibility::multiply);

        return words.stream()
                .filter(w -> !wordPos.containsKey(w))
                .map(w -> unknownWordPosPerClass.get(clas))
                .map(Possibility::new)
                .reduce(posWithoutUnknown, Possibility::multiply);
    }

    private static Map<String, Integer> countWords(String filePath) {
        return IOUtils.getWordsFromFile(filePath).stream()
                .collect(Collectors.toMap(s -> s, s -> 1, Integer::sum));
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    private class Possibility {
        int num;
        int exp;

        Possibility() {
        }

        Possibility(double number) {
            double pow = Math.pow(10, accuracy - 1);
            int d = 0;
            while (number < pow) {
                number *= 10;
                d++;
            }
            num = (int) Math.round(number);
            exp = d;
        }

        Possibility multiply(Possibility possibility) {
            int number = num * possibility.num;
            double pow = Math.pow(10, 3);
            int d = 0;
            for (; number >= pow; d++) {
                number /= 10;
            }
            Possibility result = new Possibility();
            result.num = number;
            result.exp = exp + possibility.exp - d;
            return result;
        }

        boolean more(Possibility p) {
            if (exp < p.exp) {
                return true;
            } else if (exp == p.exp && num > p.num) {
                return true;
            }
            return false;
        }
    }
}
