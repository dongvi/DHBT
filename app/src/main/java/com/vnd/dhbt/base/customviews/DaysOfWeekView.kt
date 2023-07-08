package com.vnd.dhbt.base.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import com.vnd.dhbt.R
import java.util.Calendar

class DaysOfWeekView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val viewList = listOf(
        DayView(DayOfWeek.T2),
        DayView(DayOfWeek.T3),
        DayView(DayOfWeek.T4),
        DayView(DayOfWeek.T5),
        DayView(DayOfWeek.T6),
        DayView(DayOfWeek.T7),
        DayView(DayOfWeek.CN),
    )

    init {
        val childViewLayout =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                setMargins(4)
            }

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_HORIZONTAL
        viewList.forEach { addView(it, childViewLayout) }
    }

    fun getValue(): String {
        val daysSelected = viewList.filter { it.isChecked }.map { DayOfWeek.toValue(it.day) }
        var string = ""

        daysSelected.forEach { string += "$it" + if (it == daysSelected.last()) "" else "," }

        return string
    }

    enum class DayOfWeek {
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        CN;

        companion object {
            fun toValue(day: DayOfWeek): Int {
                return when (day) {
                    T2 -> 2
                    T3 -> 3
                    T4 -> 4
                    T5 -> 5
                    T6 -> 6
                    T7 -> 7
                    CN -> 8
                }
            }

            private fun toDay(value: Int): DayOfWeek? =
                when (value) {
                    2 -> T2
                    3 -> T3
                    4 -> T4
                    5 -> T5
                    6 -> T6
                    7 -> T7
                    8 -> CN
                    else -> null
                }

            fun toDayString(data: String): String {
                var dayString = ""
                val days = data.trim().split(",").map { it.toInt() }

                if(days.size == 7) return "Mỗi ngày!"

                days.forEach { dayString += "${toDay(it)}" + if (it == days.last()) "" else "," }

                return dayString
            }
        }
    }

    inner class DayView(private val _day: DayOfWeek) : LinearLayout(context) {
        private var _isChecked = false
        val isChecked: Boolean get() = _isChecked

        val day: DayOfWeek get() = _day

        private val textView = TextView(context).apply {
            text = _day.name
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(R.dimen.large)
            )
            typeface = Typeface.DEFAULT_BOLD
        }

        init {
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

            gravity = Gravity.CENTER
            addView(textView)
            check(today == DayOfWeek.toValue(day))
            setOnClickListener { check(!_isChecked) }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun check(yep: Boolean = false) {
            background =
                context.getDrawable(if (yep) R.drawable.bg_day_selected else R.drawable.bg_day_unselected)
            textView.setTextColor(context.getColor(if (yep) R.color.white else R.color.off))
            _isChecked = yep
        }
    }
}
