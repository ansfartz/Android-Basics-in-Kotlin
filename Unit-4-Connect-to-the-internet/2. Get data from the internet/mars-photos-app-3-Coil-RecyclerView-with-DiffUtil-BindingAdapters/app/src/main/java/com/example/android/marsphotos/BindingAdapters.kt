package com.example.android.marsphotos

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.android.marsphotos.network.MarsPhoto
import com.example.android.marsphotos.overview.MarsApiStatus
import com.example.android.marsphotos.overview.PhotoGridAdapter


/**
 * https://developer.android.com/topic/libraries/data-binding/binding-adapters
 *
 * Binding Adapters are annotated methods used to create custom setters for custom properties of
 * your view.
 *
 * Usually when you set an attribute in your XML using the code: android:text="Sample Text", the
 * Android system automatically looks for a setter with the same name as the 'text' attribute,
 * which is set by the setText(String: text) method.
 * The setText(String: text) method is a setter method for some views provided by the Android Framework.
 * Similar behavior can be customized using the binding adapters; you can provide a custom attribute
 * and custom logic that will be called by the Data binding library.
 *
 * Example:
 *   Consider loading images off the UI thread (main thread), from the internet.
 *   First, choose a custom attribute to assign the image to an ImageView ---> 'myImageUrl'.
 *
 *       <ImageView
 *           android:layout_width="wrap_content"
 *           android:layout_height="wrap_content"
 *           app:myImageUrl="@{product.imageUrl}"/>
 *
 *   If you do not add any further code, the system would look for a setMyImageUrl(String) method
 *   in ImageView.java and not find it, throwing an error because this is a custom attribute not
 *   provided by the framework.
 *   You have to create a way to set the 'app:myImageUrl' attribute to the ImageView. You will use
 *   Binding adapters (annotated methods) to do this.
 *
 *       @BindingAdapter("myImageUrl")
 *       fun bindImage(imgView: ImageView, imgUrl: String?) {
 *           imgUrl?.let {
 *               // Load the image in the background using Coil.
 *               }
 *           }
 *       }
 *
 *  The @BindingAdapter() annotation takes the attribute name as its parameter.
 *  In the bindImage(...) method, the first method parameter is the type of the target View and the
 *  second is the value being set to the attribute.
 *
 *  Basically, for the above example, whenever the attribute 'myImageUrl' is being used, the method
 *  annotated with @BindingAdapter("myImageUrl") will be called
 *
 *  _______________________________________________________________________________________________
 *
 *  Data binding works even if no attribute exists with the given name. You can then create
 *  attributes for any setter by using data binding. For example, the support class DrawerLayout
 *  doesn't have any attributes, but plenty of setters. The following layout automatically uses the
 *  'setScrimColor(int)' and   'setDrawerListener(DrawerListener)'   methods as the setter for the
 *  'app:scrimColor'     and   'app:drawerListener'                  attributes, respectively:
 *
 *      <android.support.v4.widget.DrawerLayout
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      app:scrimColor="@{@color/scrim}"
 *      app:drawerListener="@{fragment.drawerListener}">
 *
 *
 */
@BindingAdapter("imageUrl")
fun loadURLtoImageView(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

/**
 * Using a BindingAdapter to set the RecyclerView data causes data binding to automatically observe
 * the LiveData for the list of MarsPhoto objects. Then the binding adapter is called automatically
 * when the MarsPhoto list changes.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsPhoto>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [MarsApiStatus] of the network request in an image view.
 * When the request is loading, it displays a loading_animation.
 * If the request has an error, it displays a broken image to reflect the connection error.
 * When the request is finished, it hides the image view.
 */
@BindingAdapter("marsApiStatus")
fun bindStatus(statusImageView: ImageView, status: MarsApiStatus?) {
    when (status) {
        MarsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        MarsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        MarsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}