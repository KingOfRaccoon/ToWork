package elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lightAccent
import lightTextMain
import textSecondary

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    color: Color = Color.White,
    textColor: Color = lightTextMain,
    onClick: () -> Unit = {},
) {
    Button(
        onClick,
        modifier,
        enabled = isEnabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            disabledBackgroundColor = color.copy(0.09f),
            disabledContentColor = textSecondary
        )
    ) {
        CustomText(
            text,
            textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 5.dp),
            textColor = if (isEnabled) textColor else textSecondary
        )
    }
}

@Composable
fun AnimatedCustomButton(
    text: State<String>,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        onClick,
        modifier,
        enabled = isEnabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            disabledBackgroundColor = Color.White.copy(0.09f),
            disabledContentColor = textSecondary
        )
    ) {
        AnimatedContent(
            targetState = text.value,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 500))
            },
            contentAlignment = Alignment.Center
        ) { targetCount ->
            CustomText(
                targetCount,
                textStyle = MaterialTheme.typography.button,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 5.dp),
                textColor = lightTextMain
            )
        }

    }
}

@Composable
fun CustomTextButton(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    textColor: Color = lightAccent,
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick,
        modifier,
        enabled = isEnabled,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.textButtonColors(
            contentColor = lightAccent,
            disabledContentColor = textSecondary
        )
    ) {
        CustomText(
            text, textStyle = MaterialTheme.typography.button, textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 5.dp),
            textColor = if (isEnabled) textColor else textSecondary
        )
    }
}