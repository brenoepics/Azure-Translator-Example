# Azure Translator Example

This is a simple console application that uses the Azure Translator API to translate text input by the user. The application asks for the Azure Translator subscription key and region, then enters a loop where it asks for text to translate. The text is translated into Portuguese, Spanish, French, and German, and the translations are printed to the console.

## Prerequisites

- Java 19
- Maven

## Dependencies

- [azure-translator-for-java](https://github.com/brenoepics/at4j)
- [okhttp](https://square.github.io/okhttp/)

## How to Run

1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn clean install` to build the project.
4. Run the `TranslationConsoleApp` class.

## Usage

1. When prompted, enter your Azure Translator subscription key and region.
2. Enter the text you want to translate.
3. The translated text will be printed to the console in Portuguese, Spanish, French, and German.
4. To exit the application, type 'exit'

## Note

This is a simple example and does not handle errors or edge cases.