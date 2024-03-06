import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import org.jetbrains.skia.BlendMode

actual fun NativePaint.setBlendModeDst() {
    this.blendMode = BlendMode.DST_OUT
}