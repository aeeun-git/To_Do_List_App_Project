// com/example/todolist/fragments/EventDecorator.kt

package com.example.todolist.fragments

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.example.todolist.R
import java.util.*

class EventDecorator(
    private val dates: Set<CalendarDay>,
    context: Context
) : DayViewDecorator {

    private val dotSpan = DotSpan(6f, ContextCompat.getColor(context, R.color.event_dot_color))

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(dotSpan)
    }
}
