package md.webmasterstudio.domenator.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import md.webmasterstudio.domenator.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


data class CarInfoItem(val date: String, val km: String, val licencePlateNr: String)

class AddCarInfoDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    val liveDate = MutableLiveData<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireActivity())
        val view = inflater.inflate(R.layout.fragment_dialog_add_car, null)

        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val editTextKM = view.findViewById<EditText>(R.id.editTextKM)
        val editTextLicencePlateNr = view.findViewById<EditText>(R.id.editTextLicencePlateNr)


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
        editTextDate.setOnClickListener {
            // Create a new instance of DatePickerDialog and return it.
            val datePicker = DatePickerDialog(requireContext(), this, year, month, day)
            datePicker.show()
        }


        builder.setView(view)
            .setTitle("Add Car Info")
            .setPositiveButton("Add") { _, _ ->
                // Retrieve data from EditText fields and pass it back to the activity
                val date = editTextDate.text.toString()
                val km = editTextKM.text.toString()
                val licencePlateNr = editTextLicencePlateNr.text.toString()
                val carInfoItem = CarInfoItem(date, km, licencePlateNr)
                // Pass the data back to the activity
                (requireActivity() as? DialogAddCarFragmentListener)?.onCarInfoAdded(carInfoItem)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    interface DialogAddCarFragmentListener {
        fun onCarInfoAdded(carInfoItem: CarInfoItem)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        liveDate.postValue("$dayOfMonth-$month-$year")
    }
}