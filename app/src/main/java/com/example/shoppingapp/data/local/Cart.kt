package com.example.shoppingapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "product_id") val productId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "quantitiy") val quantity:Int
    )

