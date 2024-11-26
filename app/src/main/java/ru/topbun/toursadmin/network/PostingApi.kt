package ru.topbun.toursadmin.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.topbun.toursadmin.models.config.Config

class PostingApi(
    private val apiFactory: ApiFactory = ApiFactory
) {

    suspend fun setConfig(config: Config) = apiFactory.postingClient.post("/config"){
        setBody(config)
    }

    suspend fun getConfig() = apiFactory.postingClient.get("/config").body<Config>()

}