package com.example.android.devbyteviewer.network

import com.example.android.devbyteviewer.database.VideoEntity
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Objects are responsible for:
 * - parsing responses from the server
 * - or formatting objects to send to the server.
 *
 * You should convert these to domain objects before using them.
 */

/**
 * NetworkVideoContainer holds a list of Videos.
 * This is to parse first level of our network result which looks like
 *   {
 *      "videos": [
 *              {
 *                  "title":"...",
 *                  "description":"...",
 *                  "url":"...",
 *                  "updated":"...",
 *                  "thumbnail":"..."
 *              },
 *
 *              {
 *                  ...
 *              },
 *              ...
 *      ]
 *   }
 */
@JsonClass(generateAdapter = true)
data class VideoDtoContainer(val videos: List<VideoDto>)

@JsonClass(generateAdapter = true)
data class VideoDto(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?)


// Convert Network results to database objects
fun VideoDtoContainer.asDatabaseModel(): List<VideoEntity> {
    return videos.map {
        VideoEntity(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}

// Convert Network results to model objects
// fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
//     return videos.map {
//         DevByteVideo(
//             title = it.title,
//             description = it.description,
//             url = it.url,
//             updated = it.updated,
//             thumbnail = it.thumbnail)
//     }
// }

