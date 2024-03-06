import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
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
                SplashScreen { navigator.navigate(Routes.OnBoarding.name) }
            }

            scene(Routes.EnterEmail.name) {
                EmailEnterScreen(navigateToAuthorization = { navigator.navigate(Routes.Authorization.name) }) {
                    navigator.navigate(Routes.Registration.name)
                }
            }

            scene(Routes.OnBoarding.name) {
                OnBoardingScreen { navigator.navigate(Routes.Authorization.name) }
            }

            scene(Routes.Authorization.name) {
                AuthorizationScreen { navigator.popBackStack() }
            }

            scene(Routes.Registration.name) {
                RegistrationScreen { navigator.popBackStack() }
            }

            scene(Routes.Finish.name) {
                FinishScreen()
            }

            scene(Routes.ChoiceAvatar.name) {
                ChoiceAvatarScreen { navigator.popBackStack() }
            }

            scene(Routes.InputName.name) {
                InputNameScreen { navigator.popBackStack() }
            }

            scene(Routes.GoToMain.name) {
                GoToMainScreen()
            }
        }

        scene(Routes.Main.name) {
            MainNavigation()
        }
    }
}