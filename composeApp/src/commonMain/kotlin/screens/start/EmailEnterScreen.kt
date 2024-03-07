package screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.AnimatedCustomButton
import elements.CustomOutlinedTextField
import elements.CustomText
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import util.Resource
import viewmodel.UserDataViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EmailEnterScreen(
    navigateToAuthorization: () -> Unit,
    navigateToRegistration: () -> Unit,
    viewModel: UserDataViewModel = koinInject(),
) {
    val email = viewModel.emailFlow.collectAsState()
    val needRegistration = viewModel.needRegistrationFlow.collectAsState()
    val buttonText = remember {
        derivedStateOf {
            when (needRegistration) {
                is Resource.Error<*> -> "Попропбуйте снова"
                is Resource.Loading<*> -> "Ожидайте"
                is Resource.Success<*> -> "Успех!"
                else -> "Далее"
            }
        }
    }
    val scrollState = rememberScrollState()

    Scaffold(
        Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 16.dp, vertical = 11.dp),
        topBar = { TopAppBarEnterEmail() }
    ) {
        Column(
            Modifier.fillMaxSize().padding(it).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomText(
                "Авторизация",
                Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 16.dp),
                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
                TextAlign.Center,
                Color.White
            )

            CustomText(
                "Введите почту ГУАП, если она у вас есть, или личную почту",
                Modifier.fillMaxWidth().padding(bottom = 59.dp),
                MaterialTheme.typography.button,
                TextAlign.Center,
                Color.White
            )

            Image(
                painterResource("images/enter_email_sticker.png"),
                null,
                Modifier.padding(horizontal = 20.dp).padding(42.dp).fillMaxWidth().aspectRatio(1f)
            )

            CustomOutlinedTextField(
                "Email",
                email,
                viewModel::setNewEmail,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            Spacer(Modifier.weight(1f))

            AnimatedCustomButton(buttonText, Modifier.fillMaxWidth()) {
                viewModel.needRegistration()
            }
        }
    }

    if (needRegistration.value != null) {
        if (needRegistration.value is Resource.Success) {
            if (needRegistration.value?.data == true)
                navigateToRegistration().also {
                    println("navigateToRegistration")
                }
            else
                navigateToAuthorization()
        }
    }
}

@Composable
fun TopAppBarEnterEmail() {
    TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {}
}