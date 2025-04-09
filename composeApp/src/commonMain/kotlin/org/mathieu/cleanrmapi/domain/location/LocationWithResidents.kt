package org.mathieu.cleanrmapi.domain.location

import org.mathieu.cleanrmapi.domain.character.models.Character

data class LocationWithResidents(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<Character>
) {
    constructor(location: Location, residents: List<Character>) : this(
        id = location.id,
        name = location.name,
        type = location.type,
        dimension = location.dimension,
        residents = residents
    )
}