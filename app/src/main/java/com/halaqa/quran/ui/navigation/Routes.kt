package com.halaqa.quran.ui.navigation

/** مسارات التنقل داخل التطبيق */
object Routes {
    const val DASHBOARD = "dashboard"
    const val STUDENTS = "students"
    const val ADD_EDIT_STUDENT = "add_edit_student?studentId={studentId}"
    fun addEditStudent(studentId: Long? = null) = "add_edit_student?studentId=${studentId ?: -1}"
    const val DAILY_SESSION = "daily_session"
    const val WEEKLY_REPORT = "weekly_report"
    const val SETTINGS = "settings"
}
