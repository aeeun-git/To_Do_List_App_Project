package com.example.todolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.todolist.AddTaskActivity
import com.example.todolist.R
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.data.TaskDBHelper
import com.example.todolist.model.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast

class TaskFragment : Fragment(), TaskAdapter.OnTaskActionListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: TaskDBHelper
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        recyclerView = view.findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dbHelper = TaskDBHelper(requireContext())

        // FAB 클릭 시 추가 화면
        view.findViewById<FloatingActionButton>(R.id.btn_add_task)
            .setOnClickListener {
                startActivity(Intent(requireContext(), AddTaskActivity::class.java))
            }

        // TaskFragment.kt 안 onCreateView() 중에서…

        // 스와이프 삭제 처리
        val touchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // ↓ bindingAdapterPosition 대신 adapterPosition 사용
                val pos = viewHolder.adapterPosition
                val task = taskList[pos]
                dbHelper.deleteTask(task.id)
                adapter.removeAt(pos)
                Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        })
        touchHelper.attachToRecyclerView(recyclerView)


        loadTasks()
        return view
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        taskList = dbHelper.getAllTasks().toMutableList()
        adapter = TaskAdapter(taskList, this)
        recyclerView.adapter = adapter
    }

    // 롱클릭 삭제 콜백
    override fun onDeleteTask(task: Task, position: Int) {
        dbHelper.deleteTask(task.id)
        adapter.removeAt(position)
        Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }
}
