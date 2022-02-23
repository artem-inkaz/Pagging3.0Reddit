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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ui.smartpro.pagging30reddit.database.entities.Posts
import ui.smartpro.pagging30reddit.network.Api
import java.io.IOException

class RedditPagingSource(private val redditApiService: Api) :
// 1 & 2
    PagingSource<String, Posts>() {

    override val keyReuseSupported: Boolean = true


    override suspend fun load(params: LoadParams<String>): LoadResult<String, Posts> {
        return try {
            // 1.  Получаем список сообщений из Reddit API, передав loadSize в качестве параметра через LoadParams.
            val response = redditApiService.fetchPosts(loadSize = params.loadSize)

            // 2 Получаем список сообщений из response
            val listing = response.body()?.data
            val redditPosts = listing?.children?.map { it.data }

            // 3 Создаем экземпляр LoadResult.Page, указав все аргументы.
            LoadResult.Page(

                // 4 Когда вызов API не вернул ни одного RedditPosts, вы передаете пустой список.
                redditPosts ?: listOf(),

                // 5 Передайте ключи «до» и «после», которые вы получили в теле ответа от Reddit API.
                listing?.before.toString(),
                listing?.after
            )

        } catch (exception: IOException) { // 6  два блока catch обрабатывают исключения и возвращают LoadResult.Error.

            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<String, Posts>): String? {
        TODO("Not yet implemented")
    }
}