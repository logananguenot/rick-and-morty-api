package org.mathieu.cleanrmapi.ui.screens.locationDetails

import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.location.usecases.GetLocationWithCharacters
import org.mathieu.cleanrmapi.ui.core.Destination
import org.mathieu.cleanrmapi.ui.core.ViewModel

sealed interface LocationDetailsAction {
    data class SelectedCharacter(val character: Character): LocationDetailsAction
}

class LocationDetailsViewModel :
    ViewModel<LocationDetailsState>(LocationDetailsState.Loading) {

    fun init(locationId: Int) {
        println("Entrée dans le LocationDetailsViewModel")

        fetchData(
            source = { GetLocationWithCharacters(locationId = locationId) }
        ) {
            onSuccess { details ->
                updateState {
                    LocationDetailsState.Loaded(
                        id = details.id,
                        name = details.name,
                        type = details.type,
                        dimension = details.dimension,
                        residents = details.residents
                    )
                }
                println("Détails : " + details.name)
                details.residents.forEach { character ->
                    println("Resident : " + character.name)
                }
            }

            onFailure {
                updateState {
                    println("Fail retrieving location details for location $locationId")
                    LocationDetailsState.Error(message = it.message ?: it.toString())
                }
            }

        }

    }


    fun handleAction(action: LocationDetailsAction) {
        when(action) {
            is LocationDetailsAction.SelectedCharacter -> selectedCharacter(action.character)
        }
    }


    private fun selectedCharacter(character: Character) =
        sendEvent(Destination.CharacterDetails(character.id.toString()))



}

sealed interface LocationDetailsState {
    data object Loading : LocationDetailsState

    data class Error(val message: String) : LocationDetailsState

    data class Loaded(
        val id: Int,
        val name: String,
        val type: String,
        val dimension: String,
        val residents: List<Character>
    ) : LocationDetailsState

}