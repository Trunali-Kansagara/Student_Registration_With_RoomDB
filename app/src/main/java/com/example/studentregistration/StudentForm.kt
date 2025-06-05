package com.example.studentregistration

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class StudentForm : AppCompatActivity() {


    private lateinit var db: AppDatabase
    private var studentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_form)

        db = AppDatabase.getDatabase(this)

        AddStudentInfo()
    }

    private  fun AddStudentInfo() {
        val etName = findViewById<EditText>(R.id.etName)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val spinnerCourse = findViewById<Spinner>(R.id.spinnerCourse)
        val cbSports = findViewById<CheckBox>(R.id.cbSports)
        val cbMusic = findViewById<CheckBox>(R.id.cbMusic)
        val cbReading = findViewById<CheckBox>(R.id.cbReading)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val btnSave = findViewById<Button>(R.id.btnSave)

        //set up spinner dropdown
        val courses = listOf("BCA", "BSc", "BBA", "MBA", "MCA")
        spinnerCourse.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, courses)

        // Check for update mode
        studentId = intent.getIntExtra("student_id", -1).takeIf { it != -1 }

        if (studentId != null) {
            lifecycleScope.launch {

                val student = db.studentDao().getStudentById(studentId!!)
                student?.let {
                    etName.setText(it.name)
                    when (it.gender) {
                        "Male" -> rgGender.check(R.id.rbMale)
                        "Female" -> rgGender.check(R.id.rbFemale)
                    }
                    spinnerCourse.setSelection(courses.indexOf(it.course))
                    cbSports.isChecked = it.hobbies.contains("Sports")
                    cbMusic.isChecked = it.hobbies.contains("Music")
                    cbReading.isChecked = it.hobbies.contains("Reading")
                    etAddress.setText(it.address)
                    btnSave.setText("Update")

                }
            }
        }
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbMale -> "Male"
                R.id.rbFemale -> "Female"
                else -> ""

            }
            val course = spinnerCourse.selectedItem.toString()
            val hobbies = listOfNotNull(
                cbSports.takeIf { it.isChecked }?.text,
                cbMusic.takeIf { it.isChecked }?.text,
                cbReading.takeIf { it.isChecked }?.text
            ).joinToString(", ");
            val address = etAddress.text.toString()
            val student = Student(
                id = studentId ?: 0,
                name = name,
                gender = gender,
                course = course,
                hobbies = hobbies,
                address = address
            )
            lifecycleScope.launch {
                if (studentId != null) {
                    db.studentDao().update(student)
                    Toast.makeText(this@StudentForm, "Student updated", Toast.LENGTH_SHORT).show()
                } else {
                    db.studentDao().insert(student)
                    Toast.makeText(this@StudentForm, "Student added", Toast.LENGTH_SHORT).show()


                }
                finish()
            }
        }
    }
}