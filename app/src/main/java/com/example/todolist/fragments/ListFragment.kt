// com/example/todolist/fragments/ListFragment.kt

package com.example.todolist.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.todolist.R
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.data.TaskDBHelper
import com.example.todolist.model.Task

class ListFragment : Fragment(), TaskAdapter.OnTaskActionListener {

    private lateinit var dbHelper    : TaskDBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner     : Spinner
    private lateinit var adapter     : TaskAdapter

    // DB 에 저장된 문자열 카테고리와 똑같이
    private val realCategories = listOf("공부", "운동", "업무", "기타")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // 1) 뷰 바인딩 & DB helper
        dbHelper     = TaskDBHelper(requireContext())
        recyclerView = view.findViewById(R.id.recycler_list)
        spinner      = view.findViewById(R.id.category_spinner)

        // 2) RecyclerView + Adapter 초기화 (빈 리스트로)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        // 처음엔 전체 리스트
        adapter.setTasks(dbHelper.getAllTasks())

        // 3) Spinner 아이템 준비
        val items = mutableListOf<String>().apply {
            add(getString(R.string.spinner_prompt))  // "카테고리"
            addAll(realCategories)
        }
        val spAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ) {
            override fun isEnabled(position: Int) = position != 0
            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val v  = super.getDropDownView(position, convertView, parent)
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                return v
            }
        }.also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
            spinner.setSelection(0, false)
        }

        // 4) Spinner 선택 시 바로 필터링
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, pos: Int, id: Long
            ) {
                val list: List<Task> = if (pos == 0) {
                    dbHelper.getAllTasks()
                } else {
                    dbHelper.getTasksByCategory(realCategories[pos - 1])
                }
                adapter.setTasks(list)
            }
            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

        // 5) 스와이프 삭제
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val pos  = vh.adapterPosition
                val task = adapter.getItem(pos)
                dbHelper.deleteTask(task.id)
                adapter.removeAt(pos)
                Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

        return view
    }

    override fun onDeleteTask(task: Task, position: Int) {
        dbHelper.deleteTask(task.id)
        adapter.removeAt(position)
        Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }
}
