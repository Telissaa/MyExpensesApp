package pl.wluczak.myexpenses.ui.addexpense

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import org.koin.androidx.compose.koinViewModel
import pl.wluczak.myexpenses.ui.theme.blue
import pl.wluczak.myexpenses.ui.theme.purple
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    viewModel: AddExpenseViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var subCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val categoryFocusRequester = remember { FocusRequester() }
    val subCategoryFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state.isAddingCustomCategory) {
        if (state.isAddingCustomCategory) {
            kotlinx.coroutines.delay(100)
            categoryFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(state.isAddingCustomSubCategory) {
        if (state.isAddingCustomSubCategory) {
            kotlinx.coroutines.delay(100)
            subCategoryFocusRequester.requestFocus()
        }
    }

    // Kopiowanie wybranego zdjęcia do pamięci trwałej aplikacji
    fun saveImageToLocalFolder(contentUri: android.net.Uri?): String? {
        if (contentUri == null) return null
        return try {
            val inputStream = context.contentResolver.openInputStream(contentUri) ?: return null
            val photoDir = java.io.File(context.filesDir, "expense_photos")
            if (!photoDir.exists()) photoDir.mkdirs()
            val photoFile = java.io.File(photoDir, "photo_${System.currentTimeMillis()}.jpg")
            photoFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            photoFile.absolutePath
        } catch (e: Exception) {
            contentUri.toString()
        }
    }

    // Wybór zdjęcia produktu z galerii
    val productImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val localPath = saveImageToLocalFolder(uri)
        viewModel.onProductImageUriChanged(localPath)
    }

    // Wybór zdjęcia paragonu z galerii
    val receiptImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val localPath = saveImageToLocalFolder(uri)
        viewModel.onReceiptImageUriChanged(localPath)
    }

    // Prośba o pozwolenie na dostęp do galerii
    var pendingPhotoType by remember { mutableStateOf<String?>(null) }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            when (pendingPhotoType) {
                "product" -> productImageLauncher.launch("image/*")
                "receipt" -> receiptImageLauncher.launch("image/*")
            }
        }
        pendingPhotoType = null
    }

    fun openGalleryWithPermission(type: String) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            permissionToRequest
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            when (type) {
                "product" -> productImageLauncher.launch("image/*")
                "receipt" -> receiptImageLauncher.launch("image/*")
            }
        } else {
            pendingPhotoType = type
            permissionLauncher.launch(permissionToRequest)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(blue)
            .padding(horizontal = 30.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Nazwa
            WhiteInputField(
                value = state.name,
                onValueChange = { viewModel.onNameChanged(it) },
                placeholder = "nazwa",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Cena
            AlignLeftContainer {
                WhiteInputField(
                    value = state.amount,
                    onValueChange = { viewModel.onAmountChanged(it) },
                    placeholder = "cena",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.width(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Data (format DD-MM-RRRR, kalendarz po kliknięciu)
            AlignLeftContainer {
                WhiteInputField(
                    value = state.date,
                    onValueChange = { viewModel.onDateChanged(it) },
                    placeholder = "data",
                    readOnly = true,
                    onClick = { showDatePickerDialog = true },
                    modifier = Modifier.width(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Kategoria (dropdown)
            AlignLeftContainer {
                Box {
                    WhiteInputField(
                        value = state.category,
                        onValueChange = { viewModel.onCategoryChanged(it) },
                        placeholder = "kategoria",
                        readOnly = !state.isAddingCustomCategory,
                        focusRequester = categoryFocusRequester,
                        onClick = if (!state.isAddingCustomCategory) {
                            { categoryDropdownExpanded = true }
                        } else null,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Wybierz kategorię",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { categoryDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier.width(180.dp)
                    )

                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        state.availableCategories.forEach { categoryOption ->
                            DropdownMenuItem(
                                text = { Text(categoryOption) },
                                onClick = {
                                    viewModel.onCategorySelected(categoryOption)
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("+ Wpisz własną...", color = Color.Gray) },
                            onClick = {
                                viewModel.onCustomCategoryToggle(true)
                                viewModel.onCategoryChanged("")
                                categoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Podkategoria (przesunięta w prawo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 120.dp)
            ) {
                Box {
                    WhiteInputField(
                        value = state.subCategory,
                        onValueChange = { viewModel.onSubCategoryChanged(it) },
                        placeholder = "podkategoria",
                        readOnly = !state.isAddingCustomSubCategory,
                        focusRequester = subCategoryFocusRequester,
                        onClick = if (!state.isAddingCustomSubCategory) {
                            { subCategoryDropdownExpanded = true }
                        } else null,
                        modifier = Modifier.width(180.dp)
                    )

                    DropdownMenu(
                        expanded = subCategoryDropdownExpanded,
                        onDismissRequest = { subCategoryDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Brak (tylko kategoria)", color = Color.Gray) },
                            onClick = {
                                viewModel.onSubCategorySelected("")
                                subCategoryDropdownExpanded = false
                            }
                        )
                        state.availableSubCategories.forEach { subCatOption ->
                            DropdownMenuItem(
                                text = { Text(subCatOption) },
                                onClick = {
                                    viewModel.onSubCategorySelected(subCatOption)
                                    subCategoryDropdownExpanded = false
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("+ Wpisz własną...", color = Color.Gray) },
                            onClick = {
                                viewModel.onCustomSubCategoryToggle(true)
                                viewModel.onSubCategoryChanged("")
                                subCategoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Zwiększona przestrzeń dla zachowania harmonii układu
            Spacer(modifier = Modifier.height(48.dp))

            // 6. Dodaj zdjęcie produktu (po pozwoleniu)
            PhotoBox(
                title = "dodaj zdjęcie produktu",
                hasImage = state.productImageUri != null,
                onClick = { openGalleryWithPermission("product") }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 7. Dodaj zdjęcie paragonu (po pozwoleniu)
            PhotoBox(
                title = "dodaj zdjęcie paragonu",
                hasImage = state.receiptImageUri != null,
                onClick = { openGalleryWithPermission("receipt") }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Komunikat błędu (jeśli wystąpił)
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 8. Przycisk zapisu (+ fioletowy okrąg)
            IconButton(
                onClick = {
                    viewModel.saveExpense(onSuccess = onNavigateBack)
                },
                modifier = Modifier
                    .size(64.dp)
                    .background(purple, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zapisz wydatek",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    // Mniejszy kalendarz do wyboru daty
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            modifier = Modifier.scale(0.85f),
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val selectedLocalDate = Instant.ofEpochMilli(selectedMillis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                            val formattedDate = selectedLocalDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            viewModel.onDateChanged(formattedDate)
                        }
                        showDatePickerDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Anuluj")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}

@Composable
private fun AlignLeftContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        content()
    }
}

@Composable
private fun WhiteInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(4.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.LightGray,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    readOnly = readOnly,
                    enabled = onClick == null,
                    keyboardOptions = keyboardOptions,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                )
            }
            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}

@Composable
private fun PhotoBox(
    title: String,
    hasImage: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(width = 170.dp, height = 110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = if (hasImage) "zdjęcie dodane ✓" else title,
                color = if (hasImage) Color(0xFF2E7D32) else Color.LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
