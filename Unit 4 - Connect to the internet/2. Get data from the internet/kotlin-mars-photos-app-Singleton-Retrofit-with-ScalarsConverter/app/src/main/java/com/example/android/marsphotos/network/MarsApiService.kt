package com.example.android.marsphotos.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET


private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {

    // when getPhotos() is called, @GET("photos) will append "photos" to the BASE_URL
    // ---> BASE_URL/photos ---> and the method will return the result from the URL
    @GET("photos")
    suspend fun getPhotos(): String

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