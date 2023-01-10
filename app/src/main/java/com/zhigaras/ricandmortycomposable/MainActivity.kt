package com.zhigaras.ricandmortycomposable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import com.zhigaras.ricandmortycomposable.model.Location
import com.zhigaras.ricandmortycomposable.model.Personage
import com.zhigaras.ricandmortycomposable.screens.DetailsScreen
import com.zhigaras.ricandmortycomposable.screens.LocationListScreen
import com.zhigaras.ricandmortycomposable.screens.PersonageCard
import com.zhigaras.ricandmortycomposable.screens.PersonageListScreen
import com.zhigaras.ricandmortycomposable.ui.theme.RicAndMortyComposableTheme
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            RicAndMortyComposableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RickAndMortyApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RickAndMortyApp(personageViewModel: PersonageViewModel = viewModel()) {
    
    val pagedPersonages = personageViewModel.pagedPersonages
    val pagedLocations = personageViewModel.pagedLocations
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        bottomTabList.find { it.route == currentDestination?.route } ?: PersonageList
    
    Scaffold(
        bottomBar = {
            BottomTabRow(
                allScreens = bottomTabList,
                onTabSelected = { newScreen ->
                    navController.navigate(newScreen.route)
                },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        SetUpNavHost(
            navController = navController,
            pagedPersonages = pagedPersonages,
            pagedLocations = pagedLocations,
            modifier = Modifier.padding(innerPadding)
        )
    }
    
    
}

@Composable
fun BottomTabRow(
    allScreens: List<Destination>,
    onTabSelected: (Destination) -> Unit,
    currentScreen: Destination
) {
    Surface(
        Modifier
            .height(56.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    
    ) {
        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            allScreens.forEach { screen ->
                BottomTab(
                    text = screen.route,
                    icon = screen.icon,
                    onSelected = { onTabSelected(screen) },
                    selected = currentScreen == screen
                )
            }
        }
    }
}

@Composable
fun BottomTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(text = text)
    }
}

@Composable
fun SetUpNavHost(
    navController: NavHostController,
    pagedPersonages: Flow<PagingData<Personage>>,
    pagedLocations: Flow<PagingData<Location>>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PersonageList.route,
        modifier = modifier
    ) {
        composable(route = PersonageList.route) {
            PersonageListScreen(
                pagedPersonages = pagedPersonages,
                onPersonageClick = { personageId ->
                    navController.navigate("${Details.route}/$personageId")
                }
            )
        }
        composable(
            route = Details.routeWithArgs,
            arguments = Details.arguments
        ) { navBackStackEntry ->
            val personageId = navBackStackEntry.arguments?.getInt(Details.personageIdArg)
            DetailsScreen(personageId!!)
        }
        composable(route = LocationsList.route) {
            LocationListScreen(pagedLocations)
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

val bottomTabList = listOf(PersonageList, LocationsList)