package dev.msfjarvis.claw.common.ui.decorations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import dev.msfjarvis.claw.common.ui.surfaceColorAtNavigationBarElevation
import kotlinx.datetime.Month

@Composable
fun MonthHeader(month: Month) {
  Box(
    Modifier.fillMaxWidth()
      .wrapContentHeight()
      .background(MaterialTheme.colorScheme.surfaceColorAtNavigationBarElevation())
  ) {
    Text(
      text = month.name.lowercase().capitalize(Locale.current),
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
    )
  }
}
