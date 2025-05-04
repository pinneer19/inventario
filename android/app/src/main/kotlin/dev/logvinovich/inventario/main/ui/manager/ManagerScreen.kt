package dev.logvinovich.inventario.main.ui.manager

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.logvinovich.inventario.main.ui.admin.viewmodel.AdminViewModel
import dev.logvinovich.inventario.ui.component.DetailedDrawerExample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerScreen() {
    DetailedDrawerExample {
        Text(text = "hello ")
    }
//    val uiState by viewModel.uiState.collectAsState()
//    val pullToRefreshState = rememberPullToRefreshState()
//
//    PullToRefreshBox(
//        state = pullToRefreshState,
//        isRefreshing = uiState.loading,
//        onRefresh = { viewModel.handleIntent(MainIntent.GetAdminOrganizations) }
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 10.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            items(uiState.organizations) { organization ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.LightGray)
//                ) {
//                    Text(
//                        text = organization.name,
//                        modifier = Modifier.padding(15.dp)
//                    )
//                }
//            }
//
//            item {
//                if (uiState.organizations.isEmpty()) {
//                    Text("You don't have any items in this shopping list.")
//                }
//                TextField(value = uiState.organizationName, onValueChange = {
//                    viewModel.handleIntent(MainIntent.UpdateOrganizationName(it))
//                })
//                Button(onClick = {
//                    viewModel.handleIntent(MainIntent.CreateOrganization)
//                }) {
//                    Text(text = "Create organization")
//                }
//            }
//        }
//
//        AnimatedVisibility(uiState.error != null) {
//            Text(
//                text = requireNotNull(uiState.error),
//                fontSize = 16.sp
//            )
//        }
//    }
}

@Preview
@Composable
fun ManagerScreenPreview() {
    ManagerScreen()
}