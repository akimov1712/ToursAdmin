package ru.topbun.toursadmin.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.toursadmin.ui.screens.settings.SettingsScreen
import ru.topbun.toursadmin.ui.theme.Colors

object LoginScreen: Screen{

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE),
            contentAlignment = Alignment.Center
        ){
            val context = LocalContext.current
            val navigator = LocalNavigator.currentOrThrow
            val viewModel = rememberScreenModel { LoginViewModel() }
            val state by viewModel.state.collectAsState()
            LaunchedEffect(state.loginState) {
                when(state.loginState){
                    LoginState.LoginScreenState.Error -> {
                        Toast.makeText(context, "Неверный пароль", Toast.LENGTH_SHORT).show()
                        viewModel.resetScreenState()
                    }
                    LoginState.LoginScreenState.Success -> {
                        navigator.replace(SettingsScreen)
                    }
                    else -> {}
                }
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.password,
                    onValueChange = { if (it.length <=48) viewModel.changePassword(it) },
                    textStyle = LocalTextStyle.current.copy(color = Colors.GRAY, fontSize = 18.sp),
                    placeholder = {
                        Text(text = "Пароль", color = Colors.GRAY_LIGHT, fontSize = 18.sp)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Colors.WHITE,
                        focusedContainerColor = Colors.WHITE,
                        errorContainerColor = Colors.WHITE,
                        unfocusedContainerColor = Colors.WHITE,
                        focusedIndicatorColor = Colors.ORANGE
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(Colors.ORANGE),
                    shape = RoundedCornerShape(8.dp),
                    onClick = viewModel::checkPassword
                ){
                    Text(text = "Войти", color = Colors.GRAY, fontSize = 20.sp)
                }

            }

        }
    }

}