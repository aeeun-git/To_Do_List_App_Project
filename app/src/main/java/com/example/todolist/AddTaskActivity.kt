package com.example.todolist

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.todolist.data.TaskDBHelper
import com.example.todolist.model.Task
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var txtDate: TextView
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSave: Button

    private var selectedDate: String = ""
    // 실제 카테고리 목록
    private val realCategories = listOf("공부", "운동", "업무", "기타")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // 툴바 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // 뷰 바인딩
        editTitle       = findViewById(R.id.edit_title)
        editDescription = findViewById(R.id.edit_description)
        txtDate         = findViewById(R.id.txt_date)
        spinnerCategory = findViewById(R.id.spinner_category)
        btnSave         = findViewById(R.id.btn_save)

        // 1️⃣ 더미 힌트(카테고리)와 실제 목록 합치기
        val items = mutableListOf<String>().apply {
            add(getString(R.string.spinner_prompt))  // position 0: '카테고리'
            addAll(realCategories)                  // 이후 실제 항목
        }

        // 2️⃣ ArrayAdapter 커스터마이징: position 0 은 비활성 & 회색으로
        val adapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, items
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view.findViewById<TextView>(android.R.id.text1)
                tv.setTextColor(
                    if (position == 0) Color.GRAY
                    else Color.BLACK
                )
                return view
            }
        }.also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // 3️⃣ Spinner에 적용
        spinnerCategory.adapter = adapter
        spinnerCategory.setSelection(0, false)  // 처음엔 position=0(힌트)만 보여줌

        // 선택 리스너
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, pos: Int, id: Long
            ) {
                // pos > 0 일 때만 실제 카테고리 선택
                if (pos > 0) {
                    val category = parent.getItemAtPosition(pos) as String
                    // 필요하면 category 변수 사용
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // DB 헬퍼
        val db = TaskDBHelper(this)

        // 날짜 선택 다이얼로그
        txtDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    cal.set(y, m, d)
                    selectedDate = sdf.format(cal.time)
                    txtDate.text = selectedDate
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 인텐트에서 넘어온 날짜
        intent.getStringExtra("selectedDate")?.let {
            selectedDate = it
            txtDate.text = it
        }

        // 저장 버튼
        btnSave.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val desc  = editDescription.text.toString().trim()
            // position==0 이면 아직 힌트 상태이므로 처리 막기
            if (spinnerCategory.selectedItemPosition == 0) {
                Toast.makeText(this, "카테고리를 선택하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val category = spinnerCategory.selectedItem as String

            if (title.isEmpty()) {
                Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Task 생성 및 DB 저장
            val task = Task(
                title       = title,
                description = desc,
                date        = selectedDate,
                isDone      = false,
                category    = category
            )
            db.insertTask(task)
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
