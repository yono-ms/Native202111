package com.example.native202111

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputDialogViewModel @Inject constructor() : ViewModel() {
    private val _inputValue = MutableStateFlow("")
    val inputValue: StateFlow<String> = _inputValue

    private var isInit = true

    fun initInputValue(value: String) {
        if (isInit) {
            _inputValue.value = value
            isInit = false
        }
    }

    fun setInputValue(value: String) {
        viewModelScope.launch {
            _inputValue.value = value
        }
    }

    fun clearInputValue() {
        viewModelScope.launch {
            _inputValue.value = ""
            isInit = true
        }
    }
}
