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

package ui.smartpro.pagging30reddit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ui.smartpro.pagging30reddit.databinding.ItemLoadingStateBinding

class RedditLoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RedditLoadingAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(private val vb: ItemLoadingStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(vb.root) {
        init {
            vb.btnRetry.setOnClickListener {
                retry()
            }
        }

        /**
         * 3 Функция с именем bindState, которая принимает LoadState в качестве аргумента
         * LoadState — это закрытый класс, который может иметь любое из следующих состояний:
         * NotLoading: Загрузка данных не происходит и ошибок нет
         * Загрузка: данные загружаются.
         * Ошибка: Получение данных завершается с ошибкой.
         */

        fun bindState(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                vb.tvErrorMessage.text = loadState.error.localizedMessage
            }
            vb.progressBar.isVisible = loadState is LoadState.Loading
            vb.tvErrorMessage.isVisible = loadState !is LoadState.Loading
            vb.btnRetry.isVisible = loadState !is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {

        val view = ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadingStateViewHolder(view, retry)
    }
}