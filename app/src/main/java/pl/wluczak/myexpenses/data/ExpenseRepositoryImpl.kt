package pl.wluczak.myexpenses.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAllExpenses()

    override fun getTotalSpentForMonth(yearMonthPattern: String): Flow<Double?> =
        expenseDao.getTotalSpentForMonth(yearMonthPattern)

    override fun getAllCategories(): Flow<List<String>> =
        expenseDao.getAllCategories()

    override fun getSubcategoriesForCategory(category: String): Flow<List<String>> =
        expenseDao.getSubcategoriesForCategory(category)

    override suspend fun insertExpense(expense: Expense) =
        expenseDao.insertExpense(expense)

    override suspend fun updateExpense(expense: Expense) =
        expenseDao.updateExpense(expense)

    override suspend fun deleteExpense(expense: Expense) =
        expenseDao.deleteExpense(expense)
}
