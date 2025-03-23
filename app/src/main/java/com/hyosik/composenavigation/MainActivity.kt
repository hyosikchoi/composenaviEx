package com.hyosik.composenavigation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hyosik.composenavigation.ui.theme.ComposeNavigationTheme
import kotlinx.serialization.Serializable

@Serializable
data class Profile(val name: String)

@Serializable
object FriendsList

sealed class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Home : Screen("home", Icons.Default.Home)
    data object Search : Screen("search", Icons.Default.Search)
    data object Profile : Screen("profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeNavigationTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screen.Home.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text("Home Screen")
                            }
                        }
                        composable(Screen.Search.route) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text("Search Screen")
                            }
                        }
                        composable(Screen.Profile.route) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text("Profile Screen")
                            }
                        }
                    }


//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding)
//                    ) {
//                        MyApp()
//                    }

                }
            }
        }
    }
}

// Define the MyApp composable, including the `NavController` and `NavHost`.
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Profile(name = "John Smith")) {
        composable<Profile> { backStackEntry ->
            val profile: Profile = backStackEntry.toRoute()
            ProfileScreen(
                profile = profile,
                onNavigateToFriendsList = {
                    navController.navigate(route = FriendsList)
                }
            )
        }
        composable<FriendsList> {
            FriendsListScreen(
                onNavigateToProfile = {
                    navController.navigate(
                        route = Profile(name = "Aisha Devi")
                    )
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val context = LocalContext.current

    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Profile
    )
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.route) },
                label = { Text(screen.route) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        /** 인자로 받은곳 까지 백스택을 제거함. */
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    Toast.makeText(context, "이름: ${screen.route}, ${screen.hashCode()}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun ProfileScreen(
    profile: Profile,
    onNavigateToFriendsList: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile for ${profile.name}")
        Button(onClick = { onNavigateToFriendsList() }) {
            Text("Go to Friends List")
        }

    }
}

// Define the FriendsListScreen composable.
@Composable
fun FriendsListScreen(onNavigateToProfile: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Friends List")
        Button(onClick = { onNavigateToProfile() }) {
            Text("Go to Profile")
        }
    }

}


@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    ComposeNavigationTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MyApp()
        }
    }
}