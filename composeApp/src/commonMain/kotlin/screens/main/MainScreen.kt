package screens.main

import Routes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomText
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import util.Resource
import viewmodel.UserDataViewModel

@Composable
fun MainNavigation(userDataViewModel: UserDataViewModel = koinInject()) {
    val navigator = rememberNavigator()
    val currentEntry = navigator.currentEntry.collectAsState(null).value?.route?.route.orEmpty()

    Scaffold(contentColor = Color.Transparent, backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            GetTopBar(
                currentEntry,
                navigator::popBackStack,
                { navigator.navigate(Routes.Chat.name) },
                userDataViewModel
            )
        }
    ) {
        NavHost(
            navigator = navigator,
            navTransition = NavTransition(),
            initialRoute = Routes.Home.name
        ) {
            scene(Routes.Home.name) {
                HomeScreen(
                    { navigator.navigate(Routes.Track.name) },
                    { navigator.navigate(Routes.Chat.name) }
                )
            }

            scene(Routes.Track.name) {
                TracksScreen { navigator.navigate(Routes.Module.name) }
            }

            scene(Routes.Module.name) {
                ModulesScreen()
            }

            scene(Routes.Chat.name) {
                ChatScreen()
            }
        }
    }
}

@Composable
fun GetTopBar(
    route: String,
    navigateToBack: () -> Unit,
    navigateToChat: () -> Unit,
    viewModel: UserDataViewModel
) =
    when (route) {
        Routes.Home.name -> TopAppBarHome(navigateToBack)
        Routes.Track.name -> TopAppBarBack(navigateToBack)
        Routes.Module.name -> TopAppBarModule(navigateToBack, navigateToChat, viewModel)
        Routes.Chat.name -> TopAppBarChat(navigateToBack)
        else -> TopAppBarHome(navigateToBack)
    }

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopAppBarHome(
    navigateToBack: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
            Box(Modifier.fillMaxWidth()) {
                IconButton({ navigateToBack() }, Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        painterResource("icons/profile.xml"),
                        "",
                        Modifier,
                        Color.White
                    )
                }
                Image(
                    painterResource("images/logo.xml"),
                    "",
                    Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopAppBarBack(
    navigateToBack: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
            Box(Modifier.fillMaxWidth()) {
                IconButton({ navigateToBack() }, Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        "",
                        Modifier.padding(vertical = 5.dp).fillMaxHeight().aspectRatio(1f),
                        Color.White
                    )
                }
                Image(
                    painterResource("images/logo.xml"),
                    "",
                    Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopAppBarModule(
    navigateToBack: () -> Unit, navigateToChat: () -> Unit, userDataViewModel: UserDataViewModel
) {
    val module = userDataViewModel.getModule().collectAsState(Resource.Loading())
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                IconButton({ navigateToBack() }) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        "",
                        Modifier.padding(vertical = 5.dp).fillMaxHeight().aspectRatio(1f),
                        Color.White
                    )
                }

                CustomText(
                    "Уровень ${module.value.data?.idTrack?.plus(1)}: ${module.value.data?.name}",
                    Modifier,
                    MaterialTheme.typography.button.copy(fontWeight = FontWeight.SemiBold),
                    TextAlign.Center,
                    Color.White
                )

                Image(
                    painterResource("images/helper_module.png"),
                    null,
                    Modifier.fillMaxHeight().aspectRatio(1f).border(2.dp, Color.White, CircleShape)
                        .padding(2.dp).clip(CircleShape).clickable {
                            navigateToChat()
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopAppBarChat(
    navigateToBack: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                IconButton({ navigateToBack() }) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        "",
                        Modifier.padding(vertical = 5.dp).fillMaxHeight().aspectRatio(1f),
                        Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.padding(vertical = 20.dp).padding(end = 4.dp).fillMaxHeight()
                            .aspectRatio(1f).background(Color(0xFF84F690), CircleShape)
                    )
                    CustomText(
                        "Никита",
                        Modifier,
                        MaterialTheme.typography.button.copy(fontWeight = FontWeight.SemiBold),
                        TextAlign.Center,
                        Color.White
                    )
                }

                Image(
                    painterResource("images/helper_module.png"),
                    null,
                    Modifier.fillMaxHeight().aspectRatio(1f).padding(2.dp).border(2.dp, Color.White, CircleShape)
                        .padding(2.dp)
                )
            }
        }
    }
}