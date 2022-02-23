package ui.smartpro.pagging30reddit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.smartpro.pagging30reddit.databinding.ActivityMainBinding
import ui.smartpro.pagging30reddit.ui.*

class MainActivity : AppCompatActivity() {

    private val vm by viewModel<RedditViewModel>()
    private lateinit var postRv: RecyclerView
    private lateinit var redditAdapter: RedditAdapter
    private val binding: ActivityMainBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        fetchPosts()
    }

    private fun setupViews() {
        redditAdapter = RedditAdapter()
        postRv = binding.postRv
        postRv.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        postRv.adapter = redditAdapter
        postRv.adapter = redditAdapter.withLoadStateHeaderAndFooter(
            header = RedditLoadingAdapter { redditAdapter.retry() },
            footer = RedditLoadingAdapter { redditAdapter.retry() }
        )
    }

    private fun fetchPosts() {
        lifecycleScope.launch {
            vm.fetchPosts().collectLatest { pagingData ->
                redditAdapter.submitData(pagingData)
            }
        }
    }
}