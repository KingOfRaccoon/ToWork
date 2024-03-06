import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.text.font.Font
import io.kamel.core.utils.File
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import org.jetbrains.skia.BlendMode
import platform.Foundation.NSBundle

@OptIn(ExperimentalResourceApi::class)
actual suspend fun loadFont(): Font {
    return androidx.compose.ui.text.platform.Font(
        "NotoSans",
        resource("fonts/NotoSans.ttf").readBytes()
    )
}

actual fun NativePaint.setBlendModeDst() {
    this.blendMode = BlendMode.DST_OUT
}

actual fun NativePaint.setMaskFilterBlur(blur: Float) {
    this.maskFilter = org.jetbrains.skia.MaskFilter.makeBlur(
        org.jetbrains.skia.FilterBlurMode.NORMAL,
        blur / 2,
        true
    )
}

actual fun NativePaint.setBlendModeClear() {
    this.maskFilter = null
}

actual suspend fun getResourceFile(fileResourcePath: String): File {
    return File(NSBundle.mainBundle.resourcePath + "/compose-resources/" + fileResourcePath)
}