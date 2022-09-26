package com.example.busschedule.database.schedule

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO's are the main classes where you define your database interactions. They can include a variety of query methods.
 * The class marked with @Dao should either be an interface or an abstract class. At compile time, Room will generate an implementation of this class when it is referenced by a Database.
 * An abstract @Dao class can optionally have a constructor that takes a Database as its only parameter.
 * It is recommended to have multiple Dao classes in your codebase depending on the tables they touch.
 *
 *  TL;DR:
 *      - Provides access to read/write operations on the schedule table, by mapping a method to a SQL query.
 *      - Generally used by ViewModels to format the query results for use in the UI.
 *      - Working with the database is a time-consuming I/O operation, so this needs to be done on a background thread.
 *
 * Note: When using complex data types, such as java.util.Date, you have to also supply type converters.
 *       To keep this example basic, no data types that require type converters are used.
 *       See the documentation
 *       at https://developer.android.com/training/data-storage/room/referencing-data
 */
@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule ORDER BY arrival_time ASC")
    fun getAll(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
    fun getByStopName(stopName: String): Flow<List<Schedule>>

}