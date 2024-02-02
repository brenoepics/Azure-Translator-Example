package io.github.brenoepics;

import io.github.brenoepics.at4j.AzureApi;
import io.github.brenoepics.at4j.AzureApiBuilder;
import io.github.brenoepics.at4j.data.request.TranslateParams;
import io.github.brenoepics.at4j.data.response.TranslationResponse;

import java.util.List;
import java.util.Optional;

public class ExampleTranslator {
  public static void main(String[] args) {
    // Insert your Azure key and region here
    String azureKey = "<Your Azure Subscription Key>";
    String azureRegion = "<Your Azure Subscription Region>";
    AzureApi api = new AzureApiBuilder().setKey(azureKey).region(azureRegion).build();

    // Set up translation parameters
    List<String> targetLanguages = List.of("pt", "es", "fr");
    TranslateParams params =
        new TranslateParams("Hello World!", targetLanguages).setSourceLanguage("en");

    // Translate the text
    Optional<TranslationResponse> translationResult = api.translate(params).join();

    // Print the translations
    translationResult.ifPresent(
        response -> {
          System.out.println("Translations:");

          // Create a single stream of translations from all results
          response.getResultList().stream()
              .flatMap(result -> result.getTranslations().stream())
              .forEach(
                  translation ->
                      System.out.println(
                          translation.getLanguageCode() + ": " + translation.getText()));
        });
  }
}
