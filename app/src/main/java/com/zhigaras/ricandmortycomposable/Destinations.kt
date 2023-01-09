package com.zhigaras.ricandmortycomposable

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destinations {
    val route: String
}

object PersonageList : Destinations {
    override val route = "personagesList"
    
}

object Details : Destinations {
    override val route = "details"
    const val personageIdArg = "personage_id"
    val routeWithArgs = "$route/{$personageIdArg}"
    val arguments = listOf(
        navArgument(personageIdArg) { type = NavType.IntType }
    )
}

object Locations : Destinations {
    override val route = "locations"
}