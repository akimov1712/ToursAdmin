package ru.topbun.toursadmin.network

import io.ktor.client.request.post
import ru.topbun.toursadmin.models.TypeContent

class TourvisorApi{

    suspend fun getContent(typeContent: TypeContent) = ApiFactory.tourvisorClient.post("/xml/list.php"){
        url {
            parameters.append("type", typeContent.name)
        }
    }

}