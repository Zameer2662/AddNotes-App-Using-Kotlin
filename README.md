# Add Notes App (Kotlin)

A simple and lightweight Android Notes app built using Kotlin.

## Features
- Add, edit, and delete notes
- Clean and minimal user interface
- Local data storage using Room (if implemented)

Important Note:
This project was built using a specific version of Gradle and Android Gradle Plugin.
If you face Gradle sync issues or version compatibility errors, please follow these steps:

Open the project in your Android Studio.

When prompted, choose "Update Gradle Plugin" or "Update Gradle Wrapper".

Alternatively, manually adjust the following files:

gradle/wrapper/gradle-wrapper.properties → update distributionUrl to match your local Gradle version.

build.gradle (Project level) → update classpath 'com.android.tools.build:gradle:X.X.X' based on your Android Studio version.
