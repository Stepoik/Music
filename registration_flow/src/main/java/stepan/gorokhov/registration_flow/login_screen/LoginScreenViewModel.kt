package stepan.gorokhov.registration_flow.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stepan.gorokhov.domain.repositories.RegistrationRepository
import javax.inject.Inject

data class LoginState(
    val isLogged:Boolean,
    val isLoading:Boolean,
    val email:String,
    val password:String,
    val error:String
)

class LoginScreenViewModel @Inject constructor(private val registrationRepository: RegistrationRepository):ViewModel() {
    private val _loginState = MutableStateFlow(LoginState(false, false, "","", ""))
    val loginState: StateFlow<LoginState> get() = _loginState
    fun login(){
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true)
            registrationRepository.login(email = _loginState.value.email, password = _loginState.value.password).onFailure {
                _loginState.value = _loginState.value.copy(error = it.message.toString())
            }
            _loginState.value = _loginState.value.copy(isLoading = false)
        }
    }
    fun setEmail(email: String){
        _loginState.value = _loginState.value.copy(email=email)
    }
    fun setPassword(password: String){
        _loginState.value = _loginState.value.copy(password = password)
    }
}