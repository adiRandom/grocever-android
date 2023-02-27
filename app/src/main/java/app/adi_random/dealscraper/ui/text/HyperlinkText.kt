package app.adi_random.dealscraper.ui.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HyperlinkText(
    text: String,
    link: String,
    modifier: Modifier = Modifier,
    fontSize: Float = 16f,
) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = fontSize.sp,
                textDecoration = TextDecoration.Underline
            ), start = 0, end = text.length
        )

        // attach a string annotation that stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = link,
            start = 0,
            end = text.length
        )

    }

// UriHandler parse and opens URI inside AnnotatedString Item in Browse
    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}