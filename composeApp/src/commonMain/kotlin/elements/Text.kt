package elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextTitle(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    textAlign: TextAlign = TextAlign.Center
) {
    CustomText(text, modifier, MaterialTheme.typography.h1, textAlign)
}

@Composable
fun CustomText(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = MaterialTheme.colors.onBackground,
    minLines: Int = 1
) {
    Text(text, modifier, textColor, style = textStyle, textAlign = textAlign, minLines = minLines)
}

@Composable
fun CustomText(
    text: AnnotatedString,
    modifier: Modifier = Modifier.fillMaxWidth(),
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = MaterialTheme.colors.onBackground,
    minLines: Int = 1
) {
    Text(text, modifier, textColor, style = textStyle, textAlign = textAlign, minLines = minLines)
}

