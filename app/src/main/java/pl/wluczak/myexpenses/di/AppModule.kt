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
import pl.wluczak.myexpenses.ui.history.HistoryViewModel
import org.koin.core.module.dsl.viewModel

val AppModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "expenses_db",
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    single<ExpenseDao> { get<AppDatabase>().expenseDao() }

    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { AddExpenseViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}