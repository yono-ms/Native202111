package com.example.native202111.network

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerAPI @Inject constructor() {

    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val urlBase = "https://api.github.com"
    private val urlUsers = "$urlBase/users/"

    fun getUsersUrl(login: String?): String {
        return "$urlUsers$login"
    }

    suspend fun <T> getDecode(urlString: String, deserializer: DeserializationStrategy<T>): T {
        val text = get(urlString)
        return json.decodeFromString(deserializer, text)
    }

    suspend fun get(urlString: String): String {
        logger.info("request START")
        val (request, response, result) = Fuel.get(urlString).awaitStringResponseResult()
        logger.info("$request")
        logger.info("$response")
        return result.get()
    }
}
