// com/example/todolist/fragments/CalendarFragment.kt

package com.example.todolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.AddTaskActivity
import com.example.todolist.R
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.data.TaskDBHelper
import com.example.todolist.model.Task
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment(), TaskAdapter.OnTaskActionListener {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDBHelper
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_calendar, container, false).apply {
        calendarView = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.tasksRecyclerView)
        dbHelper     = TaskDBHelper(requireContext())

        // RecyclerView 초기화
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskAdapter(mutableListOf(), this@CalendarFragment)
        recyclerView.adapter = adapter

        // ① 할 일 있는 날짜에 점 찍기
        applyEventDecorator()

        // ② 날짜 선택 시: 리스트 갱신 + 할 일 추가 화면으로 이동
        calendarView.setOnDateChangedListener { _, date, selected ->
            val selectedDate = dateFormat.format(date.date)

            // 1) 선택된 날짜의 할 일만 보여주기
            val tasks: List<Task> = dbHelper.getTasksByDate(selectedDate)
            adapter.setTasks(tasks)

            // 2) 날짜 클릭(선택) 시 AddTaskActivity 로 이동
            if (selected) {
                Intent(requireContext(), AddTaskActivity::class.java).apply {
                    putExtra("selectedDate", selectedDate)
                    startActivity(this)
                }
            }
        }
    }

    override fun onDeleteTask(task: Task, position: Int) {
        dbHelper.deleteTask(task.id)
        adapter.removeAt(position)
    }

    private fun applyEventDecorator() {
        val daysWithTasks = dbHelper.getAllTasks()
            .mapNotNull { dateFormat.parse(it.date) }
            .map { CalendarDay.from(it) }
            .toSet()

        calendarView.addDecorator(EventDecorator(daysWithTasks, requireContext()))
    }
}
