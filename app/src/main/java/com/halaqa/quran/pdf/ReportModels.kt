package com.halaqa.quran.pdf

/** نموذج بيانات مُجمَّع لتقرير طالب واحد ضمن التقرير الأسبوعي */
data class StudentWeeklySummary(
    val studentName: String,
    val presentDays: Int,
    val absentDays: Int,
    val lateDays: Int,
    val excusedDays: Int,
    val totalNewMemorization: String,   // ملخص نصي، مثال: "5 صفحات"
    val totalReview: String,
    val achievementPercent: Int         // 0..100
)

/** بيانات الحضور اليومي المجمّعة عبر أيام الأسبوع (لرسم المخطط) */
data class DailyAttendancePoint(
    val dayLabelAr: String, // مثل "السبت"
    val presentCount: Int,
    val totalCount: Int
)

/** كل البيانات اللازمة لإنشاء التقرير الأسبوعي الكامل */
data class WeeklyReportData(
    val halaqaName: String,
    val teacherName: String,
    val periodStartDisplay: String,
    val periodEndDisplay: String,
    val totalStudents: Int,
    val overallAttendancePercent: Int,
    val dailyAttendance: List<DailyAttendancePoint>,
    val studentSummaries: List<StudentWeeklySummary>,
    val topAchievers: List<String>,      // أسماء أكثر الطلاب إنجازًا
    val laggingStudents: List<String>,   // أسماء الطلاب المتأخرين في الحفظ
    val generalNotes: String
)
