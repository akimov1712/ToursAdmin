package ru.topbun.toursadmin.repository

import ru.topbun.toursadmin.models.config.Config
import ru.topbun.toursadmin.network.PostingApi

class PostingRepository(private val api: PostingApi) {

    suspend fun getConfig() = api.getConfig()

    suspend fun setConfig(config: Config) = api.setConfig(config)

}