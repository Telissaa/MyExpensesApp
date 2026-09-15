# Plan Działania: Projekt Wieloletniej Aplikacji Finansowej (JetBrains Internship Track)

## Wybrany Stos Technologiczny
* **UI (Interfejs):** Jetpack Compose
* **Architektura:** MVVM (Model-View-ViewModel)
* **Wstrzykiwanie zależności (DI):** Koin (czysty Kotlin)
* **Baza danych (Lokalna):** Room (SQLite) - obsługa offline i ścieżki do paragonów
* **Chmura i Backend:** Firebase (Firestore dla danych tekstowych + Storage na zdjęcia paragonów)

---

## Faza 1: Konfiguracja Środowiska i Bibliotek w Android Studio
* **1.1. Repozytorium i Konfiguracja**
    * Uporządkowanie pliku `.gitignore`.
    * Inicjalizacja i powiązanie projektu z GitHubem.
* **1.2. Dodanie wybranych bibliotek (Dependencies)**
    * Konfiguracja Koin do wstrzykiwania zależności.
    * Konfiguracja Room do obsługi bazy danych.
    * Podpięcie pakietów SDK Firebase.
DONE
---

## Faza 2: Warstwa Danych i Architektura Lokalna
* **2.1. Modele Danych i Encje**
    * Stworzenie klas reprezentujących wydatki (`Expense`) oraz kategorie (`Category`).
* **2.2. Baza Room i Repozytorium**
    * Utworzenie interfejsów DAO i logiki zapisywania danych lokalnie na urządzeniu.
DONE
---

## Faza 3: Interfejs Użytkownika (Jetpack Compose)
* **3.1. Odwzorowanie przepływów z Figma Jam**
    * Stworzenie ekranu głównego (Dashboard z podsumowaniem wydatków).
    * Stworzenie widoku listy wydatków oraz formularza dodawania nowej pozycji.
* **3.2. Obsługa paragonów**
    * Dodanie funkcji aparatu/galerii i zapisu ścieżki do pliku w lokalnym cache.
WE'RE HERE
---

## Faza 4: Synchronizacja z Chmurą oraz Kotlin Multiplatform (KMP)
* **4.1. Integracja z Firebase**
    * Przesyłanie danych tekstowych do Cloud Firestore oraz zdjęć paragonów do Firebase Storage.
    * Obsługa synchronizacji danych między lokalną bazą a chmurą.
* **4.2. Obsługa konfliktów i wersjonowanie**
  * Dodanie isloading wszędzie gdzie dane są wyświetlane użytkownikowi.
* **4.3. KMP (Przygotowanie pod JetBrains)** - DONE
    * Wydzielenie czystej logiki biznesowej do wspólnego modułu Kotlin Multiplatform.

---

## Faza 5: Testy, Code Review i Utrzymanie
* **5.1. Testy jednostkowe i Code Review**
    * Analiza kodu pod kątem wydajności, czystości i zasad SOLID.
* **5.2. Wieloletnie utrzymanie**
    * Regularne aktualizacje bibliotek i dbałość o kompatybilność wsteczną.

## TO DO 
* softDelete
* restore ?
* permanentDelete
* getDeletedExpenses
* konfilikty przy updatowaniu bazy danych