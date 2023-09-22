package com.example.diagnal_programming_test.data.viewmodel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.diagnal_programming_test.DiagnalApp
import com.example.diagnal_programming_test.data.model.Comedy
import com.example.diagnal_programming_test.data.model.Content
import com.example.diagnal_programming_test.data.repo.DiagnalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiagnalViewModel @Inject constructor(
    private val repository: DiagnalRepository,
    private val diagnalApp: DiagnalApp
) : AndroidViewModel(diagnalApp) {
    private val TAG: String = DiagnalViewModel::class.java.name

    var comedy = MutableLiveData<List<Content>?>()

    init {
        repository.initPreDataLoading()
    }

    fun getApiData() {
        val apiData = repository.getApiData()
        Log.d(TAG, "Api Data: $apiData")
        comedy.value = apiData
    }

    fun setSearchKeyword(words: String) {
        val searchData = repository.searchComedy(words)
        Log.d(TAG, "Search Data: $searchData")
        comedy.value = searchData
    }

    fun firstJsonLoad() {
        val apiData = repository.firstJsonLoad()
        Log.d(TAG, "Api Data: $apiData")
        comedy.value = apiData
    }

    fun loadMoreData(page: Int) {
        val apiData = repository.loadMoreJsonLoad(page)
        Log.d(TAG, "Api Data: $apiData")

        val arr = ArrayList<Content>()

        if (comedy.value != null) {
            for (i in comedy.value!!) {
                arr.add(i)
            }
        }

        if (apiData != null) {
            for (i in apiData) {
                if (comedy.value.isNullOrEmpty()) {
                    var same = false
                    for (j in comedy.value!!) {
                        if (i.name == j.name) {
                            same = true
                        }
                    }

                    if (!same) {
                        arr.add(i)
                    }
                } else {
                    arr.add(i)
                }
            }
        }
        comedy.value = arr
    }

}