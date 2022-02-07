package dev.msfjarvis.claw.android.ui.decorations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavController
import dev.msfjarvis.claw.android.ui.AnimationDuration
import dev.msfjarvis.claw.android.ui.navigation.Destinations

@Composable
fun ClawNavigationBar(
  navController: NavController,
  items: List<NavigationItem>,
  isVisible: Boolean,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = isVisible,
    enter =
      slideInVertically(
        // Enters by sliding up from offset 0 to fullHeight.
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = AnimationDuration, easing = LinearOutSlowInEasing),
      ),
    exit =
      slideOutVertically(
        // Exits by sliding up from offset 0 to -fullHeight.
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = AnimationDuration, easing = FastOutLinearInEasing),
      ),
    modifier = Modifier,
  ) {
    NavigationBar(modifier = modifier) {
      items.forEach { navItem ->
        NavigationBarItem(
          icon = { Icon(painter = navItem.icon, contentDescription = navItem.label) },
          label = { Text(text = navItem.label) },
          selected = navController.currentDestination?.route == navItem.route,
          onClick = {
            if (navController.currentDestination?.route == navItem.route) return@NavigationBarItem
            navController.popBackStack(navController.graph.startDestinationRoute!!, false)
            if (navItem.route != Destinations.startDestination.getRoute()) {
              navController.navigate(navItem.route)
            }
          }
        )
      }
    }
  }
}

class NavigationItem(
  val label: String,
  val route: String,
  val icon: Painter,
)
