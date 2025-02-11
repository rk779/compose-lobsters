package dev.msfjarvis.claw.common.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.Material3RichText

@Composable
fun ThemedRichText(
  text: String,
  modifier: Modifier = Modifier,
) {
  CompositionLocalProvider(
    LocalTextStyle provides MaterialTheme.typography.bodyLarge,
    LocalContentColor provides MaterialTheme.colorScheme.onBackground,
  ) {
    Material3RichText(modifier) { Markdown(text) }
  }
}
