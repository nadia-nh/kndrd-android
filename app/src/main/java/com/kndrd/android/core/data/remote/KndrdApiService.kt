package com.kndrd.android.core.data.remote

import retrofit2.http.GET

// Placeholder — endpoints will be added once the backend API is documented.
interface KndrdApiService {
    @GET("health")
    suspend fun healthCheck(): Map<String, String>
}
