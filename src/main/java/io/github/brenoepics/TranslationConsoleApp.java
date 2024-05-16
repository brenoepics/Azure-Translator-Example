package io.github.brenoepics;

import io.github.brenoepics.at4j.AzureApi;
import io.github.brenoepics.at4j.AzureApiBuilder;
import io.github.brenoepics.at4j.data.Translation;
import io.github.brenoepics.at4j.data.request.TranslateParams;
import io.github.brenoepics.at4j.data.request.optional.ProfanityAction;
import io.github.brenoepics.at4j.data.request.optional.ProfanityMarker;
import io.github.brenoepics.at4j.data.response.TranslationResponse;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TranslationConsoleApp {

  private static AzureApi azureApi;
  private static final List<Translation> exitTranslations = new ArrayList<>();

  public static void main(String[] args) {
    AzureSubscription keys = getAzureSubscription();

    // Initialize Azure API
    azureApi = initializeAzureApi(keys.subscriptionKey(), keys.subscriptionRegion());
    exitTranslations.add(new Translation("en", "exit"));
    translate("exit")
        .ifPresent(
            response -> exitTranslations.addAll(response.getResultList().getFirst().getTranslations()));

    // Enter translation loop
    while (true) {
      System.out.print("Enter text to translate (or type 'exit' to end): ");
      String inputText = keys.scanner().nextLine();

      if (exitTranslations.stream()
          .anyMatch(translation -> translation.getText().equalsIgnoreCase(inputText))) {
        System.out.println("Exiting translation app. Goodbye!");
        azureApi.getThreadPool().getExecutorService().shutdown();
        break;
      }

      // Translate input text
      Optional<TranslationResponse> translate = translate(inputText);

      if (translate.isPresent()) {
        translate
            .get()
            .getResultList()
            .getFirst()
            .getTranslations()
            .forEach(TranslationConsoleApp::printTranslation);
      } else {
        System.out.println("Translation failed.");
      }
    }

    keys.scanner().close();
  }

  static @NotNull AzureSubscription getAzureSubscription() {
    Scanner scanner = new Scanner(System.in);

    // Ask for a subscription key
    System.out.print("Enter your Azure Translator subscription key: ");
    String subscriptionKey = scanner.nextLine();

    // Ask for a subscription region
    System.out.print("Enter your Azure Translator subscription region: ");
    String subscriptionRegion = scanner.nextLine();
    AzureSubscription keys = new AzureSubscription(scanner, subscriptionKey, subscriptionRegion);
    return keys;
  }

  record AzureSubscription(Scanner scanner, String subscriptionKey, String subscriptionRegion) {
  }

  private static AzureApi initializeAzureApi(String subscriptionKey, String subscriptionRegion) {
    AzureApiBuilder builder =
        new AzureApiBuilder().setKey(subscriptionKey).region(subscriptionRegion);
    return builder.build();
  }

  private static Optional<TranslationResponse> translate(String inputText) {
    List<String> targetLanguages = Arrays.asList("pt", "es", "fr", "de");
    // Translate input text with profanity handling
    TranslateParams params =
        new TranslateParams(inputText, targetLanguages)
            .setProfanityAction(ProfanityAction.MARKED)
            .setProfanityMarker(ProfanityMarker.ASTERISK);

    CompletableFuture<Optional<TranslationResponse>> translationFuture = azureApi.translate(params);
    return translationFuture.join();
  }

  private static void printTranslation(Translation translation) {
    System.out.println(
        "{" + translation.getLanguageCode() + "] Translated Text: " + translation.getText());
  }
}
