package screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elements.CustomOutlinedTextField
import elements.CustomText
import lightAccent
import lightTextMain
import model.ChatMessage
import org.koin.compose.koinInject
import textSecondary
import util.Resource
import viewmodel.UserDataViewModel

@Composable
fun ChatScreen(viewModel: UserDataViewModel = koinInject()) {
    val message = viewModel.messageText.collectAsState()
    val chat = viewModel.messagesFlow.collectAsState()
    Column(
        Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(16.dp),
        Arrangement.SpaceBetween,
    ) {
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(chat.value) {
                if (it.data?.isUser == true)
                    UserMessage(it)
                else
                    BotMessage(it)
            }
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp), Alignment.CenterVertically) {
            CustomOutlinedTextField(
                "",
                message,
                viewModel::setMessage,
                modifier = Modifier.weight(8.4f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = lightTextMain,
                    disabledBorderColor = lightAccent,
                    focusedBorderColor = lightAccent,
                    unfocusedBorderColor = lightAccent,
                    cursorColor = lightAccent
                )
            )
            Image(
                Icons.Default.Send,
                null,
                Modifier.weight(1.6f).padding(top = 6.dp).aspectRatio(1f)
                    .background(lightAccent, CircleShape)
                    .padding(16.dp).clip(CircleShape).clickable {
                        viewModel.sendMessage()
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun BotMessage(message: Resource<ChatMessage>) {
    Row(Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(30.dp, 30.dp, 10.dp, 30.dp),
            backgroundColor = Color.White,
            contentColor = Color.White
        ) {
            CustomText(
                getMessage(message),
                Modifier.padding(16.dp, 12.dp),
                MaterialTheme.typography.button,
                textColor = getColor(message)
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun UserMessage(message: Resource<ChatMessage>) {
    Row(Modifier.fillMaxWidth()) {
        Spacer(Modifier.weight(1f))
        Card(
            shape = RoundedCornerShape(30.dp, 30.dp, 10.dp, 30.dp),
            backgroundColor = Color.White,
            contentColor = Color.White
        ) {
            CustomText(
                message.data?.message.orEmpty(),
                Modifier.padding(16.dp, 12.dp),
                MaterialTheme.typography.button,
                TextAlign.End,
                lightTextMain
            )
        }
    }
}

fun getMessage(message: Resource<ChatMessage>) = when (message) {
    is Resource.Error -> "Попробуйте снова"
    is Resource.Loading -> "Никита думает..."
    is Resource.Success -> message.data.message
}

fun getColor(message: Resource<ChatMessage>) = when (message) {
    is Resource.Error -> lightTextMain
    is Resource.Loading -> textSecondary
    is Resource.Success -> lightTextMain
}