package ui.smartpro.pagging30reddit.di

import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.smartpro.pagging30reddit.database.Database
import ui.smartpro.pagging30reddit.network.Api
import ui.smartpro.pagging30reddit.network.ApiKeyInterceptor

val appModule = module {

    // Room : CatDatabase
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "reddit_database"
        ).build()
    }

    // Retrofit :
    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
            .create(Api::class.java)
    }

}
