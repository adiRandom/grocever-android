package app.adi_random.dealscraper.usecase

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx().roundToInt() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }