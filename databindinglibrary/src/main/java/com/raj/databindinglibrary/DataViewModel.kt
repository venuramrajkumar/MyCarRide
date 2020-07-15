package com.raj.databindinglibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlin.reflect.KTypeProjection.Companion.STAR

class DataViewModel : ViewModel() {

    private val nameLivedata = MutableLiveData("RAJKUMAR")
    private val lastNameLivedata = MutableLiveData("NEELAM")
    private val likesCountLiveData = MutableLiveData(0)

    val name : LiveData<String> = nameLivedata
    val lastName : LiveData<String> = lastNameLivedata
    val likes : LiveData<Int> = likesCountLiveData

    val popularity: LiveData<Popularity> = Transformations.map(likesCountLiveData) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }

    fun onLike() {
        likesCountLiveData.value = likesCountLiveData.value?.plus(1)
    }

}





