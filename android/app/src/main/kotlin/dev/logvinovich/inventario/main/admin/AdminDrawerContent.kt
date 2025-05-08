package dev.logvinovich.inventario.main.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.logvinovich.domain.model.Organization
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.ui.component.InputDialog

@Composable
fun AdminDrawerContent(
    organizations: List<Organization>,
    selectedOrganizationId: Long,
    onOrganizationClick: (Long) -> Unit,
    organizationName: String,
    organizationNameHasError: Boolean,
    showOrganizationDialog: Boolean,
    onShowOrganizationDialog: () -> Unit,
    onDismissOrganizationDialog: () -> Unit,
    onUpdateOrganizationName: (String) -> Unit,
    onCreateOrganization: () -> Unit,
) {
    ModalDrawerSheet {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            item(key = "Drawer top bar") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.organizations),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(
                        onClick = onShowOrganizationDialog
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add organization"
                        )
                    }
                }
                HorizontalDivider()
            }

            items(
                items = organizations,
                key = { organization -> organization.id }
            ) { organization ->
                NavigationDrawerItem(
                    modifier = Modifier.padding(top = 10.dp).animateItem(),
                    label = {
                        Text(organization.name)
                    },
                    selected = organization.id == selectedOrganizationId,
                    onClick = { onOrganizationClick(organization.id) }
                )
            }
        }
    }

    if (showOrganizationDialog) {
        InputDialog(
            title = stringResource(R.string.creating_organization),
            inputValue = organizationName,
            onValueChange = onUpdateOrganizationName,
            inputPlaceholder = stringResource(R.string.enter_organization_name),
            onSubmit = onCreateOrganization,
            onDismissRequest = onDismissOrganizationDialog,
            isError = organizationNameHasError
        )
    }
}