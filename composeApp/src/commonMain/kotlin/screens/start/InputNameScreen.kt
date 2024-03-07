package screens.start

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.AnimatedCustomButton
import elements.CustomButton
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
fun InputNameScreen(
    navigateBack: () -> Unit,
    navigateToFinish: () -> Unit,
    viewModel: UserDataViewModel = koinInject()
) {
    val imageUser = viewModel.userImageFlow.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val name = viewModel.nameFlow.collectAsState()
    val lastname = viewModel.lastnameFlow.collectAsState()
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
        topBar = { TopAppBarEnterAuthorization(navigateBack) }
    ) {
        Column(
            Modifier.fillMaxSize().padding(it).padding(horizontal = 16.dp, vertical = 28.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomText(
                "Выбери себе аватар",
                Modifier.fillMaxWidth().padding(bottom = 50.dp),
                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
                TextAlign.Center,
                Color.White
            )

            Box(Modifier.padding(bottom = 24.dp)) {
                Box(
                    Modifier.fillMaxWidth(0.4f).aspectRatio(1f).clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape).padding(8.dp)
                ) {
                    AnimatedContent(imageUser.value) {
                        Image(
                            painterResource(it.drawable),
                            null,
                            Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            CustomOutlinedTextField(
                "Имя",
                name,
                viewModel::setNewName,
            )

            CustomOutlinedTextField(
                "Фамилия",
                lastname,
                viewModel::setNewLastname,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            Spacer(Modifier.weight(1f))

            AnimatedCustomButton(buttonText, Modifier.fillMaxWidth()){
                viewModel.registrationUser()
            }
        }
    }

    if (user.value != null && user.value is Resource.Success){
        scope.launch(Dispatchers.IO) {
            delay(300)
            navigateToFinish()
        }
    }
}