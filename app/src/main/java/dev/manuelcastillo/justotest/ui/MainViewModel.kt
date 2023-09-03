package dev.manuelcastillo.justotest.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.manuelcastillo.justotest.data.model.RandomUser
import dev.manuelcastillo.justotest.data.repository.MainRepository
import dev.manuelcastillo.justotest.utils.ResponseStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
class MainViewModel : ViewModel() {
    private val repository: MainRepository = MainRepository()

    private val _mainUIState = MutableStateFlow(MainUIState())
    val mainUIState: StateFlow<MainUIState> = _mainUIState

    init {
        getRandomUser()
    }

    fun getRandomUser() {
        _mainUIState.update { it.copy(status = ResponseStatus.Loading) }
        viewModelScope.launch {
            val response = repository.getRandomUser()
            if (response != null) {
                Log.d(TAG, response.toString())
                _mainUIState.update {
                    it.copy(randomUser = response, status = ResponseStatus.Success)
                }
            } else {
                _mainUIState.update { it.copy(status = ResponseStatus.Error) }
            }
        }
    }

    data class MainUIState(
        val randomUser: RandomUser? = null,
        val status: ResponseStatus = ResponseStatus.Loading
    )
}