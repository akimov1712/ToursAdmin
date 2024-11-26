package ru.topbun.toursadmin.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiFactory {

    private val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }

        defaultRequest {
            contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
    }

    val postingClient = client.config {
        defaultRequest {
            url {
                url("http://192.168.31.188:8080")
            }
        }
    }

    val tourvisorClient = client.config {
        defaultRequest {
            url {
                url("http://tourvisor.ru")
                parameters.append("authlogin", "tyrmarketsamara@yandex.ru")
                parameters.append("authpass", "gON5OLKRTTU4")
                parameters.append("format", "json")
            }
        }
    }

}