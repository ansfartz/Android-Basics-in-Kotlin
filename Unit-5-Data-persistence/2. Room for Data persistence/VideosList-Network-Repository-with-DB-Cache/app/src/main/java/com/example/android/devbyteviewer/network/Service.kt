package com.example.android.devbyteviewer.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A Retrofit service to fetch the data. This is where the APIs are defined
 */
interface DevbyteService {
    @GET("devbytes")
    suspend fun getPlaylist(): VideoDtoContainer
}

/**
 * Main entry point for network access.
 * Use 'object declaration' to make it Singleton
 */
object DevByteNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val devbytes: DevbyteService = retrofit.create(DevbyteService::class.java)

}


