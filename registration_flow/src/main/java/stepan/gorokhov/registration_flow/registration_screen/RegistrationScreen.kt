package stepan.gorokhov.registration_flow.registration_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import stepan.gorokhov.components.DataTextField
import stepan.gorokhov.registration_flow.R
import androidx.lifecycle.viewmodel.compose.viewModel
import stepan.gorokhov.components.LoadingScreen


@Composable
internal fun RegistrationScreen(onLogged:()->Unit, viewModel: RegistrationScreenViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState().value
    val isLogged = uiState.isLogged
    LaunchedEffect(isLogged){
        if (isLogged){
            onLogged()
        }
    }
    Scaffold {
        if (uiState.isLoading) {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        } else {
            InputScreen(
                emailValue = uiState.email,
                passwordValue = uiState.password,
                onEmailChange = { viewModel.setEmail(it) },
                onPasswordChange = { viewModel.setPassword(it) },
                onRegisterClicked = { viewModel.register() },
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
internal fun InputScreen(
    emailValue: String,
    passwordValue: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.registration),
                modifier = Modifier.padding(bottom = 30.dp)
            )
            DataTextField(
                value = emailValue,
                onValueChange = onEmailChange,
                placeholder = { Text(text = stringResource(id = R.string.email)) },
                backgroundColor = Color.Cyan
            )
            DataTextField(
                value = passwordValue,
                onValueChange = onPasswordChange,
                placeholder = { Text(text = stringResource(id = R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                backgroundColor = Color.Cyan
            )
            Text(text = "Register",
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Red)
                    .padding(horizontal = 30.dp)
                    .clickable { onRegisterClicked() })
        }
    }
}

@Composable
@Preview
internal fun RegistrationScreenPreview() {
    MaterialTheme {
        RegistrationScreen({})
    }
}