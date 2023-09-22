package com.example.diagnal_programming_test.activity

import android.R
import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnal_programming_test.data.adapter.DiagnalRecyclerAdapter
import com.example.diagnal_programming_test.data.model.Content
import com.example.diagnal_programming_test.data.viewmodel.DiagnalViewModel
import com.example.diagnal_programming_test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.name
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<DiagnalViewModel>()

    @Inject
    lateinit var adapter: DiagnalRecyclerAdapter

    private var list = ArrayList<Content>()

    private var isLoading = false
    private var currentPage = 1
    private val pageSize = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

//        val typeface = Typeface.createFromAsset(assets, "font/tit.ttf")
//        binding.heading.typeface = typeface

        setRecyclerAdapter(this)
        setApiObserver(this)
        viewModel.getApiData()

        binding.back.visibility = View.VISIBLE
        binding.heading.visibility = View.VISIBLE
        binding.search.visibility = View.VISIBLE

        binding.searchBar.visibility = View.GONE
        binding.searchCancel.visibility = View.GONE

        binding.search.setOnClickListener {
            binding.back.visibility = View.GONE
            binding.heading.visibility = View.GONE
            binding.search.visibility = View.GONE

            binding.searchBar.visibility = View.VISIBLE
            binding.searchCancel.visibility = View.VISIBLE
        }

        binding.searchCancel.setOnClickListener {
            binding.back.visibility = View.VISIBLE
            binding.heading.visibility = View.VISIBLE
            binding.search.visibility = View.VISIBLE

            binding.searchBar.visibility = View.GONE
            binding.searchCancel.visibility = View.GONE

            binding.searchBar.setText("")
            viewModel.firstJsonLoad()

        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                Log.d(TAG, "Change: ${s.toString()}")
                if (binding.searchBar.text.toString().length >= 3)
                    viewModel.setSearchKeyword(binding.searchBar.text.toString())
                else
                    viewModel.firstJsonLoad()
            }
        })
    }

    private fun setRecyclerAdapter(mainActivity: MainActivity) {
        binding.comedyRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    currentPage++
                    if (currentPage <= 3) {
                        viewModel.loadMoreData(currentPage)
                    }
                }
            }
        })
        if (resources.configuration.orientation == 1)
            binding.comedyRecycler.layoutManager = GridLayoutManager(mainActivity, 3)
        else
            binding.comedyRecycler.layoutManager = GridLayoutManager(mainActivity, 7)
        binding.comedyRecycler.adapter = adapter
    }

    private fun setApiObserver(mainActivity: MainActivity) {
        viewModel.comedy.observe(
            mainActivity
        ) { value ->
            if (value != null) {
                Log.d(TAG, "MainActivity Api Data: $value")
                list = value as ArrayList<Content>
                adapter.submitList(value)
            }
        }
    }
}