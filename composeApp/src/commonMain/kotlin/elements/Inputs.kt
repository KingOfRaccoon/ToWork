package elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import textSecondary

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OutlinedTextFieldValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    error: String = "",
    isError: Boolean = error.isNotEmpty(),
    trueTrailingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        if (error.isNotEmpty())
            Icon(painterResource("icons/error.xml"), null, tint = MaterialTheme.colors.error)
        else
            if (trueTrailingIcon != null) {
                trueTrailingIcon()
            }
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(16.dp),
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = Color.Black
    )
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )
        if (error.isNotEmpty()) {
            CustomText(
                text = error,
                textColor = MaterialTheme.colors.error,
                textStyle = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
            )
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    label: String,
    valueState: State<String>,
    actionValueChange: (String) -> Unit,
    valueErrorState: State<String?>? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = if (valueErrorState?.value == null) TextFieldDefaults.outlinedTextFieldColors(
        textColor = Color.White,
        cursorColor = Color.White,
        disabledTextColor = Color.White,
        unfocusedBorderColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedLabelColor = Color.White,
        disabledBorderColor = textSecondary,
        disabledLabelColor = textSecondary,
        disabledPlaceholderColor = textSecondary,
        disabledLeadingIconColor = textSecondary,
        disabledTrailingIconColor = textSecondary
    ) else TextFieldDefaults.outlinedTextFieldColors(
        textColor = MaterialTheme.colors.error,
        cursorColor = Color.White,
        disabledTextColor = Color.White,
        unfocusedBorderColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedLabelColor = Color.White,
        disabledBorderColor = textSecondary,
        disabledLabelColor = textSecondary,
        disabledPlaceholderColor = textSecondary,
        disabledLeadingIconColor = textSecondary,
        disabledTrailingIconColor = textSecondary
    )

) {
        OutlinedTextFieldValidation(
            value = valueState.value,
            onValueChange = actionValueChange,
            label = { Text(label) },
            enabled = enabled,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            isError = valueErrorState?.value != null,
            error = valueErrorState?.value.orEmpty(),
            visualTransformation = visualTransformation,
            singleLine = true,
            trueTrailingIcon = icon,
            colors = colors
        )

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PasswordIcon(
    passwordVisible: State<Boolean>,
    updatePasswordVisible: (Boolean) -> Unit
) {
    val image = if (passwordVisible.value)
        painterResource("icons/visibility_off.xml")
    else
        painterResource("icons/visibility_on.xml")

    val description = if (passwordVisible.value) "Hide password" else "Show password"

    IconButton(onClick = { updatePasswordVisible(!passwordVisible.value) }) {
        Icon(image, description, tint = MaterialTheme.colors.onBackground)
    }
}