package com.example.holidayimage.funtion.home
enum class LoadMoreState{
    LOADING,
    DONE
}
data class LoadMoreInfo(var loadingState:LoadMoreState)