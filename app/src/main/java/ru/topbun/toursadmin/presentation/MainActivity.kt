package ru.topbun.toursadmin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.topbun.toursadmin.presentation.screens.login.LoginScreen
import ru.topbun.toursadmin.presentation.screens.settings.SettingsScreen
import ru.topbun.toursadmin.presentation.theme.Colors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberSystemUiController()
            controller.setStatusBarColor(Colors.ORANGE, true)
            Navigator(LoginScreen)
        }
    }
}
