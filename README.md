# Azure Translator App

This is a console application that leverages the Azure Translator API to translate user input text. The application
requests the Azure Translator subscription key and region, then enters a loop where it prompts for text to translate.
The text is translated into Portuguese, Spanish, French, and German, and the translations are displayed in the console.

## Prerequisites

- Java 21
- Maven

## Dependencies

- [azure-translator-for-java](https://github.com/brenoepics/at4j)

## Setting Up Azure Translator

Before running the application, you need to set up the Azure Translator service:

1. Go to the [Azure portal](https://portal.azure.com/).
2. Create a new resource for Translator Text.
3. After the resource is created, go to the resource page and copy the subscription key and region.

## Running the Application

1. Clone the repository: `git clone https://github.com/brenoepics/Azure-Translator-Example.git`
2. Navigate to the project directory: `cd Azure-Translator-Example`
3. Build the project: `mvn clean install`
4. Run the `TranslationConsoleApp` class.

## Usage

1. When prompted, enter your Azure Translator subscription key and region.
2. Enter the text you want to translate.
3. The translated text will be printed to the console in Portuguese, Spanish, French, and German.
4. To exit the application, type 'exit' (or equivalent in any of the supported languages) and press enter.

## Application Versions

1. `VirtualThreadsTranslator.java`: This version of the application uses Java's virtual threads (also known as
   lightweight threads or fibers) to handle the translation tasks. It translates a predefined list of phrases into
   Portuguese, Spanish, and French.

2. `KotlinConsoleApp.kt`: This version of the application is written in Kotlin. It prompts the user for text to
   translate and translates the input into Portuguese, Spanish, French, and German. It also handles profanity by marking
   it with asterisks.

3. `TranslationConsoleApp.java`: This version of the application is similar to the Kotlin version but is written in
   Java. It also includes a feature to exit the application by typing 'exit' in any of the supported languages.

4. `ExampleTranslator.java`: This version of the application is a simple example that translates the phrase "Hello
   World!" into Portuguese, Spanish, and French.

Remember, all these versions use the Azure Translator API for translation and require an Azure subscription key and
region to function. They are all console applications, meaning they run in the command line or terminal.

## Contributing

We welcome contributions! Please see the [CONTRIBUTING.md](CONTRIBUTING.md) file for details on how to contribute to
this project.

## Note

This is a simple example and does not handle errors or edge cases. It's intended for educational purposes and should not
be used in production without further enhancements.
