package dev.logvinovich.inventario.main.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.ui.component.TextFieldComponent

@Composable
fun EmptyOrganizationListContent(
    organizationName: String,
    organizationNameHasError: Boolean,
    onUpdateOrganizationName: (String) -> Unit,
    onClearOrganizationName: () -> Unit,
    onCreateOrganization: () -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.create_organization),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            TextFieldComponent(
                value = organizationName,
                onValueChange = onUpdateOrganizationName,
                placeholder = stringResource(R.string.enter_organization_name),
                trailingIcon = {
                    if (organizationName.isNotEmpty()) {
                        IconButton(onClick = onClearOrganizationName) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.organization)
                            )
                        }
                    }
                },
                isError = organizationNameHasError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onCreateOrganization()
                    }
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(15.dp),
                onClick = onCreateOrganization
            ) {
                Text(
                    text = stringResource(R.string.add_organization),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}