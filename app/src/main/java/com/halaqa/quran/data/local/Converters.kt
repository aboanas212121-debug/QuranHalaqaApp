package com.halaqa.quran.data.local

import androidx.room.TypeConverter
import com.halaqa.quran.data.local.entity.AttendanceStatus
import com.halaqa.quran.data.local.entity.EvaluationGrade

/** محوّلات Room للأنواع المُعدّدة (Enums) */
class Converters {
    @TypeConverter
    fun fromAttendance(value: AttendanceStatus): String = value.name

    @TypeConverter
    fun toAttendance(value: String): AttendanceStatus = AttendanceStatus.valueOf(value)

    @TypeConverter
    fun fromGrade(value: EvaluationGrade?): String? = value?.name

    @TypeConverter
    fun toGrade(value: String?): EvaluationGrade? = value?.let { EvaluationGrade.valueOf(it) }
}
