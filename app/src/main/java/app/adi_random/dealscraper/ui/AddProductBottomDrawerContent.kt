package app.adi_random.dealscraper.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.EditPurchaseInstalmentModel
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.bottomSheet.AddProductBottomSheetModel
import app.adi_random.dealscraper.usecase.MapUnitTypeUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProductBottomDrawerContent(
    model: AddProductBottomSheetModel,
) {
    val (
        unitStringResList,
        stores,
        initialOcrProduct,
        onSubmit,
        pickImage
    ) = model
    var canEditName by remember { mutableStateOf(initialOcrProduct == null) }
    var productName by remember { mutableStateOf(initialOcrProduct?.name ?: "") }
    var productQty by remember { mutableStateOf(initialOcrProduct?.quantity?.toString() ?: "") }
    var productPrice by remember { mutableStateOf(initialOcrProduct?.unitPrice?.toString() ?: "") }
    var purchaseDate by remember { mutableStateOf(initialOcrProduct?.date) }

    var productUnit by remember { mutableStateOf(initialOcrProduct?.unitName) }
    val defaultUniLabel = stringResource(R.string.manually_add_unit_default)
    val selectedUnitLabel by remember {
        derivedStateOf {
            productUnit ?: defaultUniLabel
        }
    }

    var selectedStoreId by remember { mutableStateOf(initialOcrProduct?.storeId) }
    val defaultStoreLabel = stringResource(R.string.manually_add_store_label)
    val selectedStoreName by remember {
        derivedStateOf {
            stores.find { it.id == selectedStoreId }?.name ?: defaultStoreLabel
        }
    }

    val isFormValid by remember {
        derivedStateOf {
            productName.isNotBlank() && productQty.isNotBlank() && productPrice.isNotBlank() && productUnit != null && selectedStoreId != null
        }
    }

    val context = LocalContext.current


    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }.timeInMillis.run {
                purchaseDate = this / 1000
            }
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(model){
        canEditName = initialOcrProduct == null
        productName = initialOcrProduct?.name ?: ""
        productQty = initialOcrProduct?.quantity?.toString() ?: ""
        productPrice = initialOcrProduct?.unitPrice?.toString() ?: ""
        purchaseDate = initialOcrProduct?.date
        productUnit = initialOcrProduct?.unitName
        selectedStoreId = initialOcrProduct?.storeId
    }


    Text(
        text = stringResource(
            id = if (canEditName) {
                R.string.manually_add
            } else {
                R.string.manually_edit
            }
        ),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 24.dp, 12.dp, 16.dp)
    )
    TextField(
        value = productName,
        onValueChange = { productName = it },
        label = { Text(text = stringResource(id = R.string.manually_add_name_label)) },
        placeholder = {
            Text(
                text = stringResource(
                    id = R.string.manually_add_name_hint
                )
            )
        },
        textStyle = TextStyle.Default.copy(
            color = if (canEditName) {
                app.adi_random.dealscraper.ui.theme.Colors.TextPrimary
            } else {
                app.adi_random.dealscraper.ui.theme.Colors.TextSecondary
            }
        ),
        readOnly = canEditName.not(),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp)

    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text(text = stringResource(id = R.string.manually_add_price_label)) },
            placeholder = {
                Text(
                    text = stringResource(
                        id = R.string.manually_add_price_hint
                    )
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .weight(1f)
                .padding(end = 24.dp)
        )

        TextField(
            value = productQty,
            onValueChange = { productQty = it },
            label = { Text(text = stringResource(id = R.string.manually_add_quantity_label)) },
            placeholder = {
                Text(
                    text = stringResource(
                        id = R.string.manually_add_quantity_hint
                    )
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 24.dp)
        ) {
            var isExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
            ) {
                TextField(
                    value = selectedUnitLabel,
                    onValueChange = {},
                    label = { Text(text = stringResource(id = R.string.manually_add_unit_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpanded
                        )
                    },
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    unitStringResList.forEach { unitStringRes ->
                        val unitLabel = stringResource(id = unitStringRes)

                        DropdownMenuItem(onClick = {
                            productUnit = unitLabel
                            isExpanded = false
                        }) {
                            Text(text = unitLabel)
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp)
        ) {
            var isExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
            ) {
                TextField(
                    value = selectedStoreName,
                    onValueChange = {},
                    label = { Text(text = stringResource(id = R.string.manually_add_store_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpanded
                        )
                    },
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    stores.forEach { storeMetadata ->
                        DropdownMenuItem(onClick = {
                            selectedStoreId = storeMetadata.id
                            isExpanded = false
                        }) {
                            Text(text = storeMetadata.name)
                        }
                    }
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .weight(1f)

        ) {
            TextField(
                value = purchaseDate?.let { SimpleDateFormat("dd/MM/yy").format(it * 1000) }
                    ?: "",
                onValueChange = {},
                label = {
                    Text(
                        text = "Purchase Date"
                    )
                },
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24_black),
                        contentDescription = "pickDate"
                    )
                },
                readOnly = true,
                enabled = false,
                textStyle = TextStyle.Default.copy(
                    color = app.adi_random.dealscraper.ui.theme.Colors.TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        datePickerDialog.show()
                    }
            )
        }

    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isFormValid) {
                    app.adi_random.dealscraper.ui.theme.Colors.Primary
                } else {
                    app.adi_random.dealscraper.ui.theme.Colors.Primary.copy(alpha = 0.5f)
                },
                contentColor = app.adi_random.dealscraper.ui.theme.Colors.TextOnPrimary
            ),
            onClick = {
                onSubmit(
                    if (initialOcrProduct != null) {
                        EditPurchaseInstalmentModel(
                            productName,
                            productPrice.toFloat(),
                            productQty.toFloat(),
                            MapUnitTypeUseCase.map(productUnit ?: ""),
                            selectedStoreId ?: 0,
                            purchaseDate,
                            initialOcrProduct.id,
                        )
                    } else {
                        ManualAddProductModel(
                            productName,
                            productPrice.toFloat(),
                            productQty.toFloat(),
                            MapUnitTypeUseCase.map(productUnit ?: ""),
                            selectedStoreId ?: 0,
                            purchaseDate,
                        )
                    }
                )
            },
            modifier = Modifier
                .padding(12.dp, 0.dp, 12.dp, 24.dp)
        ) {
            Text(
                text = stringResource(
                    id = if (canEditName) {
                        R.string.manually_add_btn
                    } else {
                        R.string.manually_edit_btn
                    }
                )
            )
        }

        if (canEditName) {
            TextButton(onClick = pickImage) {
                Text(text = stringResource(id = R.string.manually_add_img_btn))
            }
        }
    }
}