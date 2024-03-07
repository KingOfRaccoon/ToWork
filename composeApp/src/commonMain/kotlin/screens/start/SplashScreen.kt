package screens.start

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import util.Resource
import utils.StarShape
import viewmodel.UserDataViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(
    navigateToOnBoarding: () -> Unit,
    navigateToMain: () -> Unit,
    navigateToEnterEmail: () -> Unit,
    viewModel: UserDataViewModel = koinInject()
) {
    val user = viewModel.userFlow.collectAsState()
    val fractionWidth = remember { mutableFloatStateOf(0.1f) }

    LaunchedEffect(Unit){
        viewModel.authUserOnToken()
    }

    LaunchedEffect(Unit) {
        animate(0.1f, 1f, animationSpec = tween(1500)) { value, _ ->
            fractionWidth.value = value
        }
    }

    Column(
        Modifier.fillMaxSize().systemBarsPadding().background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BoxWithConstraints(Modifier.fillMaxWidth(0.9f), contentAlignment = Alignment.Center) {
            Image(
                painterResource("images/logo.xml"),
                "",
                Modifier.fillMaxSize().scale(fractionWidth.value)
            )
            Image(
                painterResource("images/Rectangle.png"),
                "",
                modifier = Modifier.fillMaxSize().aspectRatio(1f)
                    .scale(1f - fractionWidth.value)
                    .clip(
                        StarShape(
                            14,
                            with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() })
                    )
            )
        }
    }

    if (fractionWidth.value == 1f) {
        if (viewModel.isTokenEmpty())
            navigateToOnBoarding()
        else
            if (user.value is Resource.Success)
                navigateToMain()
            else if (user.value is Resource.Error)
                navigateToEnterEmail()
    }
}