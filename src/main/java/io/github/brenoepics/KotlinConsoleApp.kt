package io.github.brenoepics

import io.github.brenoepics.at4j.AzureApi
import io.github.brenoepics.at4j.AzureApiBuilder
import io.github.brenoepics.at4j.data.Translation
import io.github.brenoepics.at4j.data.request.TranslateParams
import io.github.brenoepics.at4j.data.request.optional.ProfanityAction
import io.github.brenoepics.at4j.data.request.optional.ProfanityMarker
import io.github.brenoepics.at4j.data.response.TranslationResponse
import java.util.*
import java.util.function.Consumer

object KotlinConsoleApp {
    private var azureApi: AzureApi? = null
    private val exitTranslations: MutableList<Translation> = ArrayList()

    @JvmStatic
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)

        // Ask for a subscription key
        print("Enter your Azure Translator subscription key: ")
        val subscriptionKey = scanner.nextLine()

        // Ask for a subscription region
        print("Enter your Azure Translator subscription region: ")
        val subscriptionRegion = scanner.nextLine()

        // Initialize Azure API
        azureApi = initializeAzureApi(subscriptionKey, subscriptionRegion)
        exitTranslations.add(Translation("en", "exit"))
        translate("exit").ifPresent { response: TranslationResponse -> exitTranslations.addAll(response.resultList.first().translations) }

        // Enter translation loop
        while (true) {
            print("Enter text to translate (or type 'exit' to end): ")
            val inputText = scanner.nextLine()

            if (exitTranslations.stream()
                    .anyMatch { translation: Translation -> translation.text.equals(inputText, ignoreCase = true) }
            ) {
                println("Exiting translation app. Goodbye!")
                azureApi!!.threadPool.executorService.shutdown()
                break
            }

            // Translate input text
            val translate = translate(inputText)

            if (translate.isPresent) {
                translate.get().resultList.first().translations.forEach(Consumer { obj: Translation? ->
                        if (obj != null) {
                            printTranslation(obj)
                        }
                    })
            } else {
                println("Translation failed.")
            }
        }

        scanner.close()
    }

    private fun initializeAzureApi(subscriptionKey: String, subscriptionRegion: String): AzureApi {
        val builder = AzureApiBuilder().setKey(subscriptionKey).region(subscriptionRegion)
        return builder.build()
    }

    private fun translate(inputText: String): Optional<TranslationResponse> {
        val targetLanguages: List<String> = mutableListOf("pt", "es", "fr", "de")
        // Translate input text with profanity handling
        val params = TranslateParams(inputText, targetLanguages).setProfanityAction(ProfanityAction.MARKED)
            .setProfanityMarker(ProfanityMarker.ASTERISK)

        val translationFuture = azureApi!!.translate(params)
        return translationFuture.join()
    }

    private fun printTranslation(translation: Translation) {
        println(
            "{" + translation.languageCode + "] Translated Text: " + translation.text
        )
    }
}
