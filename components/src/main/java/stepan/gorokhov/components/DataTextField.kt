package stepan.gorokhov.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DataTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {},
    backgroundColor: Color = Color.White,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.background(backgroundColor),
        visualTransformation = visualTransformation
    ) { innerTextField ->
        Box(Modifier.padding(10.dp)) {
            innerTextField()
            if (value.isEmpty()) {
                placeholder()
            }
        }
    }
}

@Composable
@Preview
private fun DataTextFieldPreview() {
    DataTextField(value = "asda", onValueChange = {})
}