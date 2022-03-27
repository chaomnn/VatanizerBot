package model;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor
public class TextModifier {

    private static final int MAX_INDEX = 265;

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

        List<String> dictionary = new ArrayList<>(List.of("з", "З", "в", "В"));
        AtomicReference<Integer> mIndex = new AtomicReference<>(MAX_INDEX);

        dictionary.forEach(character -> {if (text.contains(character) && text.indexOf(character) < mIndex.get()) {
            mIndex.set(text.indexOf(character));} });

        return mIndex.get() < MAX_INDEX ? text.replaceFirst(String.valueOf(text.charAt(mIndex.get())),
                String.valueOf(text.charAt(mIndex.get())).matches("з|З") ? "Z" : "V") : text;
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
