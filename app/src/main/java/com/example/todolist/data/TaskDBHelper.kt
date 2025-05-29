package com.example.todolist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolist.model.Task

class TaskDBHelper(context: Context) : SQLiteOpenHelper(context, "tasks.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                date TEXT,
                category TEXT,
                is_done INTEGER DEFAULT 0
            )
        """
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun insertTask(task: Task) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put("title", task.title)
                put("description", task.description)
                put("date", task.date)
                put("category", task.category)
                put("is_done", if (task.isDone) 1 else 0)
            }
            db.insert("tasks", null, values)
        }
    }

    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery("SELECT * FROM tasks", null)
            cursor.use {
                while (it.moveToNext()) {
                    val id          = it.getInt(it.getColumnIndexOrThrow("id"))
                    val title       = it.getString(it.getColumnIndexOrThrow("title"))
                    val description = it.getString(it.getColumnIndexOrThrow("description"))
                    val date        = it.getString(it.getColumnIndexOrThrow("date"))
                    val isDone      = it.getInt(it.getColumnIndexOrThrow("is_done")) == 1
                    val category    = it.getString(it.getColumnIndexOrThrow("category"))
                    taskList += Task(id, title, description, date, isDone, category)
                }
            }
        }
        return taskList
    }

    fun getTasksByCategory(category: String): List<Task> {
        val taskList = mutableListOf<Task>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery(
                "SELECT * FROM tasks WHERE category = ?",
                arrayOf(category.toString())
            )
            cursor.use {
                while (it.moveToNext()) {
                    val id          = it.getInt(it.getColumnIndexOrThrow("id"))
                    val title       = it.getString(it.getColumnIndexOrThrow("title"))
                    val description = it.getString(it.getColumnIndexOrThrow("description"))
                    val date        = it.getString(it.getColumnIndexOrThrow("date"))
                    val isDone      = it.getInt(it.getColumnIndexOrThrow("is_done")) == 1
                    val cat         = it.getString(it.getColumnIndexOrThrow("category"))
                    taskList += Task(id, title, description, date, isDone, cat)
                }
            }
        }
        return taskList
    }

    /** 새로 추가: id 로 한 건 삭제 */
    fun deleteTask(id: Int) {
        writableDatabase.use { db ->
            db.delete("tasks", "id = ?", arrayOf(id.toString()))
        }
    }
    fun getTasksByDate(date: String): List<Task> {
        val list = mutableListOf<Task>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery(
                "SELECT * FROM tasks WHERE date = ?",
                arrayOf(date)
            )
            cursor.use {
                while (it.moveToNext()) {
                    // 기존 getAllTasks 로직 복사
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val title = it.getString(it.getColumnIndexOrThrow("title"))
                    val desc = it.getString(it.getColumnIndexOrThrow("description"))
                    val d = it.getString(it.getColumnIndexOrThrow("date"))
                    val done = it.getInt(it.getColumnIndexOrThrow("is_done")) == 1
                    val cat = it.getString(it.getColumnIndexOrThrow("category"))
                    list += Task(id, title, desc, d, done, cat)
                }
            }
        }
        return list
    }

    fun getAllTaskDates(): List<String> {
        val dates = mutableListOf<String>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery(
                "SELECT DISTINCT date FROM tasks WHERE date IS NOT NULL",
                null
            )
            cursor.use {
                while (it.moveToNext()) {
                    dates += it.getString(0)
                }
            }
        }
        return dates
    }
    fun updateTaskStatus(id: Int, isDone: Boolean) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put("is_done", if (isDone) 1 else 0)
            }
            db.update(
                "tasks",
                values,
                "id = ?",
                arrayOf(id.toString())
            )
        }
    }
}
