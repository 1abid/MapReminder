package com.bus.green.mapreminder


import androidx.room.Room
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.bus.green.mapreminder.data.ReminderDatabase
import org.junit.After
import org.junit.Before


const val longitude = 23.721023
const val latitude = 90.393860

@SmallTest
class ReminderDAOTest {


    private lateinit var reminderDatabase: ReminderDatabase

    @Before
    fun setUp(){
        reminderDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, ReminderDatabase::class.java).build()
    }





    @After
    fun close(){
        reminderDatabase.close()
    }
}