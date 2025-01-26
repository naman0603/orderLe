orderLe

orderLe is a mockup application of a jewelry store, serving as a clone of the project “orderLe” by the company “200 ok Solutions”. This project was initiated on 24/08/23.

Description

The orderLe application is designed to simulate the functionalities of a jewelry store’s ordering system. It provides a user-friendly interface for browsing products, managing orders, and processing payments. This mockup serves as a foundation for developers to build upon and customize according to specific business requirements.

Interesting Techniques

This project employs several noteworthy techniques:
	•	Model-View-ViewModel (MVVM) Architecture: Separates the development of the graphical user interface from the business logic, enhancing code maintainability and testability.
	•	Data Binding: Facilitates automatic synchronization between the UI components and the underlying data models, reducing boilerplate code.
	•	LiveData: Ensures that the UI reflects the latest data by observing data changes in a lifecycle-aware manner.

Libraries and Technologies

In addition to standard Android development tools, orderLe integrates the following libraries:
	•	Retrofit: A type-safe HTTP client for Android and Java, used for network operations.
	•	Glide: An image loading and caching library for Android, utilized for efficient image handling.
	•	Room: A persistence library that provides an abstraction layer over SQLite, streamlining database access.

Project Structure

The repository is organized as follows:

<pre>
.
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── orderle/
│   │   │   │               ├── ui/             # UI components and activities
│   │   │   │               ├── data/           # Data models and repositories
│   │   │   │               ├── viewmodel/      # ViewModel classes
│   │   │   │               └── utils/          # Utility classes
│   │   │   ├── res/                            # Resource files (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml             # Application manifest file
│   ├── build.gradle                            # Module-level Gradle build file
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar                  # Gradle Wrapper JAR
│       └── gradle-wrapper.properties           # Gradle Wrapper properties
├── .gitignore                                  # Git ignore file
├── build.gradle                                # Project-level Gradle build file
├── gradle.properties                           # Gradle properties
├── gradlew                                     # Unix Gradle Wrapper script
├── gradlew.bat                                 # Windows Gradle Wrapper script
└── settings.gradle                             # Settings for Gradle build
</pre>


Highlights in the Structure
	•	app/src/main/java/com/example/orderle/ui/: Contains Activity and Fragment classes that define the user interface.
	•	app/src/main/java/com/example/orderle/data/: Includes data models and repository classes responsible for data handling and business logic.
	•	app/src/main/java/com/example/orderle/viewmodel/: Houses ViewModel classes that manage UI-related data in a lifecycle-conscious way.
	•	app/src/main/java/com/example/orderle/utils/: Utility classes and helpers used across the application.
