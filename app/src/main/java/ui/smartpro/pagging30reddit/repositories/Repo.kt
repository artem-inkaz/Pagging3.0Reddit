/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ui.smartpro.pagging30reddit.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.smartpro.pagging30reddit.database.Database
import ui.smartpro.pagging30reddit.database.entities.Posts
import ui.smartpro.pagging30reddit.network.Api

class Repo: KoinComponent {

    // 1 создаем ссылку наApi, чтобы загрузить список сообщений из Reddit API
    private val api: Api by inject()

    //создаем экземпляр базы данных Room с помощью функции create(). Будем использовать его с RemoteMediator.
    private val database : Database by inject()

    @OptIn(ExperimentalPagingApi::class)
    fun fetchPosts(): Flow<PagingData<Posts>> {
        return Pager(
            PagingConfig(
                pageSize = 40,
                enablePlaceholders = true,
                // 1 Как часть конфигурации разбиения по страницам добавляем prefetchDistance в PagingConfig.
                // Этот параметр определяет, когда запускать загрузку следующих элементов в загруженном списке.
                prefetchDistance = 3),

            // 2  set RedditRemoteMediator.RedditRemoteMediator извлекает данные из сети и сохраняет их в базе данных.
            remoteMediator = RedditRemoteMediator(api, database),

            // 3 установливаем pagingSourceFactory.Dao для получения сообщений.
            // Теперь база данных служит единственным источником для отображаемых сообщений, независимо от того, есть ли подключение к сети.
            pagingSourceFactory = { database.postsDao().getPosts() }
        ).flow
    }
}