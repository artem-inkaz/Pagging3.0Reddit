package ui.smartpro.pagging30reddit.utils

import androidx.recyclerview.widget.DiffUtil
import ui.smartpro.pagging30reddit.database.entities.Posts

class DiffUtilCallBack : DiffUtil.ItemCallback<Posts>() {
    override fun areItemsTheSame(oldItem: Posts, newItem: Posts): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
        return oldItem.key == newItem.key
                && oldItem.score == newItem.score
                && oldItem.commentCount == newItem.commentCount
    }
}