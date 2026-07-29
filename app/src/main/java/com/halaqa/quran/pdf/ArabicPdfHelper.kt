package com.halaqa.quran.pdf

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint

/**
 * أداة مساعدة لرسم نص عربي RTL بشكل صحيح داخل PdfDocument
 * باستخدام StaticLayout مع TextDirectionHeuristics.RTL (بدون أي مكتبة خارجية).
 */
object ArabicPdfHelper {

    fun makePaint(textSize: Float, isBold: Boolean = false, color: Int = android.graphics.Color.BLACK): TextPaint {
        return TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            this.textSize = textSize
            this.color = color
            this.isFakeBoldText = isBold
            // خط النظام الافتراضي يدعم العربية على أغلب أجهزة أندرويد؛
            // يمكن استبداله بخط عربي مخصص (مثل Cairo/Tajawal) بوضعه في res/font
            typeface = android.graphics.Typeface.SANS_SERIF
        }
    }

    /**
     * يرسم فقرة نص عربي في نقطة (x, y) بعرض أقصى [maxWidth]، ويعيد الارتفاع المُستهلَك بالبكسل
     * ليتم استخدامه في حساب موضع العنصر التالي (تخطيط عمودي متسلسل).
     */
    fun drawParagraph(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        maxWidth: Int,
        paint: TextPaint,
        align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    ): Float {
        val builder = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, maxWidth)
            .setAlignment(align)
            .setTextDirection(TextDirectionHeuristics.RTL)
            .setLineSpacing(0f, 1.15f)
            .setIncludePad(false)

        val layout = builder.build()
        canvas.save()
        canvas.translate(x, y)
        layout.draw(canvas)
        canvas.restore()
        return layout.height.toFloat()
    }
}
