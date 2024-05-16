package io.github.brenoepics;

import static io.github.brenoepics.TranslationConsoleApp.getAzureSubscription;

import io.github.brenoepics.at4j.AzureApi;
import io.github.brenoepics.at4j.AzureApiBuilder;
import io.github.brenoepics.at4j.data.Translation;
import io.github.brenoepics.at4j.data.request.TranslateParams;
import io.github.brenoepics.at4j.data.response.TranslationResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

public class VirtualThreadsTranslator {
  public static void main(String[] args) {
    TranslationConsoleApp.AzureSubscription keys = getAzureSubscription();
    AzureApi api = new AzureApiBuilder()
                    .setKey(keys.subscriptionKey())
                    .region(keys.subscriptionRegion())
                    .executorService(Executors.newVirtualThreadPerTaskExecutor()).build();
    TranslateParams params =
        new TranslateParams(getToTranslate(), List.of("pt", "es", "fr")).setSourceLanguage("en");
    Optional<TranslationResponse> translationResult = api.translate(params).join();

    translationResult.ifPresent(
        response ->
            response.getResultList().stream()
                .flatMap(result -> result.getTranslations().stream())
                .forEach(VirtualThreadsTranslator::printTranslation));
  }

  private static void printTranslation(Translation translation) {
    System.out.println(translation.getLanguageCode() + ": " + translation.getText());
  }

  private static List<String> getToTranslate() {
    List<String> toTranslate = new ArrayList<>();

    toTranslate.add("Hello World!");
    toTranslate.add("How are you?");
    toTranslate.add("Goodbye!");
    toTranslate.add("I'm fine, thank you!");
    toTranslate.add("How can I help you?");
    toTranslate.add("How much is 1 + 1?");
    toTranslate.add("What is the capital of Brazil?");
    toTranslate.add("What is the weather like today?");
    toTranslate.add("What time is it?");
    toTranslate.add("What is the meaning of life?");
    toTranslate.add("What is the airspeed velocity of an unladen swallow?");
    toTranslate.add(
        "What is the answer to the ultimate question of life, the universe, and everything?");
    return toTranslate;
  }
}
