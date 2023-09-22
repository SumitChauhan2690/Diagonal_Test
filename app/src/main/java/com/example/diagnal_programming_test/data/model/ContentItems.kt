package com.example.diagnal_programming_test.data.model


import com.google.gson.annotations.SerializedName

data class ContentItems(
    @SerializedName("content")
    var content: List<Content>
)