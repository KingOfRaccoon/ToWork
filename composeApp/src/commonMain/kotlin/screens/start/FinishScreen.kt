package screens.start

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomButton
import elements.CustomText
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import viewmodel.UserDataViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FinishScreen(viewModel: UserDataViewModel = koinInject(), navigateToGoToMain: () -> Unit) {
    val typesUsers = viewModel.typesUsersFlow.collectAsState()

    Scaffold(Modifier.fillMaxSize().systemBarsPadding(),
        topBar = { TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) { } }) {
        Column(Modifier.fillMaxSize().padding(it).padding(16.dp, 28.dp)) {
            CustomText(
                "На каком этапе ты?",
                Modifier.fillMaxWidth().padding(bottom = 16.dp),
                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
                TextAlign.Center,
                Color.White
            )

            CustomText(
                "В дальнейшем ты сможешь сменить этап в настройках",
                Modifier.fillMaxWidth().padding(bottom = 29.dp),
                MaterialTheme.typography.button,
                TextAlign.Center,
                Color.White
            )

            LazyColumn(
                Modifier.fillMaxWidth().padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(typesUsers.value, { _, item -> item.name }) { index, item ->

                    Card(
                        { if (index == 0) viewModel.updateTypesUsersState(item) },
                        Modifier.fillMaxWidth(),
                        enabled = index == 0,
                        shape = CircleShape,
                        backgroundColor = if (item.state && index == 0) Color.White else Color.White.copy(
                            0.8f
                        )
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp).padding(start = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                CustomText(
                                    item.name, textStyle = MaterialTheme.typography.h3
                                )
                                CustomText(
                                    item.desc,
                                    textStyle = MaterialTheme.typography.subtitle1,
                                    textColor = MaterialTheme.colors.onBackground.copy(0.6f)
                                )
                            }

                            CircleCheckbox(
                                item.state && index == 0,
                                index == 0
                            ) { viewModel.updateTypesUsersState(item) }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(typesUsers.value.any { it.state }) {
                CustomButton("Далее", Modifier.fillMaxWidth()){
                    navigateToGoToMain()
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CircleCheckbox(selected: Boolean, enabled: Boolean = true, onChecked: () -> Unit) {
    val imageVector =
        if (selected) painterResource("icons/check_circle.xml") else painterResource("icons/circle.xml")
    val tint =
        if (selected) Color(0xFF63EA70).copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (selected) Color.White else Color.Transparent

    IconButton(
        onClick = { onChecked() },
        modifier = Modifier.fillMaxWidth(0.1f).aspectRatio(1f),
        enabled = enabled
    ) {
        AnimatedContent(imageVector) {
            Icon(
                it, tint = tint,
                modifier = Modifier.background(background, shape = CircleShape).wrapContentSize(),
                contentDescription = "checkbox"
            )
        }
    }
}