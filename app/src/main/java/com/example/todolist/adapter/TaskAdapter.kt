// com/example/todolist/adapter/TaskAdapter.kt

package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.TaskDBHelper
import com.example.todolist.model.Task
import android.graphics.Paint

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val listener: OnTaskActionListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnTaskActionListener {
        fun onDeleteTask(task: Task, position: Int)
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title    : TextView  = view.findViewById(R.id.taskTitle)
        private val checkBox : CheckBox  = view.findViewById(R.id.taskCheckbox)
        private val dbHelper = TaskDBHelper(view.context)

        fun bind(task: Task, pos: Int) {
            // 1) 텍스트와 체크 상태 세팅
            title.text = task.title
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = task.isDone

            // 2) 스트라이크 쓰루 플래그 적용
            title.paintFlags = if (task.isDone) {
                title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // 3) 체크 상태 변경 리스너
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // DB 저장
                dbHelper.updateTaskStatus(task.id, isChecked)
                // 모델에도 반영
                task.isDone = isChecked
                // 텍스트에 다시 스트라이크 플래그 토글
                title.paintFlags = if (isChecked) {
                    title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                // 강제로 뷰 다시 그리기
                title.invalidate()
            }

            // 4) 롱클릭 삭제
            itemView.setOnLongClickListener {
                listener.onDeleteTask(task, pos)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], position)
    }
    fun getItem(position: Int): Task = tasks[position]

    /** 외부에서 삭제 후, 리스트 갱신 시 호출 */
    fun removeAt(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
