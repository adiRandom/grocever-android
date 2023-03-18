package app.adi_random.dealscraper.ui.productDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.adi_random.dealscraper.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import app.adi_random.dealscraper.ui.report.ReportModalContent
import app.adi_random.dealscraper.ui.theme.Colors
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterialApi::class,
)

@Composable
fun ProductDetails(viewModel: ProductDetailsViewModel) {
    val productInstalmentsByOcrName by viewModel.productInstalmentsByOcrName.collectAsStateWithLifecycle()
    val product by viewModel.product.collectAsStateWithLifecycle()
    val reportableOcrProducts by viewModel.reportableOrcProductNames.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    BottomDrawer(
        drawerState = drawerState,
        drawerContent = {
            ReportModalContent(
                ocrProductNames = reportableOcrProducts,
                onReport = { ocrProductName ->
                    scope.launch {
                        drawerState.close()
                    }
                    viewModel.onReport(ocrProductName)
                })
        },
    ) {
        Column(Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)) {
            Text(
                text = stringResource(id = R.string.you_bought),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp, 8.dp)
            ) {
                items(
                    items = productInstalmentsByOcrName ?: emptyList(),
                    key = { it.first }) { (ocrName, instalments) ->
                    Column(modifier = Modifier.padding(0.dp, 8.dp)) {
                        Text(
                            text = ocrName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
                        )
                        instalments.forEachIndexed { index, instalment ->
                            OcrProductInstalmentCell(
                                instalment = instalment,
                                measureUnit = instalment.unitName,
                                topRoundCorner = index == 0,
                                bottomRoundCorner = index == instalments.size - 1
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Colors.Primary, shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    val savings by viewModel.savings.collectAsStateWithLifecycle()

                    Text(
                        text = stringResource(id = R.string.product_savings),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Colors.TextOnPrimary
                    )
                    Text(
                        text = "${String.format("%.2f", savings)} RON",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.TextOnPrimary,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )

                    if (product != null) {
                        BestProductCard(product = product!!)
                    }
                }

                if (reportableOcrProducts.isNotEmpty()) {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_report_problem),
                            contentDescription = "Report",
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}