package com.example.cameraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cameraapp.feature.camera.CameraScreen
import com.example.cameraapp.feature.configuration.ConfigurationScreen
import com.example.cameraapp.feature.edit.EditScreen
import com.example.cameraapp.feature.gallery.GalleryScreen
import com.example.cameraapp.feature.permission.AppPermission
import com.example.cameraapp.feature.preview.PreviewScreen
import com.example.cameraapp.router.Args
import com.example.cameraapp.router.Router
import com.example.cameraapp.router.navigate
import com.example.cameraapp.router.route

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            ComposerTheme {
                AppPermission {
                    val navHost = rememberNavController()
                    NavGraph(navHost)
                }
            }
        }
    }

    @Composable
    fun NavGraph(navHost: NavHostController) {
        NavHost(navHost, startDestination = Router.Camera.route) {
            route(Router.Camera) {
                CameraScreen(
                    onGalleryClick = { navHost.navigate(Router.Gallery) },
                    onConfigurationClick = { navHost.navigate(Router.Configuration) }
                )
            }
            route(Router.Gallery) {
                GalleryScreen(
                    onBackPressed = { navHost.navigateUp() },
                    onPreviewClick = { navHost.navigate(Router.Preview.createRoute(it)) }
                )
            }
            route(Router.Configuration) {
                ConfigurationScreen(onBackPressed = { navHost.navigateUp() })
            }
            route(
                route = Router.Preview,
                arguments = listOf(
                    navArgument(Args.Path) { type = NavType.StringType },
                )
            ) {
                PreviewScreen(
                    onBackPressed = { navHost.navigateUp() },
                    onEditPressed = { navHost.navigate(Router.Preview.createRoute(it))}
                )
            }
            route(
                route = Router.Edit,
                arguments = listOf(
                    navArgument(Args.Path) { type = NavType.StringType },
                )
            ) {
                EditScreen (
                    onBackPressed = { navHost.navigateUp() }
                )
            }
        }
    }
}
