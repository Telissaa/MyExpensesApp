package pl.wluczak.myexpenses.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "expenses")
data class Expense (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val amount: Double,
    val date: Long,
    val category: String,
    val subcategory: String = "",         //optional
    val productPhotoUrl: String? = null,  //optional
    val receiptPhotoUrl: String? = null,  //optional
    val deletedAt: Long? = null           // soft delete
    )