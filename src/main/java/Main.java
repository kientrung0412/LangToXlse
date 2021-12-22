import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        XmlToXlsx xmlToXlsx = new XmlToXlsx(
                "C:\\Users\\nguye\\Desktop\\Sub\\1",
                "C:\\Users\\nguye\\Desktop\\Sub\\2",
                "C:\\Users\\nguye\\Desktop\\Sub\\3",
                "C:\\Users\\nguye\\Desktop\\Sub\\4"
        );
        xmlToXlsx.cover();

    }

}
