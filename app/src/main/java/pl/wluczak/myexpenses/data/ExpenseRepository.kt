package pl.wluczak.myexpenses.data

import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getTotalSpentForMonth(yearMonthPattern: String): Flow<Double?>
    fun getAllCategories(): Flow<List<String>>
    fun getSubcategoriesForCategory(category: String): Flow<List<String>>
    suspend fun insertExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
}
