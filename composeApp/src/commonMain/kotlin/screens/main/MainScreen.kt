package screens.main

import Routes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lightAccent
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import viewmodel.UserDataViewModel

@Composable
fun MainNavigation(userDataViewModel: UserDataViewModel = koinInject()) {
    val navigator = rememberNavigator()

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = { TopAppBarMain(navigator::popBackStack) }
    ) {
        NavHost(
            navigator = navigator,
            navTransition = NavTransition(),
            initialRoute = Routes.Home.name
        ) {
            scene(Routes.Home.name) {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopAppBarMain(navigateToBack: () -> Unit, content: @Composable () -> Unit = {}) {
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
            Box(Modifier.fillMaxWidth()) {
                IconButton({ navigateToBack() }, Modifier.align(Alignment.CenterStart)) {
                    Icon(painterResource("icons/profile.xml"), "", tint = Color.White)
                }
                Image(
                    painterResource("images/logo.xml"),
                    "",
                    Modifier.align(Alignment.Center)
                )
            }
        }

        Box(
            Modifier.fillMaxWidth().animateContentSize()
                .clip(RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp)).background(lightAccent)
        ) {
            content()
        }
    }
}