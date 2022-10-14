package com.example.inventory.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemRepository
import com.example.inventory.data.ItemRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : ViewModel() {

    private val repository: ItemRepository
    val allItems: LiveData<List<Item>>
    init {
        val itemDao = ItemRoomDatabase.getDatabase(application).itemDao()
        repository = ItemRepository(itemDao)
        allItems = repository.getAllItems()
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return repository.getItem(id).asLiveData()
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = createNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    private fun createNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    private fun getUpdatedItemEntry(itemId: Int, itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(id = itemId, itemName = itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemCount.toInt())
    }

    fun updateItem(itemId: Int, itemName: String, itemPrice: String, itemCount: String) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }
}

class InventoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            return InventoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}