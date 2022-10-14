package com.example.inventory.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.InventoryApplication
import com.example.inventory.viewmodel.InventoryViewModel
import com.example.inventory.viewmodel.InventoryViewModelFactory
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentAddItemBinding

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory((activity?.application as InventoryApplication))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId

        if (id > 0) {
            // Came here from ItemDetailFragment, we're editing an existing Item
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                binding.apply {
                    item = selectedItem
                    saveAction.setOnClickListener { updateItem() }
                }
            }
        } else {
            // Came here from ItemListFragment, we're creating a new Item
            binding.saveAction.setOnClickListener { addNewItem() }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemCount.text.toString()
        )
    }

    private fun showErrorMessage() {
        binding.apply {
            if (itemName.text!!.isBlank()) {
                itemNameLabel.isErrorEnabled = true
                itemNameLabel.error = "Please input an item name"
            } else {
                itemNameLabel.isErrorEnabled = false
                itemNameLabel.error = null
            }

            if (itemPrice.text!!.isBlank()) {
                itemPriceLabel.isErrorEnabled = true
                itemPriceLabel.error = "Please input an item name"
            } else {
                itemPriceLabel.isErrorEnabled = false
                itemPriceLabel.error = null
            }

            if (itemCount.text!!.isBlank()) {
                itemCountLabel.isErrorEnabled = true
                itemCountLabel.error = "Please input an item name"
            } else {
                itemCountLabel.isErrorEnabled = false
                itemCountLabel.error = null
            }
        }
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString(),
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)

        } else {
            showErrorMessage()
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemName.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemCount.text.toString(),
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)

        } else {
            showErrorMessage()
        }
    }

    private fun bind(item: Item) {
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
