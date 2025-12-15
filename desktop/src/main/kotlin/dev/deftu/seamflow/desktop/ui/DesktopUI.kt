package dev.deftu.seamflow.desktop.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.deftu.seamflow.desktop.AppState
import dev.deftu.seamflow.desktop.ui.screen.DiscoveryScreen

@Composable
fun DesktopUI(appState: AppState) {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Row(Modifier.fillMaxSize()) {
        SideNav(
            modifier = Modifier.width(220.dp).fillMaxHeight(),
            onNavigate = { route ->
                println("Navigating to $route")
                navController.navigate(route)
            }
        )

        Divider(Modifier.fillMaxHeight().width(1.dp))
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            DesktopNavHost(navController = navController, appState = appState)
        }
    }
}

@Composable
private fun SideNav(
    modifier: Modifier = Modifier,
    onNavigate: (Route) -> Unit,
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(Modifier.height(8.dp))

        NavButton("SeamFlow") { onNavigate(Route.Home) }
        NavButton("Discovery") { onNavigate(Route.Discovery) }
        NavButton("Transfers") { onNavigate(Route.Transfers) }
        NavButton("Messages") { onNavigate(Route.Messages) }

        Spacer(Modifier.weight(1f))

        NavButton("Settings") { onNavigate(Route.Settings) }
    }
}

@Composable
private fun NavButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(label)
    }
}

@Composable
fun DesktopNavHost(navController: NavHostController, appState: AppState) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable<Route.Home> {
            Column {
                Text(
                    text = "Welcome to SeamFlow",
                    style = MaterialTheme.typography.h4
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Select an option from the side navigation to get started.",
                    style = MaterialTheme.typography.body1
                )
            }
        }

        composable<Route.Discovery> {
            DiscoveryScreen(
                appState = appState,
                onOpenPeer = { id ->
                    navController.navigate(Route.PeerDetails(id.value))
                }
            )
        }

        composable<Route.Transfers> {
//            TransfersScreen()
        }

        composable<Route.Messages> {
//            MessagesScreen()
        }

        composable<Route.Settings> {
//            SettingsScreen()
        }

        composable<Route.PeerDetails> { backStackEntry ->
//            val route = backStackEntry.toRoute<Route.PeerDetails>()
//            PeerDetailsScreen(deviceId = route.toDeviceId())
        }
    }
}
