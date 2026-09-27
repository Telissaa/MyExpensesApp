# Plan wdrożenia kolorów kategorii (Opartych na bazie danych)

Ten dokument opisuje architektoniczne podejście do zmiany mechanizmu przypisywania kolorów kategoriom. Aktualnie kolory są generowane w locie (hash na podstawie nazwy kategorii). Docelowo chcemy, aby użytkownik miał nad nimi kontrolę (odczyt/zapis) za pomocą bazy danych Room.

## Etap 1: Warstwa Danych (Data Layer)

### 1. Nowa encja bazy danych (`Category`)
*   **Plik:** `shared/src/commonMain/kotlin/pl/wluczak/myexpenses/data/Category.kt` (lub w odpowiednim pakiecie `data`).
*   **Pola:**
    *   `id` (Int, PrimaryKey, autoGenerate)
    *   `name` (String) - nazwa kategorii (np. "Jedzenie")
    *   `colorHex` (String) - kolor w formacie HEX (np. "#FFD1DC")
    *   `isDefault` (Boolean) - flaga określająca, czy jest to kategoria systemowa (domyślna), czy stworzona przez usera.

### 2. Utworzenie CategoryDao
*   **Plik:** `app/src/main/java/pl/wluczak/myexpenses/data/CategoryDao.kt`
*   **Metody:**
    *   `getAllCategories(): Flow<List<Category>>` (pobieranie wszystkich, reaktywne)
    *   `insertCategory(category: Category)`
    *   `updateCategory(category: Category)`
    *   `deleteCategory(category: Category)`

### 3. Aktualizacja bazy danych (Room)
*   **Plik:** `app/src/main/java/pl/wluczak/myexpenses/data/AppDatabase.kt`
*   **Akcje:**
    *   Dodanie `Category::class` do tablicy `entities`.
    *   Zwiększenie `version` bazy danych.
    *   Dodanie abstrakcyjnej funkcji `abstract fun categoryDao(): CategoryDao`.

## Etap 2: Inicjalizacja Danych i Migracja

### 1. Domyślne dane na start (Prepopulate)
*   Zapewnienie, że przy pierwszym uruchomieniu aplikacji, nowa tabela zostanie wypełniona podstawowymi kategoriami (np. Jedzenie, Transport, Rachunki) wraz z ich przypisanymi pastelowymi kolorami.
*   Można to zrobić w module DI (Koin) przy tworzeniu instancji `RoomDatabase` za pomocą `RoomDatabase.Callback`.

### 2. Migracja relacyjna (Opcjonalnie, ale zalecane)
*   **Plik:** `shared/.../Expense.kt`
*   **Rozważenie zmiany:** Zamiast przetrzymywać `category: String` w tabeli wydatków, lepiej przechowywać `categoryId: Int`.
*   Jeśli zostaniemy przy Stringu, w zapytaniach SQL trzeba będzie robić połączenie (JOIN) po nazwie kategorii, by wyciągnąć jej kolor. Jeśli przejdziemy na ID, będzie to szybsze i bardziej odporne na błędy (np. zmianę nazwy kategorii).

## Etap 3: Warstwa Repozytorium (Domain Layer)

### 1. CategoryRepository
*   Utworzenie interfejsu i implementacji dla repozytorium (`CategoryRepository` / `CategoryRepositoryImpl`).
*   Wstrzyknięcie repozytorium do modułów Koina (Dependency Injection).

## Etap 4: Interfejs Użytkownika (UI Layer)

### 1. Zmiana w HistoryScreen (i innych)
*   Gdy aplikacja będzie pobierać listę wydatków, ViewModel (np. `HistoryViewModel`) będzie musiał dostarczać do widoku nie tylko samego wydatku, ale też informacji o przypisanym kolorze kategorii (poprzez złączenie danych w bazie (JOIN) lub pobranie listy kategorii do pamięci).
*   Usunięcie funkcji-zaślepki `getCategoryColor(category: String)` z widoku.
*   W komponencie `HistoryExpenseItem` kolor karty będzie pobierany bezpośrednio z nowego modelu danych (np. `expenseWithCategoryInfo.color`).

### 2. Ekran "Zarządzaj Kategoriami" (Ustawienia)
*   Stworzenie nowego ekranu w Jetpack Compose (np. `ManageCategoriesScreen`).
*   Ekran ten połączy się z `CategoryViewModel`.
*   Funkcjonalności: wyświetlenie listy kategorii, kliknięcie wywołujące modal (Color Picker) do zmiany koloru, zapis nowej wartości koloru do bazy (update).
