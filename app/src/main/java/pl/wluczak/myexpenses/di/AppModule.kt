package pl.wluczak.myexpenses.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.wluczak.myexpenses.data.AppDatabase
import pl.wluczak.myexpenses.data.ExpenseDao
import pl.wluczak.myexpenses.data.ExpenseRepository
import pl.wluczak.myexpenses.data.ExpenseRepositoryImpl
import pl.wluczak.myexpenses.ui.addexpense.AddExpenseViewModel
import pl.wluczak.myexpenses.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModel

val AppModule = module {
    // 1. Definiujemy jak stworzyć instancję bazy danych
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "expenses_db",
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    // 2. Definiujemy jak stworzyć DAO
    single<ExpenseDao> { get<AppDatabase>().expenseDao() }

    // 3. Definiujemy Repozytorium
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }

    // 4. Definiujemy ViewModele
    viewModel { HomeViewModel(get()) }
    viewModel { AddExpenseViewModel(get()) }
}