package md.webmasterstudio.domenator.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.entity.ReportInfoEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddReportDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    val liveDate = MutableLiveData<String>()
    var confirmButtonName = ""
    var carId = 0L

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
            confirmButtonName = context?.resources?.getString(R.string.edit).toString()

            // Retrieve data from arguments bundle
            val km = arguments?.getString("km")
            val date = arguments?.getString("date")
            val pricePerUnit = arguments?.getString("pricePerUnit")
            val quantity = arguments?.getString("quantity")
            carId = arguments?.getLong("car_id")!!

            editTextDate.setText(date)
            editTextKM.setText(km)
            editTextFuel.setText(quantity)
            editTextPrice.setText(pricePerUnit)
        } else {
            confirmButtonName = context?.resources?.getString(R.string.add).toString()

            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            editTextDate.setText(currentDate)
        }

        editTextKM.addTextChangedListener(createTextWatcher(editTextKM))
        editTextFuel.addTextChangedListener(createTextWatcher(editTextFuel))
        editTextPrice.addTextChangedListener(createTextWatcher(editTextPrice))

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

        val addReportInfoText = context?.resources?.getString(R.string.add_report_info)
        val cancelText = context?.resources?.getString(R.string.cancel)
        val dialog = builder.setView(view)
            .setTitle(addReportInfoText)
            .setPositiveButton(confirmButtonName, null)
            .setNegativeButton(cancelText) { dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.setOnShowListener {
            val addBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            addBtn.setOnClickListener {
                // Retrieve data from EditText fields and pass it back to the activity
                val date = editTextDate.text.toString()
                val km = editTextKM.text.toString()
                val fuel = editTextFuel.text.toString()
                val price = editTextPrice.text.toString()

                if (km.isNotBlank() && fuel.isNotBlank() && price.isNotBlank()) {
                    val reportItem = ReportInfoEntity(carId = carId, date=date, speedometerValue = km.toLong(), fuelAmount = fuel.toFloat(), fuelPrice = price.toFloat())
                    // Pass the data back to the activity
                    (requireActivity() as? DialogAddReportFragmentListener)?.onReportInfoAdded(reportItem)
                    dialog.dismiss() // Dismiss the dialog after adding
                } else {
                    val pleaseFillInAllTheFieldsText =
                        context?.resources?.getString(R.string.please_fill_in_all_fields)
                    dialog.setTitle(pleaseFillInAllTheFieldsText)
                    changeErrorState(editTextKM, km.isBlank())
                    changeErrorState(editTextFuel, fuel.isBlank())
                    changeErrorState(editTextPrice, price.isBlank())

                    if (km.isBlank()) {
                        applyAnimation(editTextKM)
                    }
                    if (fuel.isBlank()) {
                        applyAnimation(editTextFuel)
                    }
                    if (price.isBlank()) {
                        applyAnimation(editTextPrice)
                    }
                }
            }
        }

        return dialog
    }
//
//    // Function to move cursor to the start of " km" suffix
//    private fun moveCursorToKmSuffix(editText: EditText) {
//        val text = editText.text.toString()
//        val kmIndex = text.indexOf(" km")
//        if (kmIndex != -1) {
//            // Move cursor to the start of " km" suffix
//            editText.setSelection(kmIndex)
//        }
//    }

    private fun createTextWatcher(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = editText.text.toString()
                changeErrorState(editText, editText.isFocused && text.isBlank())
            }
        }
    }

    fun changeErrorState(editText: EditText, isError: Boolean = true) {
        var color = requireActivity().getColor(R.color.black)
        if (isError)
            color = requireActivity().getColor(R.color.red)
        editText.compoundDrawables[0].setTint(color)
    }

    interface DialogAddReportFragmentListener {
        fun onReportInfoAdded(reportItem: ReportInfoEntity)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Create a Calendar instance and set the selected date
        val selectedDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }.time

        // Format the selected date
        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)

        // Update the liveDate LiveData with the formatted date
        liveDate.postValue(formattedDate)
    }

    private fun applyAnimation(view: View) {
        YoYo.with(Techniques.Shake)
            .duration(700)
            .playOn(view)
    }
}