package com.example.studentregistration

import StudentAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: StudentAdapter
    private val itemList = mutableListOf<String>()
    private var counter = 1
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "student_db")
            .allowMainThreadQueries() // or better: use coroutines
            .build()

        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            val intent = Intent(this, StudentForm::class.java)
            startActivity(intent)
        }

        loadStudents()
    }

    override fun onResume() {
        super.onResume()
        loadStudents()
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            val students = db.studentDao().getAll() // Safe coroutine call
            adapter = StudentAdapter(
                students,
                onUpdate = { student ->
                    val intent = Intent(this@MainActivity, StudentForm::class.java)
                    intent.putExtra("student_id", student.id)
                    startActivity(intent)
                },
                onDelete = { student ->
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete ${student.name}?")
                        .setPositiveButton("Yes") { _, _ ->
                            lifecycleScope.launch {
                                db.studentDao().delete(student)
                                loadStudents()
                            }
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            )
            recyclerView.adapter = adapter
        }
    }

}



