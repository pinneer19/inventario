package dev.logvinovich.inventario.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.domain.model.Role
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.component.RoleChip
import dev.logvinovich.inventario.ui.component.TextFieldComponent

@Composable
fun AuthScreen(
    title: String,
    username: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    isPasswordVisible: Boolean,
    toggleAuthScreen: String,
    onUpdateUsername: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
    onClearUsername: () -> Unit,
    togglePasswordVisibility: () -> Unit,
    onAuthToggle: () -> Unit,
    onSubmit: () -> Unit,
    onGoogleSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    selectedRole: Role? = null,
    showRoleButtons: Boolean = false,
    onRoleUpdate: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        snackbarHostState.showSnackbar(message = error.orEmpty())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .imePadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        TextFieldComponent(
            value = username,
            label = stringResource(id = R.string.username),
            placeholder = stringResource(id = R.string.username_placeholder),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(),
            onValueChange = onUpdateUsername,
            trailingIcon = {
                if (username.isNotEmpty()) {
                    IconButton(onClick = onClearUsername) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
        )

        TextFieldComponent(
            value = password,
            label = stringResource(id = R.string.password),
            placeholder = stringResource(id = R.string.password_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSubmit()
                }
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = onUpdatePassword,
            trailingIcon = {
                IconButton(onClick = togglePasswordVisibility) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        )

        if (showRoleButtons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                RoleChip(
                    modifier = Modifier.weight(1f),
                    selected = selectedRole?.let { it == Role.ADMIN } == true,
                    onClick = requireNotNull(onRoleUpdate),
                    label = stringResource(R.string.admin)
                )

                Spacer(modifier = Modifier.width(20.dp))

                RoleChip(
                    modifier = Modifier.weight(1f),
                    selected = selectedRole?.let { it == Role.MANAGER } == true,
                    onClick = requireNotNull(onRoleUpdate),
                    label = stringResource(R.string.manager)
                )
            }
        }

        Button(
            onClick = {
                focusManager.clearFocus()
                onSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.or),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            onClick = onGoogleSignIn
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.continue_with_google),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onAuthToggle) {
            Text(text = toggleAuthScreen)
        }
    }

    ProgressCard(isLoading)
}