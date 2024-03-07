import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import screens.main.MainNavigation
import screens.start.AuthorizationScreen
import screens.start.ChoiceAvatarScreen
import screens.start.EmailEnterScreen
import screens.start.FinishScreen
import screens.start.GoToMainScreen
import screens.start.InputNameScreen
import screens.start.OnBoardingScreen
import screens.start.RegistrationScreen
import screens.start.SplashScreen

@Composable
fun App() {
    PreComposeApp {
        MyApplicationTheme {
            Scaffold(Modifier.fillMaxSize()) {
                NavController()
            }
        }
    }
}

@Composable
fun NavController() {
    val navigator = rememberNavigator()
    NavHost(
        navigator = navigator,
        navTransition = NavTransition(),
        initialRoute = Routes.Splash.name
    ) {
        group(Routes.Start.name, Routes.Splash.name) {
            scene(Routes.Splash.name) {
                SplashScreen(
                    { navigator.navigate(Routes.OnBoarding.name) },
                    { navigator.navigate(Routes.Main.name) },
                    { navigator.navigate(Routes.EnterEmail.name) }
                )
            }

            scene(Routes.OnBoarding.name) {
                OnBoardingScreen { navigator.navigate(Routes.EnterEmail.name) }
            }

            scene(Routes.EnterEmail.name) {
                EmailEnterScreen(
                    { navigator.navigate(Routes.Authorization.name) },
                    { navigator.navigate(Routes.Registration.name) }
                )
            }

            scene(Routes.Authorization.name) {
                AuthorizationScreen(
                    { navigator.popBackStack() },
                    { navigator.navigate(Routes.Main.name) })
            }

            scene(Routes.Registration.name) {
                val test = navigator.canGoBack.collectAsState(false)
                println(test)
                RegistrationScreen(
                    { navigator.popBackStack() },
                    { navigator.navigate(Routes.ChoiceAvatar.name) }
                )
            }

            scene(Routes.ChoiceAvatar.name) {
                ChoiceAvatarScreen(
                    { navigator.popBackStack() },
                    { navigator.navigate(Routes.InputName.name) })
            }

            scene(Routes.InputName.name) {
                InputNameScreen(
                    { navigator.popBackStack() },
                    { navigator.navigate(Routes.Finish.name) }
                )
            }

            scene(Routes.Finish.name) {
                FinishScreen { navigator.navigate(Routes.GoToMain.name) }
            }

            scene(Routes.GoToMain.name) {
                GoToMainScreen { navigator.navigate(Routes.Main.name) }
            }
        }

        scene(Routes.Main.name) {
            MainNavigation()
        }
    }
}