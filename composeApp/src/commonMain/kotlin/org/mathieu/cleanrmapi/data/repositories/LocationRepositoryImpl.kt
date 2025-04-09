package org.mathieu.cleanrmapi.data.repositories

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.cleanrmapi.common.toList
import org.mathieu.cleanrmapi.data.local.LocationDAO
import org.mathieu.cleanrmapi.data.local.objects.LocationObject
import org.mathieu.cleanrmapi.data.local.objects.toDBObject
import org.mathieu.cleanrmapi.data.local.objects.toModel
import org.mathieu.cleanrmapi.data.local.objects.toPreviewModel
import org.mathieu.cleanrmapi.data.remote.CharacterApi
import org.mathieu.cleanrmapi.data.remote.LocationApi
import org.mathieu.cleanrmapi.data.remote.responses.toDBObject
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.location.Location
import org.mathieu.cleanrmapi.domain.location.LocationRepository
import org.mathieu.cleanrmapi.domain.location.LocationPreview

internal class LocationRepositoryImpl(
    private val characterApi: CharacterApi
) : LocationRepository {

    override suspend fun getLocation(id: Int): Location {
        val locationObject = GetLocationObjectIfExists(locationId = id)

        val idList = locationObject.residentsIds

        val residents = if (idList.isNotEmpty()) {
            if (idList.contains(",")) {
                characterApi.getCharactersFromIds(ids = idList).map { it.toDBObject() }
            } else {
                characterApi.getCharacter(idList.toInt())?.toDBObject()?.let { listOf(it) } ?: emptyList()
            }
        } else {
            emptyList()
        }
        println("Residents" + residents)
        return locationObject.toModel(residents)
    }

    override suspend fun getLocationPreview(id: Int): LocationPreview {
        val locationObject = GetLocationObjectIfExists(locationId = id)
        return locationObject.toPreviewModel()
    }

    override suspend fun getCharactersIn(locationId: Int): List<Character> {
        val locationLocal = GetLocationObjectIfExists(locationId)

        val idList = locationLocal.residentsIds
        println("IDList" + idList)
        return if (idList.contains(",")) {
            val characterResponse = characterApi.getCharactersFromIds(ids = idList)
            characterResponse.map { it.toDBObject().toModel() }
        } else {
            val characterResponse = characterApi.getCharacter(idList.toInt())
            characterResponse?.toDBObject()?.toModel()?.toList() ?: emptyList()
        }
    }
}

private object GetLocationObjectIfExists : KoinComponent {

    private val locationApi: LocationApi by inject()
    private val locationLocal: LocationDAO by inject()

    suspend operator fun invoke(locationId: Int): LocationObject =
        tryToGetLocationLocally(locationId)
            .fetchRemotelyIfNotFound(locationId)
            .throwIfWeCannotFindIt()

    private suspend fun tryToGetLocationLocally(id: Int) = locationLocal.getLocation(id)

    private suspend fun LocationObject?.fetchRemotelyIfNotFound(id: Int): LocationObject? {
        if (this != null) return this

        return locationApi.getLocation(id = id)
            ?.toDBObject()
            ?.also { obj ->
                locationLocal.insert(obj)
            }
    }

    private fun LocationObject?.throwIfWeCannotFindIt(): LocationObject {
        if (this != null) return this
        throw Exception("Could not find Location locally and remotely.")
    }
}