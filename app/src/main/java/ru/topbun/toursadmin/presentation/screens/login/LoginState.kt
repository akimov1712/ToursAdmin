package ru.topbun.toursadmin.presentation.screens.login

data class LoginState(
    val password: String = "",
    val loginState: LoginScreenState = LoginScreenState.Initial
){
    sealed interface LoginScreenState{
        data object Initial: LoginScreenState
        data object Error: LoginScreenState
        data object Success: LoginScreenState
    }
}
