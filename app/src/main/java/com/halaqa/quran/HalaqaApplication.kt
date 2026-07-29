package com.halaqa.quran

import android.app.Application
import com.halaqa.quran.data.local.AppDatabase
import com.halaqa.quran.data.repository.RecordRepository
import com.halaqa.quran.data.repository.StudentRepository

/**
 * فئة التطبيق: تُنشئ قاعدة البيانات والمستودعات مرة واحدة (Singleton بسيط)
 * بدون الحاجة إلى مكتبة حقن تبعيات خارجية (Dagger/Hilt) — تبسيطًا وتخفيفًا للتطبيق.
 */
class HalaqaApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val studentRepository: StudentRepository by lazy { StudentRepository(database.studentDao()) }
    val recordRepository: RecordRepository by lazy { RecordRepository(database.dailyEntryDao()) }
}
