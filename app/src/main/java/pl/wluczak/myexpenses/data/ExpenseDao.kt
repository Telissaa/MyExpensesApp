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
    //list of all expenses
    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<Expense>>
    //sum of monthly expenses
    @Query("SELECT SUM(amount) FROM expenses WHERE date LIKE :yearMonthPattern")
    fun getTotalSpentForMonth(yearMonthPattern: String): Flow<Double?>
}