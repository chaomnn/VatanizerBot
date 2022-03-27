package model;

import lombok.NoArgsConstructor;

import java.util.TreeMap;

@NoArgsConstructor
public class TextModifier {

    private static final String z = "з";
    private static final String v = "в";

    public String changeText(String text) {
        return changeTextWithoutC(text)
                .replaceAll("[сС]", "Z");
    }

    public String changeTextWithoutC(String text) {
        return text
                .replaceAll("[зЗ2]", "Z")
                .replaceAll("[вВ]", "V");
    }

    public String processString(String text) {

        TreeMap<Integer, String> map = new TreeMap<>();

        // need to change that
        map.put(text.indexOf(z), "Z");
        map.put(text.indexOf(z.toUpperCase()), "Z");
        map.put(text.indexOf(v), "V");
        map.put(text.indexOf(v.toUpperCase()), "V");

        Integer minIndex = map.firstKey();
        if (map.firstKey() == -1 && map.size() > 1) {
            minIndex = map.higherKey(map.firstKey());
        }

        if (minIndex > -1) {
            return text.replaceFirst(String.valueOf(text.charAt(minIndex)), map.get(minIndex));
        } else return text;
    }

    public String changeOnlyFirstLetter(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        int prevIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                stringBuilder.append(processString(text.substring(prevIndex, i)));
                prevIndex = i;
            }
        }
        stringBuilder.append(processString(text.substring(prevIndex)));
        return stringBuilder.toString();
    }

}
