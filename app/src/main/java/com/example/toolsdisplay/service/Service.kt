package com.example.toolsdisplay.service

import com.example.githubuser.network.ConnectivityInterceptor
import com.example.toolsdisplay.models.LoginRequest
import com.example.toolsdisplay.models.ToolsItemListResponse
import com.example.toolsdisplay.utilities.Constant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Service {

    @POST("integration/admin/token")
    suspend fun login(@Body loginRequest: LoginRequest): String

    @GET("products")
    suspend fun getList(@Query("searchCriteria[pageSize]") pageSize : Int?,
                        @Query("searchCriteria[sortOrders][0][direction]") sortOrder : String?,
                        @Query("searchCriteria[sortOrders][0][field]") field: String,
                        @Header("Authorization") accessToken: String): ToolsItemListResponse

    @GET("/products/{sku}")
    suspend fun getProductDetail(@Path("sku") sku: String, @Header("Authorization") accessToken: String) : ToolsItemListResponse.ToolsInfoItem


    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): Service {

            var interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            var okClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor) // Avoid crashing when there is no internet
                .addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build().create(Service::class.java)


        }
    }

}