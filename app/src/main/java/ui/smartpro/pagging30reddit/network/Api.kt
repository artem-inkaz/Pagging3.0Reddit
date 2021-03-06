package ui.smartpro.pagging30reddit.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ui.smartpro.pagging30reddit.network.response.ApiResponse

interface Api {

    @GET("/r/aww/hot.json")
    suspend fun fetchPosts(
        @Query("limit") loadSize: Int = 0,
        @Query("after") after: String? = null,
        @Query("before") before: Any? = null
    ): Response<ApiResponse>
}