## MyExpenses

**MyExpenses** is a modern Android mobile application designed for tracking expenses, managing home budgets, and analyzing them.

## Technologies and Tech Stack

The application is written in **Kotlin** and leverages the latest Google standards for Android:

*   **UI (User Interface):** Jetpack Compose, Material Design 3.
*   **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture (layered division: UI, ViewModel, Repository, DAO/Room).
*   **Dependency Injection (DI):** Koin (`koin-androidx-compose`).
*   **Database:** Room (local SQLite database with support for asynchronous `Flow` streams).
*   **Navigation:** Jetpack Navigation (`NavHost` with type-safe routes based on a `sealed class`).
*   **Asynchrony:** Kotlin Coroutines & Flow.

## Main Features

*   **Home Dashboard:** Dynamic summary of current month expenses, budget control, and current balance, updating in real-time thanks to the reactive database.
*   **Add Expense Form:** An intuitive screen allowing quick transaction entry (name, price, date, categories) with built-in error validation. Support for attaching product and receipt photos.
*   **Secure Navigation:** Centrally managed routing system (`Screen` sealed class) handling the home screen, history, statistics, and forms.

## Project Structure

Clean Architecture:

```text
pl.wluczak.myexpenses/
│
├── data/               # Room database (Entity, DAO, AppDatabase)
├── repository/         # Repository layer mediating between database and ViewModel
├── navigation/         # Route configuration and NavHost (sealed class Screen)
├── ui/                 # Presentation layer (Views and ViewModels)
│   ├── home/           # Home screen and its components
│   └── addexpense/     # Add expense form screen
└── utils/              # Helper formatting functions (e.g., currencies, dates)

## Future Development
* Deploy the app backend to Firebase to enable cross-device data synchronization.
* Develop an expanded desktop version of the application.
* Add family sharing – link user accounts together into a family group.
* Integrate AI for optical receipt recognition and automated expense entry.
* Integrate AI for household budget and expense analysis, as well as financial forecasting.
