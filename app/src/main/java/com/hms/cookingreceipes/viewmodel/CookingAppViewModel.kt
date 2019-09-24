package com.hms.cookingreceipes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hms.cookingreceipes.data.model.Blogspot
import com.hms.cookingreceipes.repository.Repository

class CookingAppViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var mutableLiveData: MutableLiveData<Blogspot>

   fun getBlogArticles(params: Map<String, String>): LiveData<Blogspot> {
        mutableLiveData = Repository().getBlogSpotArticles(params)
        return mutableLiveData
    }
}