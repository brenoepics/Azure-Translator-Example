package com.github.brenoepics;

import com.github.brenoepics.at4j.AzureApi;
import com.github.brenoepics.at4j.AzureApiBuilder;
import com.github.brenoepics.at4j.azure.BaseURL;
import com.github.brenoepics.at4j.data.Translation;
import com.github.brenoepics.at4j.data.request.TranslateParams;
import com.github.brenoepics.at4j.data.request.optional.ProfanityAction;
import com.github.brenoepics.at4j.data.request.optional.ProfanityMarker;
import com.github.brenoepics.at4j.data.response.TranslationResponse;
import okhttp3.OkHttpClient;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class TranslationConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for a subscription key
        System.out.print("Enter your Azure Translator subscription key: ");
        String subscriptionKey = scanner.nextLine();

        // Ask for a subscription region
        System.out.print("Enter your Azure Translator subscription region: ");
        String subscriptionRegion = scanner.nextLine();

        // Initialize Azure API
        AzureApi azureApi = initializeAzureApi(subscriptionKey, subscriptionRegion);

        // Enter translation loop
        while (true) {
            System.out.print("Enter text to translate (or type 'exit' to end): ");
            String inputText = scanner.nextLine();

            if ("exit".equalsIgnoreCase(inputText)) {
                System.out.println("Exiting translation loop. Goodbye!");
                azureApi.getThreadPool().getExecutorService().shutdown();
                break;
            }

            // Translate input text
            translateAndPrint(azureApi, inputText);
        }

        scanner.close();
    }

    private static AzureApi initializeAzureApi(String subscriptionKey, String subscriptionRegion) {
        AzureApiBuilder builder = new AzureApiBuilder();
        builder.setBaseURL(BaseURL.GLOBAL);
        builder.setSubscriptionKey(subscriptionKey);
        builder.setSubscriptionRegion(subscriptionRegion)
                .setOkHttpClient(new OkHttpClient());

        return builder.build();
    }

    private static void translateAndPrint(AzureApi azureApi, String inputText) {
        // Translate input text with profanity handling
        TranslateParams params = new TranslateParams(inputText)
                .setTargetLanguages("pt", "es", "fr", "de")
                .setProfanityAction(ProfanityAction.MARKED)
                .setProfanityMarker(ProfanityMarker.ASTERISK);

        CompletableFuture<Optional<TranslationResponse>> translationFuture = azureApi.translate(params);
        Optional<TranslationResponse> translationOptional = translationFuture.join();

        translationOptional.ifPresent(translation -> translation.getTranslations().forEach(TranslationConsoleApp::printTranslation));
    }

    private static void printTranslation(Translation translation) {
        System.out.println("{" +translation.getLanguageCode() + "] Translated Text: " + translation.getText());
    }
}