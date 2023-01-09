package com.zhigaras.ricandmortycomposable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.PagingData
import com.zhigaras.ricandmortycomposable.model.Personage
import com.zhigaras.ricandmortycomposable.screens.DetailsScreen
import com.zhigaras.ricandmortycomposable.screens.LocationsScreen
import com.zhigaras.ricandmortycomposable.screens.PersonageCard
import com.zhigaras.ricandmortycomposable.screens.PersonageListScreen
import com.zhigaras.ricandmortycomposable.ui.theme.RicAndMortyComposableTheme
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val personageViewModel: PersonageViewModel by viewModels()
        
        setContent {
            RicAndMortyComposableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RickAndMortyApp(personageViewModel.pagedPersonages)
                }
            }
        }
    }
}

@Composable
fun RickAndMortyApp(pagedPersonages: Flow<PagingData<Personage>>) {
    
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = PersonageList.route,
        modifier = Modifier
    ) {
        composable(route = PersonageList.route) {
            PersonageListScreen(
                pagedPersonages = pagedPersonages,
                onPersonageClick = { personageId ->
                    navController.navigate("${Details.route}/$personageId") }
            )
        }
        composable(
            route = Details.routeWithArgs,
            arguments = Details.arguments
        ) { navBackStackEntry ->
            val personageId = navBackStackEntry.arguments?.getInt(Details.personageIdArg)
            DetailsScreen(personageId)
        }
        composable(route = Locations.route) {
            LocationsScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RicAndMortyComposableTheme {
        PersonageCard(personage = Test.testPersonage)
    }
}