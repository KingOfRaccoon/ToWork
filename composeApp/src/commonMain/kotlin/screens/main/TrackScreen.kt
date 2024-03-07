package screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomButton
import elements.CustomText
import lightAccent
import model.Avatar
import model.Module
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import util.Resource
import viewmodel.UserDataViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TracksScreen(viewModel: UserDataViewModel = koinInject(), navigateToModule: () -> Unit) {
    val track = viewModel.getTrack().collectAsState(Resource.Loading())

    Column(
        Modifier.fillMaxSize().background(Color(0xFFF9F9F9)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp),
            backgroundColor = lightAccent,
            contentColor = lightAccent
        ) {
            Column(
                Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(Avatar.Second.drawable),
                    null,
                    Modifier.fillMaxWidth(0.3f).aspectRatio(1f).padding(bottom = 12.dp)
                )

                CustomText(
                    "Трек ${track.value.data?.name}",
                    Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    textStyle = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    textColor = Color.White
                )

                Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), Arrangement.SpaceBetween) {
                    ItemInfo("icons/clock.xml", "5-10 дней")
                    ItemInfo(
                        "icons/games.xml",
                        "${(track.value.data?.quantityModules ?: -1)} этапа"
                    )
                    ItemInfo("icons/team.xml", "12 человек")
                }

                CustomText(
                    "Трек содержит в себе шаги, которые необходимо выполнить прежде, чем начать заполнение докуменов для отдела кадров",
                    Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                    textColor = Color.White
                )
            }
        }

        Column(Modifier.fillMaxWidth().padding(11.dp)) {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(track.value.data?.listModules.orEmpty()) {
                    ItemModule(it) { id, number ->
                        viewModel.setNewModule(id, number)
                        navigateToModule()
                    }
                }
            }

            CustomButton(
                "Получить награду",
                Modifier.fillMaxWidth().padding(bottom = 12.dp),
                track.value.data?.let { it.quantityModules == it.idLastCompleteModule } == true && false,
                lightAccent,
                Color.White
            )
            InfoRow()
        }
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class)
@Composable
fun ItemModule(module: Module, setNewModule: (Int, Int) -> Unit) {
    Card(
        { setNewModule(module.id, module.numberInTrack) },
        Modifier.fillMaxWidth().aspectRatio(2f).padding(5.dp).shadow(
            elevation = 4.dp,
            spotColor = Color(0x050A0728),
            ambientColor = Color(0x050A0728),
            shape = RoundedCornerShape(30.dp)
        ),
        shape = RoundedCornerShape(30.dp),
        elevation = 0.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.wrapContentSize().padding(24.dp, 16.dp).align(Alignment.CenterStart)) {
                CustomText(
                    module.name,
                    Modifier,
                    textStyle = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
                )

                CustomText(
                    "${module.completePages}/${module.quantityPage}",
                    Modifier,
                    textStyle = MaterialTheme.typography.subtitle1,
                    textColor = lightAccent
                )
            }

            val (image, color) = getDrawableForModule(
                module.numberInTrack,
                module.quantityPage,
                module.completePages
            )

            Image(
                painterResource(image),
                null,
                alignment = Alignment.BottomEnd,
                modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(0.4f)
                    .fillMaxHeight(0.8f),
                colorFilter = if (color != null) ColorFilter.tint(color) else null
            )
        }
    }
}

fun getDrawableForModule(
    numberInTrack: Int,
    qualityPages: Int,
    lastCompletePage: Int = 0
): Pair<String, Color?> {
//    if (lastCompletePage == qualityPages)
//        return "icons/check_circle.xml" to lightAccent

    return "images/n_$numberInTrack.xml" to null
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemInfo(icon: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(icon), null, Modifier.padding(end = 12.dp))
        CustomText(text, Modifier, MaterialTheme.typography.subtitle1, textColor = Color.White)
    }
}

@Composable
fun InfoRow() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Outlined.Info, null, Modifier.padding(end = 8.dp), lightAccent)

        CustomText(
            "Ты получишь награду, когда пройдешь все уровни трека",
            textStyle = MaterialTheme.typography.subtitle1,
            textColor = lightAccent
        )
    }
}