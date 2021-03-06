package ui.smartpro.pagging30reddit.di

import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.smartpro.pagging30reddit.database.Database
import ui.smartpro.pagging30reddit.network.Api
import ui.smartpro.pagging30reddit.network.ApiKeyInterceptor
import ui.smartpro.pagging30reddit.repositories.Repo
import ui.smartpro.pagging30reddit.ui.RedditViewModel

val appModule = module {

    //vm
    viewModel { RedditViewModel(androidApplication(), get()) }

    single { Repo() }

    // Room : Database
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "reddit_database"
        ).build()
    }

    // Retrofit : Api
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
