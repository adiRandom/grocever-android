package app.adi_random.dealscraper.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.data.models.bottomSheet.ReportBottomSheetModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportModalContent(model: ReportBottomSheetModel) {

    if (model.ocrProductNames.isEmpty()) {
        return Box(modifier = Modifier.fillMaxSize())
    }

    var selectedOcrProduct by remember { mutableStateOf(model.ocrProductNames.first()) }

    LaunchedEffect(model.ocrProductNames){
        selectedOcrProduct = model.ocrProductNames.first()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Please select the purchased product that doesn't fit with the suggested alternative",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            var isExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
            ) {
                TextField(
                    value = selectedOcrProduct,
                    onValueChange = {},
                    label = { Text(text = "Purchased Product") },
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
                    model.ocrProductNames.forEach { ocrProductName ->

                        DropdownMenuItem(onClick = {
                            selectedOcrProduct = ocrProductName
                            isExpanded = false
                        }) {
                            Text(text = ocrProductName)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                model.onReport(selectedOcrProduct)
            },
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            Text(text = "Report")
        }
    }
}