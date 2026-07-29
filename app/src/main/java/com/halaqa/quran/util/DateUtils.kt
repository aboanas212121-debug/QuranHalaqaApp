package com.halaqa.quran.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val PATTERN = "yyyy-MM-dd"

    fun today(): String = format(Date())

    fun format(date: Date): String =
        SimpleDateFormat(PATTERN, Locale.US).format(date)

    fun parse(dateStr: String): Date =
        SimpleDateFormat(PATTERN, Locale.US).parse(dateStr) ?: Date()

    /** يعيد تاريخ بداية الأسبوع الحالي (السبت) ونهايته (الجمعة) بصيغة yyyy-MM-dd */
    fun currentWeekRange(): Pair<String, String> {
        val cal = Calendar.getInstance()
        // نعتبر السبت أول أيام الأسبوع (الأسبوع الدراسي/الحلقات في الغالب يبدأ سبت)
        cal.firstDayOfWeek = Calendar.SATURDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        val start = format(cal.time)
        cal.add(Calendar.DAY_OF_MONTH, 6)
        val end = format(cal.time)
        return start to end
    }

    /** تنسيق عربي مقروء لعرض التاريخ في الواجهة والتقرير، مثال: "الخميس 30 يوليو 2026" */
    fun displayArabic(dateStr: String): String {
        val date = parse(dateStr)
        val formatter = SimpleDateFormat("EEEE d MMMM yyyy", Locale("ar"))
        return formatter.format(date)
    }

    fun weekDayNameArabic(dateStr: String): String {
        val date = parse(dateStr)
        return SimpleDateFormat("EEEE", Locale("ar")).format(date)
    }
}
