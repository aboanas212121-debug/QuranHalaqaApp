package com.halaqa.quran.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

/** أدوات مشاركة الملفات (PDF) عبر نظام مشاركة أندرويد القياسي (يشمل واتساب/البريد تلقائيًا) */
object ShareUtils {

    fun shareFile(context: Context, file: File, mimeType: String = "application/pdf") {
        val uri = FileProvider.getUriForFile(
            context,
            "com.halaqa.quran.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, "مشاركة التقرير الأسبوعي")
        )
    }

    /** مشاركة مباشرة عبر واتساب إن كان مثبتًا على الجهاز */
    fun shareToWhatsApp(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "com.halaqa.quran.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            `package` = "com.whatsapp"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        runCatching { context.startActivity(intent) }
            .onFailure { shareFile(context, file) } // في حال عدم توفر واتساب، نعرض قائمة المشاركة العامة
    }
}
