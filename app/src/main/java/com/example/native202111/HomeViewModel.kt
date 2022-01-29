package com.example.native202111

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _welcomeDate = MutableStateFlow(Date().toBestString())
    val welcomeDate: StateFlow<String> = _welcomeDate

    fun refresh() {
        logger.info("refresh START.")
        viewModelScope.launch {
            logger.info("update.")
            _welcomeDate.value = Date().toBestString()
        }
    }
}
