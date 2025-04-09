package org.mathieu.cleanrmapi.domain.location.usecases

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.cleanrmapi.domain.location.LocationRepository
import org.mathieu.cleanrmapi.domain.location.LocationWithResidents

object GetLocationWithCharacters : KoinComponent {
    private val locationRepository: LocationRepository by inject()

    suspend operator fun invoke(locationId: Int): LocationWithResidents {
        println("Start location retrieval")
        val location = locationRepository.getLocation(locationId)
        println("Location" + location)
        val characters = locationRepository.getCharactersIn(locationId)
        println("Characters" + characters)
        return LocationWithResidents(
            location = location,
            residents = characters
        )
    }
}