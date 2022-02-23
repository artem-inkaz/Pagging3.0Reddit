package ui.smartpro.pagging30reddit.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import okio.IOException
import retrofit2.HttpException
import ui.smartpro.pagging30reddit.database.Database
import ui.smartpro.pagging30reddit.database.entities.Keys
import ui.smartpro.pagging30reddit.database.entities.Posts
import ui.smartpro.pagging30reddit.network.Api

@OptIn(ExperimentalPagingApi::class)
class RedditRemoteMediator(
    private val redditService: Api,
    private val redditDatabase: Database
) : RemoteMediator<Int, Posts>() {
    override suspend fun load(
        // 1 LoadType, это перечисление, представляющее тип загрузки. Он может иметь любое из следующих значений:
        //REFRESH указывает, что это новая выборка.
        //PREPEND указывает, что содержимое добавляется в начале PagingData.
        //APPEND указывает, что содержимое добавляется в конец PagingData.

        loadType: LoadType,
        // 2 Состояние пейджинга. Это принимает пару ключ-значение, где ключ имеет тип Int, а значение имеет тип Post.
        state: PagingState<Int, Posts>
    ): MediatorResult {
        return try {
            // 1 Вы извлекаете ключи Reddit из базы данных, когда LoadType ДОПОЛНЯЕТСЯ.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    getRedditKeys()
                }
            }

            // 2 Устанавливаем ключи after и before в fetchPosts.
            val response = redditService.fetchPosts(
                loadSize = state.config.pageSize,
                after = loadKey?.after,
                before = loadKey?.before
            )
            val listing = response.body()?.data
            val redditPosts = listing?.children?.map { it.data }
            if (redditPosts != null) {
            // 3 Создаем транзакцию базы данных, чтобы сохранить ключи и сообщения, которые извлекли, есть ли ответ, вернувший несколько сообщений
                redditDatabase.withTransaction {
                    redditDatabase.keysDao()
                        .saveRedditKeys(Keys(0, listing.after, listing.before))
                    redditDatabase.postsDao().savePosts(redditPosts)
                }
            }
            MediatorResult.Success(endOfPaginationReached = listing?.after == null)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    /*Это метод приостановки, который извлекает RedditKeys из базы данных. используем firstOrNull().
         Это вернет первые элементы в списке. Если в базе данных нет элементов, возвращается null.*/
    private suspend fun getRedditKeys(): Keys? {
        return redditDatabase.keysDao().getRedditKeys().firstOrNull()
    }

}