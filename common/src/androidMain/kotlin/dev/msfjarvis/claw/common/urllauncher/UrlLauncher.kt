package dev.msfjarvis.claw.common.urllauncher

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.platform.UriHandler
import io.github.aakira.napier.Napier

class UrlLauncher(private val context: Context) : UriHandler {
  override fun openUri(uri: String) {
    val customTabsIntent =
      CustomTabsIntent.Builder()
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setShowTitle(true)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
        .build()
    try {
      customTabsIntent.launchUrl(context, Uri.parse(uri))
    } catch (e: ActivityNotFoundException) {
      val error = "Failed to open URL: $uri"
      Napier.d(tag = "UrlLauncher") { error }
      Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
  }
}
