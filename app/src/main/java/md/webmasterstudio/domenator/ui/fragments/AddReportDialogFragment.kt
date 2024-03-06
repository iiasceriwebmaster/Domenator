package md.webmasterstudio.domenator.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.ui.adapters.ReportItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddReportDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    val liveDate = MutableLiveData<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireActivity())
        val view = inflater.inflate(R.layout.fragment_dialog_add_report, null)

        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val editTextKM = view.findViewById<EditText>(R.id.editTextKM)
        val editTextFuel = view.findViewById<EditText>(R.id.editTextFuel)
        val editTextPrice = view.findViewById<EditText>(R.id.editTextPrice)

        val changeBtn = view.findViewById<Button>(R.id.pickDateBtn)



        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        editTextDate.setText(currentDate)

        liveDate.observe(this) {
            editTextDate.setText(it)
        }

        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        changeBtn.setOnClickListener {
            // Create a new instance of DatePickerDialog and return it.
            val datePicker = DatePickerDialog(requireContext(), this, year, month, day)
            datePicker.show()
        }


        builder.setView(view)
            .setTitle("Add Info")
            .setPositiveButton("Add") { _, _ ->
                // Retrieve data from EditText fields and pass it back to the activity
                val date = editTextDate.text.toString()
                val km = editTextKM.text.toString()
                val fuel = editTextFuel.text.toString()
                val price = editTextPrice.text.toString()
                val reportItem = ReportItem(date, km, fuel, price)
                // Pass the data back to the activity
                (requireActivity() as? DialogAddReportFragmentListener)?.onReportInfoAdded(reportItem)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    interface DialogAddReportFragmentListener {
        fun onReportInfoAdded(reportItem: ReportItem)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        liveDate.postValue("$dayOfMonth-$month-$year")
    }
}