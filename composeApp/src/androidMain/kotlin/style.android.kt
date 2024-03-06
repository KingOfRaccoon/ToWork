import android.graphics.BlendMode
import android.graphics.BlurMaskFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.text.font.Font
import io.kamel.core.utils.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import ru.skittens.towork.R
import java.io.FileOutputStream

actual suspend fun loadFont() = Font(R.font.notosans)

actual fun NativePaint.setBlendModeDst() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.blendMode = BlendMode.DST_OUT
    }
}

actual fun NativePaint.setMaskFilterBlur(blur: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)
    }
}

actual fun NativePaint.setBlendModeClear() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.blendMode = BlendMode.CLEAR
    }
}

@OptIn(ExperimentalResourceApi::class)
actual suspend fun getResourceFile(fileResourcePath: String): File {
    val file = java.io.File.createTempFile("temp", ".${fileResourcePath.substringAfterLast(".")}")
    withContext(Dispatchers.IO) {
        FileOutputStream(file).use { os ->
            val buffer = resource(fileResourcePath).readBytes()
            os.write(buffer, 0, buffer.size)
        }
    }

    return file
}