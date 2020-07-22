package com.intern.internproject.application_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.CLLoginResponseUser
import com.intern.internproject.respository.model.CLUSerEntity

@Dao
interface CLUserDAO {

    /** insert the user details in the database
     * */
    @Insert(onConflict = REPLACE)
    fun saveUser(user: CLLoginResponseUser)

    /** getting the user details
     * */
    @Query("select * from CLLoginResponseUser")
    fun readUser(): List<CLLoginResponseUser>

    @Insert(onConflict = REPLACE)
    fun saveUsers(users:List<CLLoginResponseUser>)

    /** deleting the database
     **/
    @Query("DELETE FROM CLLoginResponseUser")
    fun deleteDataBase()


}