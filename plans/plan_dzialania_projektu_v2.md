# Plan Działania: Projekt Wieloletniej Aplikacji Finansowej (JetBrains Internship Track)

## Cel Projektu
Stworzenie pełnoprawnego, modułowego systemu do zarządzania finansami osobistymi/biznesowymi w języku **Kotlin**, ze szczególnym naciskiem na technologie kluczowe w procesie rekrutacyjnym do **JetBrains** (Kotlin Multiplatform, narzędzia CLI oraz wtyczki do IDE).

---

## Faza 1: Fundamenty Językowe i Narzędzie CLI (Pure Kotlin)
Zanim zbudujemy interfejs graficzny, opanujemy czysty Kotlin oraz stworzymy pierwsze narzędzie deweloperskie przypominające miniprojekt dla JetBrains.

*   **1.1. Konfiguracja środowiska**
    *   Instalacja i konfiguracja najnowszego **Android Studio**.
    *   Inicjalizacja repozytorium Git oraz GitHub.
*   **1.2. Moduł Konsolowy (CLI Tool - "Kotlin Expense CLI")**
    *   Napisanie małej aplikacji konsolowej w czystym Kotlinie.
    *   Obsługa operacji na plikach tekstowych/CSV z wydatkami.
    *   Implementacja algorytmów analizujących dane i generujących raporty tekstowe w terminalu.
    *   *Cel naukowy:* Opanowanie kolekcji, lambd, obsługi błędów i struktur danych w Kotlinie.

---

## Faza 2: Architektura i Kotlin Multiplatform (KMP)
Przeniesienie logiki biznesowej do architektury niezależnej od platformy, co jest mocno promowane przez JetBrains.

*   **2.1. Wydzielenie wspólnego modułu (Shared Module)**
    *   Konfiguracja projektu w architekturze **Kotlin Multiplatform (KMP)**.
    *   Przeniesienie modeli danych (`Expense`, `Category`) oraz logiki obliczeniowej (sumy, statystyki, walidacje) do wspólnego kodu.
*   **2.2. Uruchomienie logiki na wielu platformach**
    *   Weryfikacja, że wspólny moduł biznesowy działa poprawnie zarówno w naszej aplikacji konsolowej (CLI), jak i w przyszłym środowisku mobilnym.

---

## Faza 3: Aplikacja Mobilna (Android + Jetpack Compose + Firebase)
Budowa pełnoprawnej aplikacji mobilnej korzystającej ze wspólnego modułu KMP.

*   **3.1. Interfejs Użytkownika (Jetpack Compose)**
    *   Odwzorowanie projektów z Figma Jam w postaci nowoczesnych ekranów (Dashboard, Lista Wydatków, Formularz).
*   **3.2. Warstwa Danych i Przechowywanie**
    *   Konfiguracja lokalnej bazy danych do działania offline.
    *   Obsługa zdjęć paragonów (zapis lokalny w cache + przesyłanie do **Firebase Storage**).
*   **3.3. Synchronizacja z Chmurą**
    *   Podpięcie **Firebase Firestore** do synchronizacji danych tekstowych w tle.

---

## Faza 4: Rozwój Portfolio (Wtyczka do IntelliJ IDEA)
Koronny argument w rekrutacji do JetBrains – stworzenie własnego pluginu do środowiska programistycznego.

*   **4.1. Konfiguracja projektu wtyczki (IntelliJ Platform Plugin SDK)**
    *   Stworzenie podstawowego projektu wtyczki dla IntelliJ IDEA w Kotlinie.
*   **4.2. Implementacja funkcji**
    *   Dodanie prostego okna dialogowego (Tool Window lub Action) w IDE, które pozwala dopisać wydatek lub podejrzeć comiesięczny raport bez wychodzenia z edytora kodu.

---

## Faza 5: Jakość, Testy i Utrzymanie Wieloletnie
*   **5.1. Testy Jednostkowe (Unit Tests)**
    *   Pokrycie logiki biznesowej w module KMP testami (przypadki brzegowe, walidacje kwot i dat).
*   **5.2. Code Review i Refaktoryzacja**
    *   Analiza kodu pod kątem wycieków pamięci, wydajności oraz zasad SOLID (wsparcie w mentorowaniu).
*   **5.3. Utrzymanie projektu**
    *   Regularne aktualizacje wersji bibliotek i SDK pod kątem kompatybilności wstecznej.
