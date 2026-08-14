package pl.wluczak.myexpenses.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Expense::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
}