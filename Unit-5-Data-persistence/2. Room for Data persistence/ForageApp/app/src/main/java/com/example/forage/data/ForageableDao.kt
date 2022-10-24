/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.forage.model.Forageable
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for database interaction.
 */
@Dao
interface ForageableDao {

    // 1. method to retrieve all Forageables from the database
    // 2. method to retrieve a Forageable from the database by id
    // 3. method to insert a Forageable into the database (use OnConflictStrategy.REPLACE)
    // 4. method to update a Forageable that is already in the database
    // 5. method to delete a Forageable from the database
    //
    // The database operations can take a long time to execute, so they should run on a separate thread. Make the Insert / Update / Delete functions suspend functions, so that they can be called from a coroutine.
    //
    // Using Flow or LiveData as return type will ensure you get notified whenever the data in the database changes.
    // It is recommended to use Flow in the persistence layer. The Room keeps this Flow updated for you,
    // which means you only need to explicitly get the data once.
    // Because of the Flow return type, Room also runs the query on the background thread.  You don't need to explicitly make it a suspend function and call inside a coroutine scope.

    @Query("select * from forageable_table")
    fun getForageables(): Flow<List<Forageable>>

    @Query("select * from forageable_table where id=:id")
    fun getForageable(id: Long): Flow<Forageable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forageable: Forageable): Unit

    @Update
    fun update(forageable: Forageable): Unit

    @Delete
    fun delete(forageable: Forageable): Unit
}
