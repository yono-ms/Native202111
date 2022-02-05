package com.example.native202111

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202111.database.AppDatabase
import com.example.native202111.database.RepoEntity
import com.example.native202111.database.UserEntity
import com.example.native202111.network.RepoModel
import com.example.native202111.network.ServerAPI
import com.example.native202111.network.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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
    private val appDatabase: AppDatabase,
    private val serverAPI: ServerAPI
) : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _welcomeDate = MutableStateFlow(Date().toBestString())
    val welcomeDate: StateFlow<String> = _welcomeDate

    private val _showInputDialog = MutableStateFlow(false)
    val showInputDialog: StateFlow<Boolean> = _showInputDialog

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _repoItems = MutableStateFlow(listOf<RepoEntity>())
    val repoItems: StateFlow<List<RepoEntity>> = _repoItems

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
            appDataStore.setUserName(userName)
        }
    }

    private suspend fun getRepositoryItems(userName: String) {
        val userModel = serverAPI.getDecode(
            serverAPI.getUsersUrl(userName),
            UserModel.serializer()
        )
        val repoModels = serverAPI.getDecode(
            userModel.reposUrl,
            ListSerializer(RepoModel.serializer())
        )
        logger.debug("repoModels.size = ${repoModels.size}")

        val userDate = userModel.updatedAt.fromIsoToDate()
        val userEntity = UserEntity(
            id = userModel.id,
            userName = userName,
            updateAt = userDate.time,
            updateAtText = userDate.toBestString(),
            reposUrl = userModel.reposUrl
        )
        appDatabase.userDao().insert(userEntity)

        val list = mutableListOf<RepoEntity>()
        repoModels.forEach {
            val repoDate = it.updatedAt.fromIsoToDate()
            val entry = RepoEntity(
                id = it.id,
                name = it.name,
                updateAt = repoDate.time,
                updateAtText = repoDate.toBestString(),
                userId = userModel.id,
                userName = userName
            )
            list.add(entry)
        }
        appDatabase.repoDao().insertAll(*list.toTypedArray())
    }

    private var job: Job? = null

    private fun loadReposFromDatabase(userName: String) {
        logger.info("loadReposFromDatabase $userName")
        job?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        job = viewModelScope.launch {
            logger.info("loadReposFromDatabase launch START.")
            kotlin.runCatching {
                appDatabase.repoDao().loadRepoByUserName(userName).collect {
                    logger.info("loadReposFromDatabase collect. $it")
                    _repoItems.value = it
                }
            }.onFailure {
                if (it is CancellationException) {
                    logger.info("init", it)
                } else {
                    logger.error("init", it)
                }
            }
            logger.info("loadReposFromDatabase launch END.")
        }
    }

    init {
        logger.info("init.")
        viewModelScope.launch {
            logger.info("init launch START.")
            kotlin.runCatching {
                appDataStore.appPreferencesFlow.collect { preferences ->
                    logger.info("appPreferencesFlow collect. $preferences")
                    _userName.value = preferences.userName
                    logger.debug("${_repoItems.value}")

                    if (preferences.userName.isNotEmpty()) {
                        if (appDatabase.repoDao().get(preferences.userName).isEmpty()) {
                            getRepositoryItems(preferences.userName)
                        }
                        loadReposFromDatabase(preferences.userName)
                    }
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
