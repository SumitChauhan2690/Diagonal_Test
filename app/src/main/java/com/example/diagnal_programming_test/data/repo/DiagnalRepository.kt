package com.example.diagnal_programming_test.data.repo

import android.content.Context
import android.util.Log
import com.example.diagnal_programming_test.R
import com.example.diagnal_programming_test.data.model.Comedy
import com.example.diagnal_programming_test.data.model.Content
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import javax.inject.Singleton


@Singleton
class DiagnalRepository(private val context: Context) {
    private val TAG: String = DiagnalRepository::class.java.name
    private lateinit var searchContent: ArrayList<Content>

    fun getApiData(): List<Content> {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.page1)

        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        inputStream.use { stream ->
            val reader: Reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }

        val jsonString: String = writer.toString()

        Log.d(TAG, "JsonString: $jsonString")

        createListOfModelFromJson(loadJsonFromFile(R.raw.page1))

        val gson = Gson()
        return gson.fromJson(jsonString, Comedy::class.java).page.contentItems.content
    }

    fun initPreDataLoading() {
        searchContent = ArrayList()
        val content1 = createListOfModelFromJson(loadJsonFromFile(R.raw.page1))
        val content2 = createListOfModelFromJson(loadJsonFromFile(R.raw.page2))
        val content3 = createListOfModelFromJson(loadJsonFromFile(R.raw.page3))

        searchContent.addAll(content1?.page?.contentItems?.content!!)
        searchContent.addAll(content2?.page?.contentItems?.content!!)
        searchContent.addAll(content3?.page?.contentItems?.content!!)
       /* for (i in content1?.page?.contentItems?.content!!) {
            searchContent.add(i)
        }

        for (i in content2?.page?.contentItems?.content!!) {
            searchContent.add(i)
        }

        for (i in content3?.page?.contentItems?.content!!) {
            searchContent.add(i)
        }*/
    }

    fun firstJsonLoad(): List<Content>? {
        return createListOfModelFromJson(loadJsonFromFile(R.raw.page1))?.page?.contentItems?.content
    }

    fun loadMoreJsonLoad(page: Int): List<Content>? {
        return when (page) {
            1 -> {
                createListOfModelFromJson(loadJsonFromFile(R.raw.page1))?.page?.contentItems?.content
            }
            2 -> {
                createListOfModelFromJson(loadJsonFromFile(R.raw.page2))?.page?.contentItems?.content
            }
            3 -> {
                createListOfModelFromJson(loadJsonFromFile(R.raw.page3))?.page?.contentItems?.content
            }
            else -> {
                createListOfModelFromJson(loadJsonFromFile(R.raw.page1))?.page?.contentItems?.content
            }
        }
    }

    fun searchComedy(words: String): ArrayList<Content> {
        val arr = ArrayList<Content>()
        Log.d(TAG, "searchComedysize: ${searchContent.size}")
        for (i in searchContent) {
            if (i.name.contains(words,true)) {
                arr.add(i)
            }
        }
        return arr
    }

    private fun loadJsonFromFile(id: Int): InputStream {
        return context.resources.openRawResource(id)
    }

    private fun createListOfModelFromJson(loadJsonFromFile: InputStream): Comedy? {
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        loadJsonFromFile.use { stream ->
            val reader: Reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }

        val jsonString: String = writer.toString()

        Log.d(TAG, "JsonString: $jsonString")

        val gson = Gson()
        return gson.fromJson(jsonString, Comedy::class.java)
    }
}