package com.example.shoppingapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavDAO {
    @Query("SELECT * FROM fav WHERE user_id = :userId")
    suspend fun getAll(userId:String): List<Fav>

    @Query("SELECT * FROM fav WHERE user_id = :userId AND product_id = :productId LIMIT 1")
    suspend fun getId(userId:String , productId: String): Fav?

    /*
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User
    */

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(fav: Fav)

    @Query("DELETE FROM fav WHERE product_id = :productID")
    suspend fun delete(productID: String)



}