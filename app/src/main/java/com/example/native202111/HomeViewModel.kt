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

    private val _showInputDialog = MutableStateFlow(false)
    val showInputDialog: StateFlow<Boolean> = _showInputDialog

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    fun refresh() {
        logger.info("refresh START.")
        viewModelScope.launch {
            logger.info("update.")
            _welcomeDate.value = Date().toBestString()
        }
    }

    fun editUserName() {
        logger.info("editUserName START")
        viewModelScope.launch { _showInputDialog.value = true }
    }

    fun cancelEditUserName() {
        logger.info("cancelEditUserName START")
        viewModelScope.launch { _showInputDialog.value = false }
    }

    fun confirmEditUserName(userName: String) {
        logger.info("confirmEditUserName START $userName")
        viewModelScope.launch {
            _showInputDialog.value = false
            _userName.value = userName
        }
    }
}