package model.handlers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import model.TextModifier;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedSticker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class InlineUpdateHandler {

    @Getter
    private List<InlineQueryResult> inlineQueryResults = new ArrayList<>();

    private final TextModifier textModifier = new TextModifier();

    private final Map<String, String> stickerDictionary = Map.of("розия",
            "CAACAgIAAxkBAAEEQI9iPEcpZOMPKkmaMbrJ_FPmvLeMMAACFxoAAgdawUljX3ljR7I27yME",
            "техноналоги", "CAACAgIAAxkBAAEEPqBiOswkE0jh8admoeHPzwhCwiGg8gAC8wAD72qYHLaNi8fUfKV0IwQ",
            "билет", "CAACAgIAAxkBAAEEQatiPMkR_U6nhuZNKFGPGasDg2V-lAACiRsAArF06Ek4TYc053k2kCME");

    @SneakyThrows
    public void setInlineQueryResults(InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery();

        if (!query.isEmpty()) {
            addToQueryResults(inlineQueryResults, textModifier.changeText(query));

            if (!textModifier.changeText(query).equals(textModifier.changeTextWithoutC(query))) {
                addToQueryResults(inlineQueryResults, textModifier.changeTextWithoutC(query));
            }

            if (!textModifier.changeOnlyFirstLetter(query).equals(textModifier.changeText(query)) &&
                    !textModifier.changeOnlyFirstLetter(query).equals(textModifier.changeTextWithoutC(query))) {
                addToQueryResults(inlineQueryResults, textModifier.changeOnlyFirstLetter(query));
            }

            stickerDictionary.forEach((stickerName, stickerId) -> addStickerToQueryResults(query,
                    stickerName, stickerId, inlineQueryResults));
        }
    }

    private void addStickerToQueryResults(String query, String stickerName, String stickerId,
                                          List<InlineQueryResult> inlineQueryResults) {
        if (query.contains(stickerName)) {
            inlineQueryResults.add(InlineQueryResultCachedSticker.builder()
                    .id(stickerName)
                    .stickerFileId(stickerId)
                    .build());
        }
    }

    private void addToQueryResults(List<InlineQueryResult> inlineQueryResults, String text) {
        inlineQueryResults.add(InlineQueryResultArticle.builder()
                .id(String.valueOf(text.hashCode()))
                .title(text)
                .inputMessageContent(InputTextMessageContent.builder()
                        .messageText(text)
                        .build())
                .build());
    }
}