package ru.topbun.toursadmin.ui.screens.login

import ru.topbun.toursadmin.ui.screens.login.LoginState.LoginScreenState.Error
import ru.topbun.toursadmin.ui.screens.login.LoginState.LoginScreenState.Success
import ru.topbun.toursadmin.utills.Const
import ru.topbun.toursadmin.utills.ScreenModelState

class LoginViewModel: ScreenModelState<LoginState>(LoginState()) {

    fun changePassword(pass: String){
        updateState { copy(password = pass) }
    }

    fun resetScreenState() = updateState { copy(loginState = LoginState.LoginScreenState.Initial) }

    fun checkPassword(){
        updateState {
            copy(
                loginState = if (stateValue.password == Const.PASSWORD_FOR_LOGIN) Success else Error
            )
        }
    }

}