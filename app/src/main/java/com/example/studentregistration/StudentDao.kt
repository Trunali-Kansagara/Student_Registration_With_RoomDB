package com.example.studentregistration

import androidx.room.*

@Dao
interface StudentDao {

    // Get all students
    @Query("SELECT * FROM students")
    suspend fun getAll(): List<Student>

    // Insert a new student
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    // Update an existing student
    @Update
    suspend fun update(student: Student)

    // Delete a student
    @Delete
    suspend fun delete(student: Student)

    // (Optional) Get a student by ID
    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): Student?
}
