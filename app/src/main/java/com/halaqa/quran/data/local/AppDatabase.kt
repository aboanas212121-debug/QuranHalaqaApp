package com.halaqa.quran.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.halaqa.quran.data.local.dao.DailyEntryDao
import com.halaqa.quran.data.local.dao.StudentDao
import com.halaqa.quran.data.local.entity.DailyEntry
import com.halaqa.quran.data.local.entity.Student

@Database(
    entities = [Student::class, DailyEntry::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun dailyEntryDao(): DailyEntryDao

    companion object {
        const val DB_NAME = "halaqa_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    // بدون Migration معقدة حاليًا؛ عند تحديث الإصدار مستقبلًا
                    // يجب كتابة Migration صريحة للحفاظ على بيانات المستخدم
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
