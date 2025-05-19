package dev.logvinovich.inventario.main.admin.product.details

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import coil3.compose.SubcomposeAsyncImage
import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.admin.product.details.viewmodel.ProductDetailsIntent
import dev.logvinovich.inventario.main.admin.product.details.viewmodel.ProductDetailsViewModel
import dev.logvinovich.inventario.ui.component.BarcodeScannerDialog
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.component.TextFieldComponent
import dev.logvinovich.inventario.ui.util.SnackbarController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    onNavigateUp: (Product?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showScanner by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var cameraPermissionState by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            cameraPermissionState = isGranted
            if (!isGranted) {
                scope.launch {
                    SnackbarController.sendMessageResEvent(R.string.camera_not_granted)
                }
            }
        }
    )

    val pickImage = rememberLauncherForActivityResult(contract = PickVisualMedia()) { uri ->
        viewModel.handleIntent(ProductDetailsIntent.UpdateImageUri(uri))
    }

    LaunchedEffect(uiState.saveResult) {
        uiState.saveResult?.let { savedProduct ->
            onNavigateUp(savedProduct)
        }
    }

    ProgressCard(isLoading = uiState.loading, modifier = Modifier.zIndex(1f))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            if (uiState.isEdit) R.string.save_product else R.string.add_product
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp(null) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            uiState.image?.let {
                SubcomposeAsyncImage(
                    model = it,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Product image",
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_broken_image),
                                contentDescription = "Error image",
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        }
                )
            } ?: Column(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .clickable {
                        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Default.Image,
                    contentDescription = "Image icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(R.string.pick_image),
                    modifier = Modifier.padding(top = 30.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            TextFieldComponent(
                value = uiState.name,
                onValueChange = { viewModel.handleIntent(ProductDetailsIntent.UpdateName(it)) },
                label = stringResource(R.string.name),
                isError = uiState.nameError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Product name"
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                placeholder = stringResource(R.string.enter_product_name)
            )

            TextFieldComponent(
                value = uiState.description,
                onValueChange = { viewModel.handleIntent(ProductDetailsIntent.UpdateDescription(it)) },
                label = stringResource(R.string.description),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Product description"
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                placeholder = stringResource(R.string.enter_product_description)
            )

            TextFieldComponent(
                value = uiState.barcode,
                onValueChange = { viewModel.handleIntent(ProductDetailsIntent.UpdateBarcode(it)) },
                label = stringResource(R.string.barcode),
                isError = uiState.barcodeError,
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_barcode),
                        contentDescription = "Product barcode"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    cameraPermissionState = true
                                    showScanner = true
                                }

                                else -> {
                                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan barcode",
                        )
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.handleIntent(ProductDetailsIntent.SaveProduct) }
                ),
                placeholder = stringResource(R.string.enter_product_barcode)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = { viewModel.handleIntent(ProductDetailsIntent.SaveProduct) }
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showScanner && cameraPermissionState) {
        BarcodeScannerDialog(
            onBarcodeScanned = {
                viewModel.handleIntent(ProductDetailsIntent.UpdateBarcode(it))
                showScanner = false
            },
            onDismiss = { showScanner = false }
        )
    }
}