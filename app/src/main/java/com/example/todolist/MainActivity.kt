package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.todolist.fragments.CalendarFragment
import com.example.todolist.fragments.ListFragment
import com.example.todolist.fragments.TaskFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 툴바를 액션바로 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        loadFragment(TaskFragment())

        bottomNav.setOnItemSelectedListener {
            val fragment: Fragment = when (it.itemId) {
                R.id.nav_list -> ListFragment()
                R.id.nav_calendar -> CalendarFragment()
                else -> TaskFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}