package screens.start

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.AnimatedCustomButton
import elements.CustomOutlinedTextField
import elements.CustomText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import util.Resource
import viewmodel.UserDataViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AuthorizationScreen(
    navigateBack: () -> Unit,
    navigateToMain: () -> Unit,
    viewModel: UserDataViewModel = koinInject(),
) {
    val password = viewModel.passwordFlow.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val user = viewModel.userFlow.collectAsState()
    val buttonText = remember {
        derivedStateOf {
            when (user) {
                is Resource.Error<*> -> "Попропбуйте снова"
                is Resource.Loading<*> -> "Ожидайте"
                is Resource.Success<*> -> "Успех!"
                else -> "Далее"
            }
        }
    }

    Scaffold(
        Modifier.fillMaxSize().systemBarsPadding(),
        topBar = { TopAppBarEnterAuthorization { viewModel.clearRegistration(); navigateBack() } }
    ) {
        Column(
            Modifier.fillMaxSize().padding(it).padding(horizontal = 16.dp, vertical = 11.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomText(
                "Вход",
                Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 16.dp),
                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
                TextAlign.Center,
                Color.White
            )

            CustomText(
                "Рад снова тебя видеть :)\n" +
                        "Давай продолжим наше путешествие!",
                Modifier.fillMaxWidth().padding(bottom = 59.dp),
                MaterialTheme.typography.button,
                TextAlign.Center,
                Color.White
            )

            Image(
                painterResource("images/enter_sticker.png"),
                null,
                Modifier.padding(horizontal = 20.dp).padding(42.dp).fillMaxWidth().aspectRatio(1f)
            )

            CustomOutlinedTextField(
                "Пароль",
                password,
                viewModel::setNewPassword,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            Spacer(Modifier.weight(1f))

            AnimatedCustomButton(buttonText, Modifier.fillMaxWidth()) {
                viewModel.authUser()
            }
        }
    }

    if (user.value != null && user.value is Resource.Success) {
        scope.launch(Dispatchers.IO) {
            delay(300)
            navigateToMain()
        }
    }
}

@Composable
fun TopAppBarEnterAuthorization(navigateBack: () -> Unit) {
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