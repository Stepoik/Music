package stepan.gorokhov.registration_flow.registration_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import stepan.gorokhov.domain.repositories.RegistrationRepository
import javax.inject.Inject

data class RegistrationState(
    val email: String,
    val password: String,
    val error:String,
    val isLoading:Boolean,
    val isLogged:Boolean
)

class RegistrationScreenViewModel @Inject constructor(private val registrationRepository: RegistrationRepository) :
    ViewModel() {
    private val _emailValue = MutableStateFlow("")
    private val _passwordValue = MutableStateFlow("")
    private val _errorValue = MutableStateFlow("")
    private val _uiState: MutableStateFlow<RegistrationState> =
        MutableStateFlow(RegistrationState("","","", false, false))
    val uiState = _uiState
    init {
        viewModelScope.launch {
            _emailValue.collect{
                _uiState.value = _uiState.value.copy(email = it)
            }
        }
        viewModelScope.launch {
            _passwordValue.collect{
                _uiState.value = _uiState.value.copy(email = it)
            }
        }
        viewModelScope.launch {
            _errorValue.collect{
                _uiState.value = _uiState.value.copy(email = it)
            }
        }
    }
    fun register() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            registrationRepository.register(_emailValue.value, _passwordValue.value)
                .onFailure {
                    _uiState.value = _uiState.value.copy(email = it.message.toString())
                }
            _uiState.value = _uiState.value.copy(isLoading = false)

        }
    }
    fun setEmail(email:String){
        _emailValue.value = email
    }
    fun setPassword(password:String){
        _passwordValue.value = password
    }
}