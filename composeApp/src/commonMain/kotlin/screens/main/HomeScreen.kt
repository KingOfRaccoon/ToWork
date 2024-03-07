package screens.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elements.CustomText
import lightAccent
import model.Avatar
import model.Knowledge
import model.Track
import model.UserWithProgress
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import screens.start.gradientBackground
import screens.start.innerShadow
import utils.EndMessageShape
import viewmodel.UserDataViewModel

@Composable
fun HomeScreen(
    navigateToTrack: () -> Unit,
    navigateToChat: () -> Unit,
    viewModel: UserDataViewModel = koinInject()
) {
    val scrollState = rememberScrollState()
    val tracks = viewModel.tracksFlow.collectAsState()
    val knowledge = viewModel.knowledgeFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsersWithRating()
        viewModel.loadUsersProgress()
    }

    Column(Modifier.fillMaxSize().background(Color(0xFFF9F9F9))) {
        TopBarHome(navigateToChat)
        LazyColumn(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            trackBlock(tracks.value.data.orEmpty(), navigateToTrack, viewModel::setNewTrack)
        }
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class)
@Composable
fun TopBarHome(navigateToChat: () -> Unit) {
    Card(
        Modifier.fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp),
        backgroundColor = lightAccent,
        contentColor = lightAccent
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Card(navigateToChat,
                Modifier.weight(1f)
                    .padding(start = 24.dp, top = 24.dp, bottom = 24.dp)
                    .clip(with(LocalDensity.current) {
                        EndMessageShape(
                            30.dp.toPx(),
                            1.dp.toPx(),
                            17.dp.toPx()
                        )
                    })
                    .gradientBackground(
                        listOf(Color.White, Color.White.copy(0.8f)),
                        275f
                    )
                    .innerShadow(),
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                elevation = 0.dp,
                shape = with(LocalDensity.current) {
                    EndMessageShape(
                        30.dp.toPx(),
                        1.dp.toPx(),
                        17.dp.toPx()
                    )
                }
            ) {
                CustomText(
                    "Есть вопросы?\nЯ готов помочь, справшивай!",
                    Modifier.fillMaxWidth().padding(24.dp)
                )
            }

            Image(
                painterResource("images/main_helper.png"),
                null,
                Modifier.fillMaxWidth(0.3f).wrapContentHeight().clickable {
                    navigateToChat()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalResourceApi::class)
fun LazyListScope.trackBlock(
    tracks: List<Track>,
    navigateToTrack: () -> Unit,
    setCurrentModule: (Int) -> Unit
) {
    item {
        CustomText(
            "Треки",
            Modifier.fillMaxWidth().padding(15.dp, 12.dp, 12.dp, 0.dp),
            MaterialTheme.typography.h3.copy(fontWeight = FontWeight.SemiBold)
        )
    }

    items(tracks) {
        Card(
            { setCurrentModule(it.id); navigateToTrack() },
            Modifier.fillMaxWidth().shadow(
                elevation = 3.dp,
                spotColor = Color(0x050A0728),
                ambientColor = Color(0x050A0728),
                shape = RoundedCornerShape(30.dp)
            ),
            elevation = 0.dp,
            shape = RoundedCornerShape(30.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(Avatar.First.drawable),
                    null,
                    Modifier.weight(0.25f).aspectRatio(1f).clip(RoundedCornerShape(23.dp))
                        .padding(10.dp)
                )

                Column(Modifier.weight(0.75f), Arrangement.spacedBy(2.dp)) {
                    CustomText(it.name, textStyle = MaterialTheme.typography.button)
                    CustomText(
                        "Заполнение документов, введение",
                        textStyle = MaterialTheme.typography.button,
                        textColor = MaterialTheme.colors.onBackground.copy(0.6f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun LazyListScope.knowledgeBlock(knowledge: List<Knowledge>) {
    item {
        CustomText(
            "База знаний",
            Modifier.fillMaxWidth().padding(15.dp, 12.dp, 12.dp, 0.dp),
            MaterialTheme.typography.h3.copy(fontWeight = FontWeight.SemiBold)
        )
    }

    item {
        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(knowledge) {
                Box(
                    Modifier.fillMaxWidth(0.2f).aspectRatio(1.5f).clip(RoundedCornerShape(20.dp)),
                ) {

                    Image(
                        painterResource(Avatar.First.drawable),
                        null,
                        Modifier.wrapContentSize().padding(10.dp).align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )

                    CustomText(
                        it.name,
                        Modifier.align(Alignment.BottomStart),
                        MaterialTheme.typography.button,
                        textColor = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun LazyListScope.ratingBlock(users: List<UserWithProgress>) {
    item {
        CustomText(
            "База знаний",
            Modifier.fillMaxWidth().padding(15.dp, 12.dp, 12.dp, 0.dp),
            MaterialTheme.typography.h3.copy(fontWeight = FontWeight.SemiBold)
        )
    }

    item {
        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(users) {
                Box(
                    Modifier.fillMaxWidth(0.2f).aspectRatio(1.5f).clip(RoundedCornerShape(20.dp)),
                ) {

                    Image(
                        painterResource(Avatar.First.drawable),
                        null,
                        Modifier.wrapContentSize().padding(10.dp).align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )

                    CustomText(
                        it.fullName,
                        Modifier.align(Alignment.BottomStart),
                        MaterialTheme.typography.button,
                        textColor = Color.White
                    )
                }
            }
        }
    }
}