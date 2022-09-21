package com.example.busschedule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.database.schedule.ScheduleDao

/**
 * The Database class (class extending RoomDatabase) tells Room what to do with all of the @Entity and @Dao classes
 * While you may be wondering why Room can't just find all the entities and DAO objects for you, it's quite possible that
 * your app could have multiple databases, or any number of scenarios where the library can't assume the intent of you, the developer.
 * The AppDatabase class gives you complete control over your models, DAO classes, and any database setup you wish to perform.
 *
 * In this particular case, the AppDatabase class should:
 *      1. Specify which entities are defined in the database.                  ---> Schedule
 *      2. Provide access to a single instance of each DAO class.               ---> ScheduleDao
 *      3. Perform any additional setup, such as pre-populating the database.   ---> assets/database/bus_schedule.db
 */
@Database(entities = arrayOf(Schedule::class), version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/bus_schedule.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }

}