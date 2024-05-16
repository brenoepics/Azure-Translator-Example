package io.github.brenoepics;

import static io.github.brenoepics.TranslationConsoleApp.getAzureSubscription;

import io.github.brenoepics.at4j.AzureApi;
import io.github.brenoepics.at4j.AzureApiBuilder;
import io.github.brenoepics.at4j.data.Translation;
import io.github.brenoepics.at4j.data.request.TranslateParams;
import io.github.brenoepics.at4j.data.response.TranslationResponse;
import java.util.List;
import java.util.Optional;

public class ExampleTranslator {
  public static void main(String[] args) {
    TranslationConsoleApp.AzureSubscription keys = getAzureSubscription();
    AzureApi api =
        new AzureApiBuilder()
            .setKey(keys.subscriptionKey())
            .region(keys.subscriptionRegion())
            .build();
    TranslateParams params =
        new TranslateParams("Hello World!", List.of("pt", "es", "fr")).setSourceLanguage("en");
    Optional<TranslationResponse> translationResult = api.translate(params).join();

    translationResult.ifPresent(
        response ->
            response.getResultList().stream()
                .flatMap(result -> result.getTranslations().stream())
                .forEach(ExampleTranslator::printTranslation));
  }

  private static void printTranslation(Translation translation) {
    System.out.println(translation.getLanguageCode() + ": " + translation.getText());
  }
}
