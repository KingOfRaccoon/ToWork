package elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebViewElement(modifier: Modifier, html: String)