package screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomButton
import elements.CustomOutlinedTextField
import elements.CustomText
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import viewmodel.UserDataViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegistrationScreen(
    navigateBack: () -> Unit,
    navigateToChoiceAvatar: () -> Unit,
    userDataViewModel: UserDataViewModel = koinInject(),
) {
    val buttonText = remember { mutableStateOf("Далее") }
    val password = userDataViewModel.passwordFlow.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(Modifier.fillMaxSize().systemBarsPadding(),
        topBar = { TopAppBarEnterRegistration { userDataViewModel.clearRegistration(); navigateBack() } }) {
        Column(
            Modifier.fillMaxSize().padding(it).padding(horizontal = 16.dp, vertical = 11.dp)
                .verticalScroll(scrollState),
            Arrangement.spacedBy(12.dp),
            Alignment.CenterHorizontally
        ) {
            CustomText(
                "Регистрация",
                Modifier.fillMaxWidth(),
                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
                TextAlign.Center,
                Color.White
            )

            CustomText(
                "Вы у нас впервые! Придумайте пароль, чтобы не потерять доступ к кабинету",
                Modifier.fillMaxWidth(),
                MaterialTheme.typography.button,
                TextAlign.Center,
                Color.White
            )

            Image(
                painterResource("images/enter_sticker.png"),
                null,
                Modifier.padding(horizontal = 62.dp).fillMaxWidth().aspectRatio(1f)
            )

            CustomOutlinedTextField(
                "Пароль",
                password,
                userDataViewModel::setNewPassword,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            Spacer(Modifier.weight(1f))

            CustomButton("Далее", Modifier.fillMaxWidth()) {
                navigateToChoiceAvatar()
            }
        }
    }
}

@Composable
fun TopAppBarEnterRegistration(navigateBack: () -> Unit) {
    TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
        IconButton({ navigateBack() }) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                null,
                Modifier.fillMaxHeight().padding(vertical = 9.dp).aspectRatio(1f),
                tint = Color.White
            )
        }
    }
}