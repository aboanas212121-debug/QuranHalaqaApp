package com.halaqa.quran.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.text.Layout
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * يولّد تقرير PDF أسبوعي احترافي باستخدام android.graphics.pdf.PdfDocument
 * فقط (بدون أي مكتبة PDF خارجية)، مع دعم كامل للنص العربي RTL عبر ArabicPdfHelper.
 */
class PdfReportGenerator(private val context: Context) {

    companion object {
        private const val PAGE_WIDTH = 595   // A4 بالنقاط (72dpi)
        private const val PAGE_HEIGHT = 842
        private const val MARGIN = 40f
        private val COLOR_PRIMARY = Color.parseColor("#0F6B4E")
        private val COLOR_GOLD = Color.parseColor("#C9A24B")
        private val COLOR_LIGHT_BG = Color.parseColor("#F2EFE6")
        private val COLOR_TEXT = Color.parseColor("#1B2B22")
    }

    fun generate(data: WeeklyReportData): File {
        val document = PdfDocument()
        var pageNumber = 1
        var page = newPage(document, pageNumber)
        var canvas = page.canvas
        var y = MARGIN
        val contentWidth = (PAGE_WIDTH - 2 * MARGIN).toInt()

        fun ensureSpace(needed: Float) {
            if (y + needed > PAGE_HEIGHT - MARGIN) {
                document.finishPage(page)
                pageNumber++
                page = newPage(document, pageNumber)
                canvas = page.canvas
                y = MARGIN
            }
        }

        // ===== رأس التقرير =====
        drawHeaderBand(canvas)
        val titlePaint = ArabicPdfHelper.makePaint(20f, isBold = true, color = Color.WHITE)
        ArabicPdfHelper.drawParagraph(canvas, "التقرير الأسبوعي لحلقة تحفيظ القرآن الكريم", MARGIN, 22f, contentWidth, titlePaint, Layout.Alignment.ALIGN_CENTER)
        y = 90f

        val infoPaint = ArabicPdfHelper.makePaint(12f, color = COLOR_TEXT)
        val infoLines = listOf(
            "اسم الحلقة: ${data.halaqaName}",
            "المعلم: ${data.teacherName}",
            "الفترة: من ${data.periodStartDisplay} إلى ${data.periodEndDisplay}",
            "عدد الطلاب: ${data.totalStudents}   |   نسبة الحضور العامة: ${data.overallAttendancePercent}%"
        )
        for (line in infoLines) {
            y += ArabicPdfHelper.drawParagraph(canvas, line, MARGIN, y, contentWidth, infoPaint, Layout.Alignment.ALIGN_OPPOSITE) + 4f
        }
        y += 10f

        // ===== مخطط الحضور الأسبوعي (أعمدة بسيطة) =====
        ensureSpace(160f)
        y += ArabicPdfHelper.drawParagraph(
            canvas, "مخطط الحضور الأسبوعي", MARGIN, y, contentWidth,
            ArabicPdfHelper.makePaint(14f, isBold = true, color = COLOR_PRIMARY), Layout.Alignment.ALIGN_OPPOSITE
        ) + 8f
        y += drawAttendanceBarChart(canvas, data.dailyAttendance, y, contentWidth)
        y += 16f

        // ===== جدول ملخص الطلاب =====
        ensureSpace(60f)
        y += ArabicPdfHelper.drawParagraph(
            canvas, "ملخص أداء الطلاب", MARGIN, y, contentWidth,
            ArabicPdfHelper.makePaint(14f, isBold = true, color = COLOR_PRIMARY), Layout.Alignment.ALIGN_OPPOSITE
        ) + 8f

        val headers = listOf("الطالب", "حضور", "غياب", "حفظ جديد", "مراجعة", "الإنجاز%")
        val colWidths = listOf(0.28f, 0.10f, 0.10f, 0.20f, 0.20f, 0.12f).map { it * contentWidth }

        y = drawTableRow(canvas, headers, colWidths, y, contentWidth, isHeader = true)
        for (s in data.studentSummaries) {
            ensureSpace(30f)
            val row = listOf(
                s.studentName,
                s.presentDays.toString(),
                s.absentDays.toString(),
                s.totalNewMemorization,
                s.totalReview,
                "${s.achievementPercent}%"
            )
            y = drawTableRow(canvas, row, colWidths, y, contentWidth, isHeader = false)
        }
        y += 16f

        // ===== الأكثر إنجازًا / المتأخرون =====
        ensureSpace(100f)
        val highlightPaint = ArabicPdfHelper.makePaint(12f, color = COLOR_TEXT)
        y += ArabicPdfHelper.drawParagraph(canvas, "⭐ الطلاب الأكثر إنجازًا: ${data.topAchievers.joinToString("، ").ifBlank { "لا يوجد" }}", MARGIN, y, contentWidth, highlightPaint, Layout.Alignment.ALIGN_OPPOSITE) + 8f
        y += ArabicPdfHelper.drawParagraph(canvas, "⚠ الطلاب المتأخرون في الحفظ: ${data.laggingStudents.joinToString("، ").ifBlank { "لا يوجد" }}", MARGIN, y, contentWidth, highlightPaint, Layout.Alignment.ALIGN_OPPOSITE) + 8f
        y += 16f

        // ===== ملاحظات عامة =====
        ensureSpace(80f)
        y += ArabicPdfHelper.drawParagraph(
            canvas, "ملاحظات عامة", MARGIN, y, contentWidth,
            ArabicPdfHelper.makePaint(14f, isBold = true, color = COLOR_PRIMARY), Layout.Alignment.ALIGN_OPPOSITE
        ) + 8f
        y += ArabicPdfHelper.drawParagraph(
            canvas, data.generalNotes.ifBlank { "لا توجد ملاحظات إضافية." }, MARGIN, y, contentWidth,
            ArabicPdfHelper.makePaint(12f, color = COLOR_TEXT), Layout.Alignment.ALIGN_OPPOSITE
        )

        document.finishPage(page)

        val fileName = "تقرير_${data.halaqaName}_${SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())}.pdf"
        val outDir = File(context.getExternalFilesDir(null), "reports").apply { mkdirs() }
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()
        return outFile
    }

    private fun newPage(document: PdfDocument, pageNumber: Int): PdfDocument.Page {
        val info = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        return document.startPage(info)
    }

    private fun drawHeaderBand(canvas: android.graphics.Canvas) {
        val paint = Paint().apply { color = COLOR_PRIMARY }
        canvas.drawRect(RectF(0f, 0f, PAGE_WIDTH.toFloat(), 55f), paint)
    }

    /** يرسم مخططًا شريطيًا بسيطًا لحضور أيام الأسبوع ويعيد الارتفاع المُستهلَك */
    private fun drawAttendanceBarChart(canvas: android.graphics.Canvas, points: List<DailyAttendancePoint>, top: Float, width: Int): Float {
        if (points.isEmpty()) return 0f
        val chartHeight = 110f
        val barAreaWidth = width / points.size.toFloat()
        val barWidth = barAreaWidth * 0.5f
        val barPaint = Paint().apply { color = COLOR_GOLD }
        val bgPaint = Paint().apply { color = COLOR_LIGHT_BG }
        val labelPaint = ArabicPdfHelper.makePaint(9f, color = COLOR_TEXT)

        canvas.drawRect(RectF(MARGIN, top, MARGIN + width, top + chartHeight), bgPaint)

        points.forEachIndexed { index, point ->
            val percent = if (point.totalCount == 0) 0f else point.presentCount.toFloat() / point.totalCount
            val barHeight = chartHeight * 0.8f * percent
            // نرسم من اليمين لليسار لتماشي الاتجاه العربي
            val slotRight = MARGIN + width - index * barAreaWidth
            val barLeft = slotRight - barAreaWidth / 2f - barWidth / 2f
            val barTop = top + chartHeight - 18f - barHeight
            canvas.drawRect(RectF(barLeft, barTop, barLeft + barWidth, top + chartHeight - 18f), barPaint)
            ArabicPdfHelper.drawParagraph(canvas, point.dayLabelAr, barLeft - 10f, top + chartHeight - 14f, (barAreaWidth + 20f).toInt(), labelPaint, Layout.Alignment.ALIGN_CENTER)
        }
        return chartHeight + 6f
    }

    /** يرسم صفًا واحدًا من الجدول (رأس أو بيانات) ويعيد الإحداثي y للصف التالي */
    private fun drawTableRow(canvas: android.graphics.Canvas, cells: List<String>, colWidths: List<Float>, top: Float, totalWidth: Int, isHeader: Boolean): Float {
        val rowHeight = 26f
        val bgPaint = Paint().apply { color = if (isHeader) COLOR_PRIMARY else Color.WHITE }
        canvas.drawRect(RectF(MARGIN, top, MARGIN + totalWidth, top + rowHeight), bgPaint)

        val textPaint = ArabicPdfHelper.makePaint(10f, isBold = isHeader, color = if (isHeader) Color.WHITE else COLOR_TEXT)

        // الأعمدة تُرسم من اليمين إلى اليسار لمطابقة اتجاه القراءة العربي
        var xRight = MARGIN + totalWidth
        for (i in cells.indices) {
            val w = colWidths[i]
            ArabicPdfHelper.drawParagraph(canvas, cells[i], xRight - w, top + 5f, w.toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)
            xRight -= w
        }
        // خط فاصل أسفل الصف
        canvas.drawLine(MARGIN, top + rowHeight, MARGIN + totalWidth, top + rowHeight, Paint().apply { color = Color.LTGRAY; strokeWidth = 0.5f })
        return top + rowHeight
    }
}
