package screens.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import elements.CustomText
import lightAccent
import screens.start.gradientBackground
import screens.start.innerShadow
import utils.EndMessageShape

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Column(Modifier.fillMaxSize().verticalScroll(scrollState).background(Color(0xFFF9F9F9))) {
        Box(
            Modifier.fillMaxWidth().animateContentSize()
                .clip(RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp)).background(lightAccent)
        ) {
            Card(
                Modifier.fillMaxWidth(0.75f)
                    .padding(24.dp)
                    .clip(with(LocalDensity.current) {
                        EndMessageShape(
                            30.dp.toPx(),
                            1.dp.toPx(),
                            17.dp.toPx()
                        )
                    })
                    .gradientBackground(listOf(Color.White, Color.White.copy(0.8f)), 275f)
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

        }
    }
}