package dev.msfjarvis.claw.android.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.msfjarvis.claw.android.ui.decorations.ClawAppBar
import dev.msfjarvis.claw.android.ui.decorations.ClawFab
import dev.msfjarvis.claw.android.ui.lists.DatabasePosts
import dev.msfjarvis.claw.android.ui.lists.HottestPosts
import dev.msfjarvis.claw.android.ui.navigation.Destinations
import dev.msfjarvis.claw.android.viewmodel.ClawViewModel
import dev.msfjarvis.claw.common.comments.CommentsPage
import dev.msfjarvis.claw.common.comments.HTMLConverter
import dev.msfjarvis.claw.common.comments.LocalHTMLConverter
import dev.msfjarvis.claw.common.theme.LobstersTheme
import dev.msfjarvis.claw.common.urllauncher.UrlLauncher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobstersApp(
  viewModel: ClawViewModel = viewModel(),
  urlLauncher: UrlLauncher,
  htmlConverter: HTMLConverter,
  setWebUri: (String) -> Unit,
) {
  var isFabVisible by remember { mutableStateOf(false) }

  val systemUiController = rememberSystemUiController()
  val networkListState = rememberLazyListState()
  val savedListState = rememberLazyListState()
  val navController = rememberNavController()
  val postActions = rememberPostActions(urlLauncher, navController, viewModel)
  val currentDestination by currentNavigationDestination(navController)
  val nestedScrollConnection = rememberNestedScrollConnection { isFabVisible = it }

  val networkPosts = viewModel.pagerFlow.collectAsLazyPagingItems()
  val savedPosts by viewModel.savedPosts.collectAsState(emptyList())

  LobstersTheme(
    LocalUriHandler provides urlLauncher,
    LocalHTMLConverter provides htmlConverter,
    colorScheme = decideColorScheme(LocalContext.current),
  ) {
    ProvideWindowInsets {
      val statusBarColor = MaterialTheme.colorScheme.background

      SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = Color.Transparent)
      }

      Scaffold(
        topBar = { ClawAppBar(modifier = Modifier.statusBarsPadding()) },
        floatingActionButton = {
          ClawFab(
            isFabVisible = isFabVisible && currentDestination == Destinations.Hottest.getRoute(),
            listState = networkListState,
            modifier = Modifier.navigationBarsPadding(),
          )
        },
      ) {
        NavHost(navController, startDestination = Destinations.Hottest.getRoute()) {
          composable(Destinations.Hottest.getRoute()) {
            setWebUri("https://lobste.rs/")
            HottestPosts(
              items = networkPosts,
              listState = networkListState,
              isPostSaved = viewModel::isPostSaved,
              reloadPosts = viewModel::reloadPosts,
              postActions = postActions,
              modifier = Modifier.nestedScroll(nestedScrollConnection),
            )
          }
          composable(Destinations.Saved.getRoute()) {
            DatabasePosts(
              items = savedPosts,
              listState = savedListState,
              isSaved = viewModel::isPostSaved,
              postActions = postActions,
              modifier = Modifier.nestedScroll(nestedScrollConnection),
            )
          }
          composable(Destinations.Comments.getRoute("{postId}")) { backStackEntry ->
            val postId = requireNotNull(backStackEntry.arguments?.getString("postId"))
            setWebUri("https://lobste.rs/s/$postId")
            CommentsPage(
              postId = postId,
              getDetails = viewModel::getPostComments,
              modifier = Modifier.navigationBarsPadding(),
            )
          }
        }
      }
    }
  }
}
