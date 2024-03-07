package screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import elements.CustomText
import elements.WebViewElement
import lightAccent
import org.koin.compose.koinInject
import util.Resource
import viewmodel.UserDataViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ModulesScreen(viewModel: UserDataViewModel = koinInject()) {
    val module = viewModel.getModule().collectAsState(Resource.Loading())
    val currentPage = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { module.value.data?.pages?.size ?: 0 }
    Column(
        Modifier.fillMaxSize().background(Color(0xFFF9F9F9)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp),
            backgroundColor = lightAccent,
            contentColor = lightAccent,
            elevation = 0.dp
        ) {
            LazyRow(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(module.value.data?.pages?.map { it.name }.orEmpty()) { index, it ->
                    Card(
                        {},
                        shape = RoundedCornerShape(30.dp),
                        backgroundColor = if (index == currentPage.value) Color.White.copy(0.3f) else lightAccent,
                        elevation = 0.dp
                    ) {
                        CustomText(
                            it,
                            Modifier.padding(16.dp, 14.dp),
                            MaterialTheme.typography.h3,
                            textColor = Color.White
                        )
                    }
                }
            }
        }

        Column(Modifier.fillMaxWidth().padding(11.dp)) {
            HorizontalPager(pagerState) {
                module.value.data?.pages?.get(it)?.let{
                    WebViewElement(
                        Modifier.align(Alignment.CenterHorizontally), it.content
//                        "<!DOCTYPE html>\n" +
//                                "<html lang=\"ru\">\n" +
//                                "<head>\n" +
//                                "    <meta charset=\"UTF-8\">\n" +
//                                "    <title>Уровень 1: Подготовка</title>\n" +
//                                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
//                                "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">\n" +
//                                "    <link href=\"https://fonts.googleapis.com/css2?family=Montserrat&display=swap\" rel=\"stylesheet\">\n" +
//                                "    <style>\n" +
//                                "        body{\n" +
//                                "            background-color: #ffffff;\n" +
//                                "            margin: 0 auto;\n" +
//                                "            padding-left: 2%;\n" +
//                                "            padding-right: 2%;\n" +
//                                "            font-family: 'Noto Sans', sans-serif;\n" +
//                                "        }\n" +
//                                "        h1{\n" +
//                                "            text-align: left;\n" +
//                                "            color: #272727;\n" +
//                                "            font-size: 20px;\n" +
//                                "            font-style: normal;\n" +
//                                "            font-weight: 600;\n" +
//                                "            line-height: 120%;\n" +
//                                "        }\n" +
//                                "        p{\n" +
//                                "            color: #272727;\n" +
//                                "            font-size: 15px;\n" +
//                                "            font-style: normal;\n" +
//                                "            font-weight: 400;\n" +
//                                "            line-height: 130%;\n" +
//                                "        }\n" +
//                                "        img{\n" +
//                                "            margin: 0 5%;\n" +
//                                "        }\n" +
//                                "    </style>\n" +
//                                "</head>\n" +
//                                "<body>\n" +
//                                "    <h1>Поздравляю!</h1>\n" +
//                                "    <p>Ты справился с первым уровнем! </p>\n" +
//                                "<a href=\"https://imageup.ru/img19/4757980/finish.jpg.html\" target=\"_blank\">" +
//                                "<img width=\"200px\" src=\"https://imageup.ru/img19/4757980/finish.jpg\" border=\"0\" ></a>" +
////                    "    <img  src=\"finish.png\">\n" +
//                                "</body>\n" +
//                                "</html>"
                    )
                }
            }
        }
    }
}
