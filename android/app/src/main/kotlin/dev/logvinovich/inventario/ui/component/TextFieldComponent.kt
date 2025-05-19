package dev.logvinovich.inventario.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.ui.theme.InventarioTheme

@Composable
fun TextFieldComponent(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    isError: Boolean = false,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = RoundedCornerShape(10.dp),
) {
    Column(
        modifier = modifier
    ) {
        label?.let {
            Text(
                text = it,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            ),
            isError = isError,
            textStyle = MaterialTheme.typography.bodyLarge,
            value = value,
            enabled = enabled,
            singleLine = true,
            placeholder = { Text(text = placeholder) },
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            shape = shape,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = {
                supportingText?.let {
                    Text(text = it)
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TextFieldComponentPreview() {
    InventarioTheme {
        TextFieldComponent(
            value = "Sample",
            label = "Username",
            placeholder = "Enter your username",
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {},
            trailingIcon = {}
        )
    }
}
