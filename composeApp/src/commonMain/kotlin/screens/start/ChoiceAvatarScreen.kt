package screens.start

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomButton
import elements.CustomText
import model.Avatar
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import viewmodel.UserDataViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChoiceAvatarScreen(
    navigateBack: () -> Unit,
    navigateToInputName: () -> Unit,
    userDataViewModel: UserDataViewModel = koinInject()
) {
    val imageUser = userDataViewModel.userImageFlow.collectAsState()
    val scrollState = rememberScrollState()

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

            LazyVerticalGrid(
                GridCells.Fixed(4),
                Modifier.fillMaxWidth().weight(1f).padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                items(Avatar.entries.toTypedArray(), { it.ordinal }) {
                    ItemAvatar(it.drawable, it == imageUser.value) {
                        userDataViewModel.setUserImage(it)
                    }
                }
            }

            CustomButton("Далее", Modifier.fillMaxWidth()) {
                navigateToInputName()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemAvatar(drawable: String, isCurrent: Boolean, updateChoice: () -> Unit) {
    Image(
        painterResource(drawable),
        null,
        Modifier.fillMaxWidth().aspectRatio(1f).clip(CircleShape).clickable {
            updateChoice()
        },
        contentScale = ContentScale.Crop
    )

    AnimatedVisibility(isCurrent, enter = enterAnimation, exit = exitAnimation) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Black.copy(0.6f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource("icons/check_circle.xml"),
                null,
                Modifier.padding(16.dp).fillMaxWidth().aspectRatio(1f)
            )
        }
    }
}

val enterAnimation = fadeIn()
val exitAnimation = fadeOut()