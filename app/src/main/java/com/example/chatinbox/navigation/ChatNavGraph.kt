package com.example.chatinbox.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatinbox.data.repository.ChatRepository
import com.example.chatinbox.ui.conversation.ConversationScreen
import com.example.chatinbox.ui.conversation.ConversationViewModel
import com.example.chatinbox.ui.conversation.ConversationViewModelFactory
import com.example.chatinbox.ui.inbox.InboxScreen
import com.example.chatinbox.ui.inbox.InboxViewModel
import com.example.chatinbox.ui.inbox.InboxViewModelFactory

@Composable
fun ChatNavGraph(
    repository: ChatRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.INBOX,
        modifier = modifier
    ) {
        composable(Routes.INBOX) {
            val viewModel: InboxViewModel = viewModel(
                factory = InboxViewModelFactory(repository)
            )

            InboxScreen(
                viewModel = viewModel,
                onConversationClick = { conversationId ->
                    navController.navigate(Routes.conversationRoute(conversationId))
                }
            )
        }

        composable(
            route = Routes.CONVERSATION_ROUTE,
            arguments = listOf(
                navArgument(Routes.CONVERSATION_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val conversationId =
                backStackEntry.arguments?.getString(Routes.CONVERSATION_ID_ARG).orEmpty()

            val viewModel: ConversationViewModel = viewModel(
                factory = ConversationViewModelFactory(
                    repository = repository,
                    conversationId = conversationId
                )
            )

            ConversationScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}