package app.adi_random.dealscraper.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProductBottomDrawerContent(
    unitStringResList: List<Int> = emptyList(),
    stores: List<StoreMetadataModel> = emptyList(),
    onSubmit: (model: ManualAddProductModel) -> Unit,
    pickImage: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var productQty by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }

    var productUnit by remember { mutableStateOf(null as String?) }
    val defaultUniLabel = stringResource(R.string.manually_add_unit_buc)
    val selectedUnitLabel by remember {
        derivedStateOf {
            productUnit ?: defaultUniLabel
        }
    }

    var selectedStoreId by remember { mutableStateOf(null as Int?) }
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


    Text(
        text = stringResource(id = R.string.manually_add),
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
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
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
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            enabled = isFormValid,
            onClick = {
                onSubmit(
                    ManualAddProductModel(
                        productName,
                        productPrice.toFloat(),
                        productQty.toFloat(),
                        productUnit ?: "",
                        selectedStoreId ?: 0
                    )
                )
            },
            modifier = Modifier
                .padding(12.dp, 0.dp, 12.dp, 24.dp)
        ) {
            Text(text = stringResource(id = R.string.manually_add_btn))
        }

        TextButton(onClick = pickImage) {
            Text(text = stringResource(id = R.string.manually_add_img_btn))
        }
    }
}