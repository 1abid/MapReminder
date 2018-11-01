package com.bus.green.mapreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ReminderEntity::class], version = 1)
public abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDAO

    companion object {
        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {

            val tempInstance = INSTANCE

            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "Reminder_database"
                ).build()
                INSTANCE = instance
                return instance
            }


        }

    }
}