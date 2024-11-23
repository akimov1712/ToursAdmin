package ru.topbun.toursadmin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.topbun.toursadmin.ui.screens.login.LoginScreen
import ru.topbun.toursadmin.ui.theme.Colors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val controller = rememberSystemUiController()
            controller.setStatusBarColor(Colors.ORANGE, true)
            Navigator(LoginScreen)
        }
    }
}
