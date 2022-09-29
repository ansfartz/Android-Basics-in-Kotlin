package com.example.inventory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(private val itemDao: ItemDao) {

    suspend fun insertItem(item: Item) =
        withContext(Dispatchers.IO) {
            itemDao.insert(item)
        }

    fun getAllItems(): LiveData<List<Item>> =
        itemDao.getItems().asLiveData()

    suspend fun update(item: Item) =
        itemDao.update(item)

    suspend fun delete(item: Item) =
        itemDao.delete(item)

    fun getItem(id: Int) = itemDao.getItem(id)

}