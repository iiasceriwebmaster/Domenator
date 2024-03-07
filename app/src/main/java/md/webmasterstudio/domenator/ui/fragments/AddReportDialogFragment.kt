package md.webmasterstudio.domenator.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
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

        if (arguments != null) {
            // Retrieve data from arguments bundle
            val km = arguments?.getString("km")
            val date = arguments?.getString("date")
            val pricePerUnit = arguments?.getString("pricePerUnit")
            val quantity = arguments?.getString("quantity")

            editTextDate.setText(date)
            editTextKM.setText(km)
            editTextFuel.setText(quantity)
            editTextPrice.setText(pricePerUnit)
        } else {
            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            editTextDate.setText(currentDate)
        }

        // Text change listeners for EditText fields
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Check if all EditText fields are filled
                val date = editTextDate.text.toString()
                val km = editTextKM.text.toString()
                val fuel = editTextFuel.text.toString()
                val price = editTextPrice.text.toString()
                changeErrorState(editTextKM, editTextKM.isFocused && km.isBlank())
                changeErrorState(editTextFuel, editTextFuel.isFocused && fuel.isBlank())
                changeErrorState(editTextPrice, editTextPrice.isFocused && price.isBlank())
            }
        }

        editTextKM.addTextChangedListener(textWatcher)
        editTextFuel.addTextChangedListener(textWatcher)
        editTextPrice.addTextChangedListener(textWatcher)

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
            .setTitle("Add Report Info")
            .setPositiveButton("Add") { dialog, _ ->

                // Retrieve data from EditText fields and pass it back to the activity
                val date = editTextDate.text.toString()
                val km = editTextKM.text.toString()
                val fuel = editTextFuel.text.toString()
                val price = editTextPrice.text.toString()

                if (km.isNotBlank() && fuel.isNotBlank() && price.isNotBlank()) {
                    val reportItem = ReportItem(date, km, fuel, price)
                    // Pass the data back to the activity
                    (requireActivity() as? DialogAddReportFragmentListener)?.onReportInfoAdded(reportItem)
                    dialog.dismiss() // Dismiss the dialog after adding
                } else {
                    // Show a message indicating that at least one field is required
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    fun changeErrorState(editText: EditText, isError: Boolean = true) {
        var color = requireActivity().getColor(R.color.black)
        if (isError)
            color = requireActivity().getColor(R.color.red)
        editText.compoundDrawables[0].setTint(color)
    }

    interface DialogAddReportFragmentListener {
        fun onReportInfoAdded(reportItem: ReportItem)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        liveDate.postValue("$dayOfMonth-$month-$year")
    }
}