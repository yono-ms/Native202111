package com.example.native202111

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202111.network.RepoModel
import com.example.native202111.network.ServerAPI
import com.example.native202111.network.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appDataStore: AppDataStore,
    private val serverAPI: ServerAPI
) : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _welcomeDate = MutableStateFlow(Date().toBestString())
    val welcomeDate: StateFlow<String> = _welcomeDate

    private val _showInputDialog = MutableStateFlow(false)
    val showInputDialog: StateFlow<Boolean> = _showInputDialog

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _repoItems = MutableStateFlow(listOf<RepoModel>())
    val repoItems: StateFlow<List<RepoModel>> = _repoItems

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
//            _userName.value = userName
            appDataStore.setUserName(userName)
        }
    }

    private suspend fun getRepositoryItems(userName: String): List<RepoModel> {
        val userModel = serverAPI.getDecode(
            serverAPI.getUsersUrl(userName),
            UserModel.serializer()
        )
        val repoModels = serverAPI.getDecode(
            userModel.reposUrl,
            ListSerializer(RepoModel.serializer())
        )
        logger.debug("repoModels.size = ${repoModels.size}")
        return repoModels
    }

    init {
        logger.info("init.")
        viewModelScope.launch {
            logger.info("init launch START.")
            kotlin.runCatching {
                appDataStore.appPreferencesFlow.collect {
                    logger.info("appPreferencesFlow collect. $it")
                    _userName.value = it.userName
                    _repoItems.value = getRepositoryItems(it.userName)
                    logger.debug("${_repoItems.value}")
                }
            }.onFailure {
                if (it is CancellationException) {
                    logger.info("init", it)
                } else {
                    logger.error("init", it)
                }
            }
            logger.info("init launch END.")
        }
    }
}
