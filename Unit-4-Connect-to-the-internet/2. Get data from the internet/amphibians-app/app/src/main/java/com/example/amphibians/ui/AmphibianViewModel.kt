package com.example.amphibians.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import com.example.amphibians.network.AmphibianApiService
import kotlinx.coroutines.launch
import java.lang.Exception

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus> = _status

    private val _amphibians = MutableLiveData<List<Amphibian>>()
    val amphibians: LiveData<List<Amphibian>> = _amphibians

    private val _amphibian = MutableLiveData<Amphibian>()
    val amphibian: LiveData<Amphibian> = _amphibian

    fun onAmphibianClicked(amphibian: Amphibian) {
        _amphibian.value = amphibian
    }

    fun getAmphibiansList() {

        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING

            try {
                Log.d("ANDYYY", "AmphibianViewModel # getAmphibiansList: calling retrofitService.getAmphibians()")
                _amphibians.value = AmphibianApi.retrofitService.getAmphibians()

                Log.d("ANDYYY", "AmphibianViewModel # getAmphibiansList: retrofitService.getAmphibians() DONE ")
                _status.value = AmphibianApiStatus.DONE
            } catch (e: Exception) {
                Log.e("ANDYYY", "AmphibianViewModel # getAmphibiansList: error = " + e.message)
                _status.value = AmphibianApiStatus.ERROR
                _amphibians.value = listOf()
            }
        }

    }
}
