package model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class StringValue {

    private String key;
    private List<String> value = new ArrayList<>();
    private Boolean translate = true;

    public StringValue(String key, List<String> value, Boolean translate) {
        this.key = key;
        if (value != null) this.value.addAll(value);
        this.translate = translate;
    }

    public String getKey() {
        return key;
    }

    public List<String> getValue() {
        return value;
    }

    public boolean getTranslate() {
        return translate == null;
    }

    public static StringValue apply(Element element) {
        String key = element.getAttribute("name").getValue();
        String value = element.getValue();
        boolean translate = element.getAttribute("translatable") != null;
        return new StringValue(key, List.of(value), translate);

    }

    @Override
    public String toString() {
        return "StringValue{" + "key='" + key + '\'' + ", value='" + value + '\'' + ", translate=" + translate + '}';
    }
}
