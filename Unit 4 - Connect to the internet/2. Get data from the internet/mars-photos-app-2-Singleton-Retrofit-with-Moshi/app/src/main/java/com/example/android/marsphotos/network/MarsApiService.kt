package com.example.android.marsphotos.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET


private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getPhotos] method
 */
interface MarsApiService {

    // when getPhotos() is called, @GET("photos) will append "photos" to the BASE_URL (resulting in BASE_URL/photos) and the method will return the result from the URL
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>

}

// In kotlin, 'object' declarations are used to declare singleton objects.
// Singleton pattern ensures that one, and only one, instance of an object is created, has one global point of access to that object.
//
// Object declaration's initialization is thread-safe and done at first access.
// To refer to the object, use it's name directly:      MarsApi.someMethod(...)
object MarsApi {

    /**
     *  val retrofitService: MarsApiService = retrofit.create(MarsApiService::class.java)
     *  ^ Instead of the above code, make the initialization lazy, to make sure it is initialized at its first usage
     *
     *  Remember "lazy instantiation" is when object creation is purposely delayed until you actually
     *  need that object to avoid unnecessary computation or use of other computing resources
     */
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }

}