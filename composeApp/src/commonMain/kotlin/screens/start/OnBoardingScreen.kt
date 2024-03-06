package screens.start

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import elements.CustomText
import elements.PageIndicator
import getResourceFile
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import lightTextMain
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import setBlendModeClear
import setBlendModeDst
import setMaskFilterBlur
import utils.MessageShape
import utils.StarShape
import viewmodel.UserDataViewModel
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(
    ExperimentalResourceApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun OnBoardingScreen(
    viewModel: UserDataViewModel = koinInject(),
    navigateToEnterEmail: () -> Unit
) {
    val user = viewModel.userFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { viewModel.getOnBoardingsArray().size }

    val scaleState by remember {
        derivedStateOf {
            (1f + abs((getScale(pagerState.currentPage) * 2 * pagerState.currentPageOffsetFraction)))
                .coerceAtLeast(1f + (getScaleReverse(pagerState.currentPage)))
        }
    }
    val rotationState by remember {
        derivedStateOf {
            -1 * (15f - abs(15f * pagerState.getOffsetFractionForPage(0)))
        }
    }

    Scaffold(
        Modifier.fillMaxSize().systemBarsPadding(),
        topBar = { TopAppBarOnBoarding(navigateToEnterEmail) },
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().padding(16.dp, 0.dp, 16.dp, 18.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                PageIndicator(
                    pagerState.pageCount,
                    Modifier,
                    pagerState.currentPage,
                    animationDurationInMillis = 300
                )

                Card(
                    {
                        if (pagerState.currentPage != pagerState.pageCount - 1)
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        else
                            navigateToEnterEmail()
                    },
                    Modifier
                        .clip(CircleShape)
                        .shadow(
                            20.dp,
                            CircleShape,
                            ambientColor = Color.White,
                            spotColor = Color.White
                        )
                        .gradientBackground(listOf(Color.White.copy(0.4f), Color.White), 315f)
                        .innerShadow(
                            Color.White,
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            spread = 0.dp,
                            blur = 8.dp
                        ),
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    elevation = 0.dp,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, null)
                }
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.primary,
    ) {
        BoxWithConstraints(
            Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier.fillMaxSize(0.9f).aspectRatio(1f)
                    ) {
                        Image(
                            painterResource("images/Rectangle.png"),
                            "",
                            modifier = Modifier.fillMaxSize()
                                .scale(scaleState)
                                .rotate(rotationState)
                                .clip(
                                    StarShape(
                                        14,
                                        with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() * 0.9f })
                                )
                        )
                        if (pagerState.currentPage == 1)
                            Image(
                                painterResource("images/logo.xml"),
                                "",
                                modifier = Modifier.fillMaxSize()
                                    .scale(1.3f)
                                    .padding(30.dp)
                                    .alpha(1 - abs(pagerState.getOffsetFractionForPage(1)))
                                    .rotate(45f * pagerState.getOffsetFractionForPage(1))
                                    .graphicsLayer {
                                        translationX =
                                            this@BoxWithConstraints.maxWidth.toPx() * 0.9f / 2 * ((
                                                    pagerState.getOffsetFractionForPage(1)
                                                    ))
                                    }
                                    .clip(
                                        StarShape(
                                            14,
                                            with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() * 0.9f })
                                    )
                            )
                    }
                }
            }

            if (pagerState.currentPage == 1) {
                Box(Modifier.fillMaxSize(), Alignment.TopEnd) {
                    Card(
                        Modifier
                            .graphicsLayer {
                                translationX =
                                    this@BoxWithConstraints.maxWidth.toPx() * 0.9f / 2 * abs(
                                        (
                                                pagerState.getOffsetFractionForPage(1)
                                                )
                                    )
                            }

                            .alpha(1 - abs(pagerState.getOffsetFractionForPage(1)))
                            .rotate(30f - 60f * abs(pagerState.getOffsetFractionForPage(1)))
                            .padding(40.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .gradientBackground(
                                listOf(Color.White, Color.White.copy(0.8f)),
                                275f
                            )
                            .innerShadow(),
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        elevation = 0.dp,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Column(
                            Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painterResource("icons/inventory.xml"),
                                null,
                                tint = lightTextMain
                            )
                            CustomText(
                                "База\nзнаний ${user.value.data?.fullName ?: "test"}",
                                Modifier,
                                MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Black),
                                TextAlign.Center,
                            )
                        }
                    }
                }

                Box(Modifier.fillMaxSize(), Alignment.CenterStart) {
                    Card(
                        Modifier
                            .minimumInteractiveComponentSize()
                            .graphicsLayer {
                                translationX =
                                    this@BoxWithConstraints.maxWidth.toPx() * 0.9f / 2 * -abs(
                                        (
                                                pagerState.getOffsetFractionForPage(1)
                                                )
                                    )
                            }
                            .alpha(1 - abs(pagerState.getOffsetFractionForPage(1)))
                            .rotate(-20f + 40f * abs(pagerState.getOffsetFractionForPage(1)))
                            .padding(20.dp)
                            .padding(top = with(LocalDensity.current) { this@BoxWithConstraints.maxHeight * 0.1f })
                            .clip(RoundedCornerShape(30.dp))
                            .gradientBackground(
                                listOf(Color.White, Color.White.copy(0.8f)),
                                275f
                            )
                            .innerShadow(),
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        elevation = 0.dp,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Column(
                            Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painterResource("icons/supervisor.xml"),
                                null,
                                Modifier.wrapContentSize().defaultMinSize(30.dp, 30.dp),
                                tint = lightTextMain
                            )
                            CustomText(
                                "Онбординг",
                                Modifier,
                                MaterialTheme.typography.button.copy(fontWeight = FontWeight.Black),
                                TextAlign.Center,
                            )
                        }
                    }
                }

                Box(Modifier.fillMaxSize(), Alignment.BottomEnd) {
                    Card(
                        Modifier
                            .minimumInteractiveComponentSize()
                            .graphicsLayer {
                                translationX =
                                    this@BoxWithConstraints.maxWidth.toPx() * 0.9f / 2 * abs(
                                        pagerState.getOffsetFractionForPage(1)
                                    )
                            }
                            .alpha(1 - abs(pagerState.getOffsetFractionForPage(1)))
                            .rotate(22.5f - 45f * abs(pagerState.getOffsetFractionForPage(1)))
                            .padding(40.dp)
                            .padding(bottom = with(LocalDensity.current) { this@BoxWithConstraints.maxHeight * 0.1f })
                            .clip(RoundedCornerShape(30.dp))
                            .gradientBackground(
                                listOf(Color.White, Color.White.copy(0.8f)),
                                275f
                            )
                            .innerShadow(),
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        elevation = 0.dp,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Column(
                            Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painterResource("icons/chat_unread.xml"),
                                null,
                                Modifier.wrapContentSize().defaultMinSize(30.dp, 30.dp),
                                tint = lightTextMain
                            )
                            CustomText(
                                "Умный\nпомощник",
                                Modifier,
                                MaterialTheme.typography.button.copy(fontWeight = FontWeight.Black),
                                TextAlign.Center,
                            )
                        }
                    }
                }
            }

            HorizontalPager(pagerState, beyondBoundsPageCount = 1) {
                viewModel.getOnBoardingsArray()[it].let { onBoarding ->
                    Column(Modifier.fillMaxSize()) {
                        Box(
                            Modifier.fillMaxWidth(),
                            if (it == 1) Alignment.BottomStart else Alignment.TopStart
                        ) {
                            WordCard(
                                onBoarding.texts
                            )
                        }

                        Box(Modifier.fillMaxWidth().weight(1f)) {
                            OnBoardingItem(onBoarding.drawable)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun OnBoardingItem(onBoarding: String) {
    var file: File? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        file = getResourceFile(onBoarding)
    }

    Column(Modifier.fillMaxSize()) {
        file?.let { it ->
            val painterResource = asyncPainterResource(it)
            KamelImage(painterResource,
                contentDescription = "Compose",
                Modifier.weight(1f).padding(bottom = 4.dp),
                onFailure = { throw it })
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopAppBarOnBoarding(navigateToAuthentication: () -> Unit) {
    TopAppBar(Modifier.fillMaxWidth(), elevation = 0.dp) {
        Box(Modifier.fillMaxWidth()) {
            IconButton({ navigateToAuthentication() }, Modifier.align(Alignment.CenterStart)) {
                Icon(painterResource("icons/close_filled.xml"), "", tint = Color.White)
            }
            Image(
                painterResource("images/logo.xml"),
                "",
                Modifier.align(Alignment.Center)
            )
        }
    }
}

fun getScale(page: Int) = when (page % 2) {
    0 -> 0.3f
    else -> 0f
}

fun getScaleReverse(page: Int) = when (page % 2) {
    1 -> 0.3f
    else -> 0f
}

@Composable
fun WordCard(texts: List<AnnotatedString>) {
    Card(
        Modifier
            .padding(18.dp)
            .clip(with(LocalDensity.current) {
                MessageShape(
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
            MessageShape(
                30.dp.toPx(),
                1.dp.toPx(),
                17.dp.toPx()
            )
        }
    ) {
        LazyColumn(
            Modifier.padding(24.dp).padding(bottom = 17.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(texts) {
                CustomText(
                    it,
                    Modifier,
                    MaterialTheme.typography.button
                )
            }
        }
    }
}

fun Modifier.innerShadow(
    color: Color = Color.White,
    cornersRadius: Dp = 30.dp,
    spread: Dp = 4.dp,
    blur: Dp = 10.dp,
    offsetY: Dp = 5.dp,
    offsetX: Dp = 5.dp
) = drawWithContent {
    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    drawIntoCanvas {
        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)
        it.drawRoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.setBlendModeDst()
        if (blur.toPx() > 0) {
            frameworkPaint.setMaskFilterBlur(blur.toPx())
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black
        it.drawRoundRect(
            left = left + spread.toPx() / 2,
            top = top + spread.toPx() / 2,
            right = right - spread.toPx() / 2,
            bottom = bottom - spread.toPx() / 2,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        frameworkPaint.setBlendModeClear()
        frameworkPaint.maskFilter = null
    }
}

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) = this.then(
    Modifier.drawBehind {
        val angleRad = angle * PI / 180f
        val x = cos(angleRad).toFloat() //Fractional x
        val y = sin(angleRad).toFloat() //Fractional y

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width, size.height) - exactOffset,
                end = exactOffset
            ),
            size = size
        )
    }
)