import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.Font
import io.kamel.core.utils.File
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter
import platform.Foundation.NSBundle

@OptIn(ExperimentalResourceApi::class)
actual suspend fun loadFont(): Font {
    return androidx.compose.ui.text.platform.Font(
        "NotoSans",
        resource("fonts/NotoSans.ttf").readBytes()
    )
}

actual fun NativePaint.setMaskFilterBlur(blur: Float) {
    this.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blur / 2, true)
}

actual fun NativePaint.setBlendModeClear() {
    this.maskFilter = null
}

actual suspend fun getResourceFile(fileResourcePath: String): File {
    return File(NSBundle.mainBundle.resourcePath + "/compose-resources/" + fileResourcePath)
}