package dev.logvinovich.inventario.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.logvinovich.inventario.R

@Composable
fun InputDialog(
    title: String,
    inputPlaceholder: String,
    inputValue: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    onSubmit: () -> Unit,
    submitButtonText: String = stringResource(R.string.create)
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                TextFieldComponent(
                    value = inputValue,
                    onValueChange = onValueChange,
                    placeholder = inputPlaceholder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp, top = 25.dp),
                    isError = isError
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontSize = 15.sp
                        )
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = submitButtonText,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}