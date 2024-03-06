import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.core.utils.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

expect suspend fun loadFont(): Font


//@Composable
expect fun NativePaint.setBlendModeDst()
expect fun NativePaint.setBlendModeClear()
expect fun NativePaint.setMaskFilterBlur(blur: Float)
expect suspend fun getResourceFile(fileResourcePath: String): File

@Composable
internal fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    var notoSans by remember { mutableStateOf<FontFamily?>(null) }

    if (notoSans == null)
        CoroutineScope(Dispatchers.Unconfined).launch {
            notoSans = FontFamily(loadFont())
        }

    val colors = when {
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    val typography = Typography(
        notoSans ?: FontFamily.SansSerif,
        h1 = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        ),
        button = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        body1 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        ),
        subtitle1 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        ),
        caption = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(100.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

val lightBackgroundMain = Color(0xFFF4FAFF)
val lightTextMain = Color(0xFF272727)
val lightAccent = Color(0xFF4F8BFF)
val lightSecondary = Color(0xFFDDE6FF)

val darkBackgroundMain = Color(0xFF0F0F0F)
val darkTextMain = Color.White
val darkAccent = Color.White
val darkSecondary = Color(0xFF2B2C34)
val darkOnCard = Color(0xFF3E404E)

val textSecondary = Color(0xFF9FADCA)
val iconSecondary = Color(0x66273469)
val hoverSecondary = Color(0x22000000)
val errorColor = Color(0xFFFF577E)
val blueMain: Color = Color(0xFF6A8BFF)

val DarkColorPalette = darkColors(
    primary = darkOnCard,
    surface = darkBackgroundMain,
    onSurface = textSecondary,
    background = lightAccent,
    onBackground = darkTextMain,
    onPrimary = darkSecondary,
    secondary = darkAccent,
    secondaryVariant = Color.White,
    primaryVariant = darkSecondary,
    onSecondary = textSecondary,
    error = errorColor
)

val LightColorPalette = lightColors(
    primary = lightAccent,
    surface = Color.White,
    onSurface = textSecondary,
    background = lightAccent,
    onBackground = lightTextMain,
    onPrimary = Color.White,
    secondary = lightAccent,
    secondaryVariant = textSecondary,
    primaryVariant = lightSecondary,
    onSecondary = iconSecondary,
    error = errorColor
)