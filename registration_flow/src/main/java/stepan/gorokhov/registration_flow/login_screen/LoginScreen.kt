package stepan.gorokhov.registration_flow.login_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import stepan.gorokhov.components.DataTextField
import stepan.gorokhov.registration_flow.R
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun LoginScreen(viewModel: LoginScreenViewModel = viewModel()) {
    val state = viewModel.loginState.collectAsState().value
    Scaffold {
        Column(Modifier.padding(it)) {
            DataTextField(
                value = state.email,
                onValueChange = { viewModel.setEmail(it) },
                placeholder = { Text(text = stringResource(id = R.string.email)) },
                backgroundColor = Color.Cyan
            )
            DataTextField(
                value = state.password,
                onValueChange = { viewModel.setPassword(it) },
                placeholder = { Text(text = stringResource(id = R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                backgroundColor = Color.Cyan
            )
            Text(text = stringResource(id = R.string.login), modifier = Modifier.clickable(onClick = viewModel::login))
        }
    }
}