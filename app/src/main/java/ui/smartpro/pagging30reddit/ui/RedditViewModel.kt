package ui.smartpro.pagging30reddit.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ui.smartpro.pagging30reddit.base.BaseViewModel
import ui.smartpro.pagging30reddit.database.entities.Posts
import ui.smartpro.pagging30reddit.repositories.Repo

class RedditViewModel(
    application: Application,
    private val redditRepo:Repo
) : BaseViewModel(application) {

    // 2
    //Вызов fetchPosts, который создали в Repo.
    // Используем вызов cachedIn для кэширования данных
    fun fetchPosts(): Flow<PagingData<Posts>> {
            return redditRepo.fetchPosts().cachedIn(viewModelScope)
    }

}