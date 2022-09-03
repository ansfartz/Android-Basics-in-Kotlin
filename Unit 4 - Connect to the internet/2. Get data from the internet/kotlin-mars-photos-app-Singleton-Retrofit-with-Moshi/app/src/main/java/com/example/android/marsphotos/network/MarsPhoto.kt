package com.example.android.marsphotos.network

import com.squareup.moshi.Json

/**
 * This data class defines a Mars photo which includes an ID, and the image URL.
 * The property names of this data class are used by Moshi to match the names of values in JSON.
 *
 *  [    {
 *           "id":"424906",
 *           "img_src":"http://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000ML0044631300305227E03_DXXX.jpg"
 *       },
 *       {
 *           ...
 *           ...
 *       },
 *       ...
 *  ]
 *
 *
 *  To use variable names in your data class that differ from the key names in the JSON response, use the @Json annotation.
 *  In this example, the name of the variable in the data class is imgSrcUrl. The variable can be mapped to the JSON attribute img_src using @Json(name = "img_src")
 *
 *  You can have the variable names the same as the JSON keys. But it is recommended to keep the Kotlin naming convention, which is camel case.
 *  If you don't use @Json(name = "...), nor do you name the variable the same as the JSON key, you will get a  Exception
 */

data class MarsPhoto(
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String)

