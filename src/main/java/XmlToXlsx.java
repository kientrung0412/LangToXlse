import model.StringValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XmlToXlsx {

    private List<String> folders;

    private final SAXBuilder saxBuilder;
    private final XSSFWorkbook workbook;
    private final XSSFSheet hssfSheet;
    private XSSFCellStyle cellStyle;

    private int lastRow = 0;


    public XmlToXlsx(String... folders) {
        this.folders = Arrays.asList(folders);
        saxBuilder = new SAXBuilder();
        workbook = new XSSFWorkbook();
        hssfSheet = workbook.createSheet("Android");
    }


    public void cover() throws IOException {

        folders.stream().map(s -> new File(s).listFiles()).filter(Objects::nonNull).forEach(files -> {
//            Arrays.stream(files).filter(File::isFile).forEach(this::xmlToXlsx);
            xmlToXlsx(files);
        });

        writeFile();
    }

    private void xmlToXlsx(File[] files) {
        Map<String, StringValue> stringValue =
                Arrays.stream(files).map(this::xmlToList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toMap(StringValue::getKey, Function.identity(), (existing, replacement) -> {
                            existing.getValue().addAll(replacement.getValue());
                            return existing;
                        }, ConcurrentHashMap::new));

        List<StringValue> list = new ArrayList<>(stringValue.values());

        try {
            listToXlsx(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<StringValue> xmlToList(File inputFile) {
        try {
            Document document = saxBuilder.build(inputFile);
            Element element = document.getRootElement();
            List<Element> elements = element.getChildren();
            return elements.stream().map(StringValue::apply).sorted(Comparator.comparing(StringValue::getKey)).sorted((stringValue, stringValue2) -> Boolean.compare(stringValue.getTranslate(), stringValue2.getTranslate())).collect(Collectors.toList());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void listToXlsx(List<StringValue> list) throws IOException {
        lastRow++;
        cellStyle = workbook.createCellStyle();
        Font font = hssfSheet.getWorkbook().createFont();
        cellStyle.setWrapText(true);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFont(font);

        list.forEach(stringValue -> {
            Row row = hssfSheet.createRow(lastRow);
            writeData(stringValue, row);
            lastRow++;
        });

    }

    private void writeData(StringValue stringValue, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(stringValue.getKey());

        for (String s : stringValue.getValue()) {

            cell = row.createCell(stringValue.getValue().indexOf(s) + 2);
            cell.setCellValue(s);
        }
    }


    private void writeFile() throws IOException {
        String outPath = String.format("%s/android.xlsx", "D:\\");
        File file = new File(outPath);
        if (file.exists()) file.delete();
        if (file.createNewFile()) {
            FileOutputStream outputStream = new FileOutputStream(outPath);
            workbook.write(outputStream);
            System.out.println("Hoàn thành");
        }
    }
}
