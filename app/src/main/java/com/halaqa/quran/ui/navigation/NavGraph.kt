package com.halaqa.quran.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.halaqa.quran.ui.screens.dashboard.DashboardScreen
import com.halaqa.quran.ui.screens.daily.DailySessionScreen
import com.halaqa.quran.ui.screens.report.WeeklyReportScreen
import com.halaqa.quran.ui.screens.settings.SettingsScreen
import com.halaqa.quran.ui.screens.students.AddEditStudentScreen
import com.halaqa.quran.ui.screens.students.StudentListScreen

@Composable
fun HalaqaNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) { DashboardScreen(navController) }
        composable(Routes.STUDENTS) { StudentListScreen(navController) }
        composable(
            Routes.ADD_EDIT_STUDENT,
            arguments = listOf(navArgument("studentId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getLong("studentId") ?: -1L
            AddEditStudentScreen(navController, if (studentId == -1L) null else studentId)
        }
        composable(Routes.DAILY_SESSION) { DailySessionScreen(navController) }
        composable(Routes.WEEKLY_REPORT) { WeeklyReportScreen(navController) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
    }
}
