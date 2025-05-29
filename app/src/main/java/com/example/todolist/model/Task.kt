package com.example.todolist.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,
    var isDone: Boolean,
    val category: String
)
