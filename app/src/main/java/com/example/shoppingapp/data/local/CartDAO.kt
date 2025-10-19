package com.example.shoppingapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CartDAO {
    @Query("SELECT * FROM cart WHERE user_id = :userId")
    suspend fun getAll(userId:String): List<Cart>

    @Query("SELECT * FROM cart WHERE user_id = :userId AND product_id = :productId LIMIT 1")
    suspend fun getId(userId:String , productId: String) : Cart?

    /*
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User
    */

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(cartObj: Cart)

    @Query("DELETE FROM cart WHERE product_id = :productId")
    suspend fun delete(productId:String)



}

