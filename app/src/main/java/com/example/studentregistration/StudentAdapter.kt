import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregistration.R
import com.example.studentregistration.Student

class StudentAdapter(
    private val students: List<Student>,
    private val onUpdate: (Student) -> Unit,
    private val onDelete: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById<TextView>(R.id.nameText)
        val updateBtn = view.findViewById<Button>(R.id.updateButton)
        val deleteBtn = view.findViewById<Button>(R.id.deleteButton)
        val genderTextView=view.findViewById<TextView>(R.id.genderText)
        val hobbiesTextView=view.findViewById<TextView>(R.id.hobbiesText)
        val addressTextView=view.findViewById<TextView>(R.id.addressText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.nameText.text = student.name
        holder.genderTextView.text = "Gender: ${student.gender}"
        holder.hobbiesTextView.text = "Hobbies: ${student.hobbies}"
        holder.addressTextView.text = "Address: ${student.address}"
        holder.updateBtn.setOnClickListener { onUpdate(student) }
        holder.deleteBtn.setOnClickListener { onDelete(student) }
    }

    override fun getItemCount() = students.size
}
