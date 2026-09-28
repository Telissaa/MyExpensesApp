package pl.wluczak.myexpenses.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.wluczak.myexpenses.ui.addexpense.AddExpenseScreen
import pl.wluczak.myexpenses.ui.history.HistoryScreen
import pl.wluczak.myexpenses.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddExpense : Screen("add_expense?expenseId={expenseId}") {
        fun createRoute(expenseId: Int = -1) = "add_expense?expenseId=$expenseId"
    }
    object Analytics : Screen("analytics")
    object History : Screen("history")
    object BudgetPlanning : Screen("budget_planning")
}

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val animDuration = 280

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(animDuration, easing = FastOutSlowInEasing)) +
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(animDuration, easing = FastOutSlowInEasing)) +
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(animDuration, easing = FastOutSlowInEasing)) +
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(animDuration, easing = FastOutSlowInEasing)) +
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
            )
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddExpense = {
                    navController.navigate(Screen.AddExpense.createRoute())
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.Analytics.route)
                },
                onNavigateToBudgetPlanning = {
                    navController.navigate(Screen.BudgetPlanning.route)
                }
            )
        }
        composable(
            route = Screen.AddExpense.route,
            arguments = listOf(
                navArgument("expenseId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: -1
            AddExpenseScreen(
                expenseId = expenseId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Analytics.route) {
            PlaceholderScreen(title = "Statystyki")
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateToAddExpense = { expenseId ->
                    navController.navigate(Screen.AddExpense.createRoute(expenseId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.BudgetPlanning.route) {
            PlaceholderScreen(title = "Zaprojektuj swój budżet")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}
