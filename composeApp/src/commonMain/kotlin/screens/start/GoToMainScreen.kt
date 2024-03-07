package screens.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomButton
import elements.CustomText
import getResourceFile
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun GoToMainScreen(navigateToMain: () -> Unit) {
    var file: File? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        file = getResourceFile("images/goToMain.png")
    }

    Scaffold(Modifier.fillMaxSize().systemBarsPadding(),
        topBar = { TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) { } }) {
        Column(Modifier.fillMaxSize().padding(it).padding(16.dp, 28.dp)) {
            CustomText(
                "Готов начать?",
                Modifier.fillMaxWidth().padding(bottom = 16.dp),
                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
                TextAlign.Center,
                Color.White
            )

            CustomText(
                "Советую начать пораньше, подготовка документов может затянуться...",
                Modifier.fillMaxWidth().padding(bottom = 29.dp),
                MaterialTheme.typography.button,
                TextAlign.Center,
                Color.White
            )

            Column(Modifier.fillMaxWidth().weight(1f)) {
                file?.let { it ->
                    val painterResource = asyncPainterResource(it)
                    KamelImage(painterResource,
                        contentDescription = "Compose",
                        Modifier.weight(1f).padding(bottom = 24.dp),
                        onFailure = { throw it })
                }
            }
            CustomButton("Погнали!", Modifier.fillMaxWidth()) {
                navigateToMain()
            }
        }
    }
}
