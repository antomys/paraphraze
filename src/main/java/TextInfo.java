import java.util.ArrayList;
import java.util.List;

public class TextInfo {
    int id;
    String name;
    String text;
    List<String> annotations = new ArrayList<>();
    List<String> tags = new ArrayList<>();

    @Override
    public String toString() {
        return "TextInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", annotations=" + annotations +
                ", tags=" + tags +
                '}';
    }
}
