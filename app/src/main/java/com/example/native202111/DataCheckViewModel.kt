package com.example.native202111

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202111.database.AppDatabase
import com.example.native202111.database.RepoEntity
import com.example.native202111.database.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

@HiltViewModel
class DataCheckViewModel @Inject constructor(
    appDatabase: AppDatabase,
) : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _showUser = MutableStateFlow(false)
    val showUser: StateFlow<Boolean> = _showUser

    private val _userItems = MutableStateFlow<List<UserEntity>>(listOf())
    val userItems: StateFlow<List<UserEntity>> = _userItems

    private val _repoItems = MutableStateFlow<List<RepoEntity>>(listOf())
    val repoItems: StateFlow<List<RepoEntity>> = _repoItems

    fun setShowUser(value: Boolean) {
        viewModelScope.launch {
            _showUser.value = value
        }
    }

    init {
        logger.info("init START")
        viewModelScope.launch {
            logger.info("init launch user START")
            kotlin.runCatching {
                appDatabase.userDao().loadAllUser().collect {
                    _userItems.value = it
                }
            }.onFailure {
                if (it is CancellationException) {
                    logger.info("init launch user", it)
                } else {
                    logger.error("init launch user", it)
                }
            }
            logger.info("init launch user END.")
        }
        viewModelScope.launch {
            logger.info("init launch repo START")
            kotlin.runCatching {
                appDatabase.repoDao().loadAllRepo().collect {
                    _repoItems.value = it
                }
            }.onFailure {
                if (it is CancellationException) {
                    logger.info("init launch repo", it)
                } else {
                    logger.error("init launch repo", it)
                }
            }
            logger.info("init launch repo END.")
        }
        logger.info("init END")
    }
}