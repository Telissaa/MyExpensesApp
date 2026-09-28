package pl.wluczak.myexpenses.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// narazie podstawowy CRUD create read edit delete
@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)
    //list of all expenses not deleted
    @Query("SELECT * FROM expenses WHERE deletedAt IS NULL")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Int): Expense?

    @Query("SELECT * FROM expenses WHERE deletedAt IS NOT NULL")
    fun getDeletedExpenses(): Flow<List<Expense>>

    @Query("DELETE FROM expenses WHERE deletedAt IS NOT NULL AND deletedAt < :cleanupThreshold")
    suspend fun deleteOldSoftDeletedExpenses(cleanupThreshold: Long)

    //sum of monthly expenses
    @Query("SELECT SUM(amount) FROM expenses WHERE date LIKE :yearMonthPattern AND deletedAt IS NULL")
    fun getTotalSpentForMonth(yearMonthPattern: String): Flow<Double?>

    // lista unikalnych kategorii
    @Query("SELECT DISTINCT category FROM expenses WHERE category IS NOT NULL AND category != ''")
    fun getAllCategories(): Flow<List<String>>

    // lista podkategorii dla wybranej kategorii
    @Query("SELECT DISTINCT subcategory FROM expenses WHERE category = :category AND subcategory IS NOT NULL AND subcategory != ''")
    fun getSubcategoriesForCategory(category: String): Flow<List<String>>
}