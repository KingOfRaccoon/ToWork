package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import repository.OnBoardingRepository
import repository.UserDataService
import util.Postman
import viewmodel.UserDataViewModel

/** Init Koin for init modules **/
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            getModules()
        )
    }

fun getModules() = listOf(
    module { single { Postman() } },
    module { single { UserDataService(get()) } },
    module { single { OnBoardingRepository(get()) } },
    module { single { UserDataViewModel(get()) } }
)

/** Realization for iOS **/
fun initKoin() = initKoin { }