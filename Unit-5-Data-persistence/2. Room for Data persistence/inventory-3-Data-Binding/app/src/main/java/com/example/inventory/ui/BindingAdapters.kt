package com.example.inventory.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat

@BindingAdapter("priceCurrencyText")
fun setPriceText(view: TextView, oldText: Double, newText: Double) {
    if (oldText == newText) {
        return
    }
    view.text = NumberFormat.getCurrencyInstance().format(newText)
}

@BindingAdapter("quantityText")
fun setQuantityPrice(view: TextView, oldText: Int, newText: Int) {
    if (newText == 0) {
        view.text = "0"
        return
    } else if (oldText == newText) {
        return
    }

    view.text = newText.toString()
}

@BindingAdapter("itemNameSpannable")
fun setItemNameSpannable(view: TextInputEditText, oldText: String?, newText: String?) {
    if (newText == oldText || (newText == null && oldText?.length == 0)) {
        return
    }
    if (!haveContentsChanged(newText, oldText)) {
        return // No content changes, so don't set anything.
    }
    view.setText(newText, TextView.BufferType.SPANNABLE)

}

@BindingAdapter("itemPriceSpannable")
fun setItemPriceSpannable(view: TextInputEditText, oldPrice: Double?, newPrice: Double?) {
    if (oldPrice == newPrice) {
        return
    }
    view.setText("%.2f".format(newPrice), TextView.BufferType.SPANNABLE)

}

@BindingAdapter("itemQuantitySpannable")
fun setItemQuantitySpannable(view: TextInputEditText, oldQuantity: Int?, newQuantity: Int?) {
    if (oldQuantity == newQuantity) {
        return
    }
    view.setText(newQuantity.toString(), TextView.BufferType.SPANNABLE)

}

private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {
    if (str1 == null != (str2 == null)) {
        return true
    } else if (str1 == null) {
        return false
    }
    val length = str1.length
    if (length != str2!!.length) {
        return true
    }
    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}

