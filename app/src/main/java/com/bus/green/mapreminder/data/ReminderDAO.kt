package com.bus.green.mapreminder.data

import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ReminderDAO {

    @Query("SELECT * FROM reminder ORDER BY id")
    fun getAllReminders() : List<ReminderEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReminder(reminder: ReminderEntity)

    @Delete
    fun removeReminder(reminder:ReminderEntity)

    @Query("DELETE FROM reminder")
    fun deleteAllReminder()
}