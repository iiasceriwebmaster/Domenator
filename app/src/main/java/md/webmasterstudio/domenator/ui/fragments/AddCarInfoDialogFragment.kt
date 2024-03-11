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
import md.webmasterstudio.domenator.data.db.entity.CarInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddCarInfoDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    val liveDate = MutableLiveData<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireActivity())
        val view = inflater.inflate(R.layout.fragment_dialog_add_car, null)

        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val editTextKM = view.findViewById<EditText>(R.id.editTextKM)
        val editTextLicencePlateNr = view.findViewById<EditText>(R.id.editTextLicencePlateNr)
        val changeBtn = view.findViewById<Button>(R.id.pickDateBtn)



        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        editTextDate.setText(currentDate)

        editTextKM.addTextChangedListener(createTextWatcher(editTextKM))
        editTextLicencePlateNr.addTextChangedListener(createTextWatcher(editTextLicencePlateNr))

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

        changeBtn.setOnClickListener {
            // Create a new instance of DatePickerDialog and return it.
            val datePicker = DatePickerDialog(requireContext(), this, year, month, day)
            datePicker.show()
        }

        val addCarInfoText = context?.resources?.getString(R.string.add_car_info)
        val addText = context?.resources?.getString(R.string.add)
        val cancelText = context?.resources?.getString(R.string.cancel)
        val dialog = builder.setView(view)
            .setTitle(addCarInfoText)
            .setPositiveButton(addText, null)
            .setNegativeButton(cancelText) { dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.setOnShowListener {
            val addBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            addBtn.setOnClickListener {
                val date = editTextDate.text.toString()
                val km = editTextKM.text.toString()
                val licencePlateNr = editTextLicencePlateNr.text.toString()

                // Pass the data back to the activity

                if (km.isNotBlank() && licencePlateNr.isNotBlank()) {
                    val carInfoItem = CarInfo(
                        date = date,
                        speedometerValue = km.toLong(),
                        licencePlateNr = licencePlateNr
                    )
                    // Pass the data back to the activity
                    (requireActivity() as? DialogAddCarFragmentListener)?.onCarInfoAdded(carInfoItem)
                    dialog.dismiss() // Dismiss the dialog after adding
                } else {
                    val pleaseFillInAllTheFieldsText =
                        context?.resources?.getString(R.string.please_fill_in_all_fields)
                    dialog.setTitle(pleaseFillInAllTheFieldsText)
                    changeErrorState(editTextKM, km.isBlank())
                    changeErrorState(editTextLicencePlateNr, licencePlateNr.isBlank())

                    if (km.isBlank()) {
                        applyAnimation(editTextKM)
                    }
                    if (licencePlateNr.isBlank()) {
                        applyAnimation(editTextLicencePlateNr)
                    }
                }
            }
        }


        return dialog
    }

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

    interface DialogAddCarFragmentListener {
        fun onCarInfoAdded(carInfoItem: CarInfo)
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

    fun changeErrorState(editText: EditText, isError: Boolean = true) {
        var color = requireActivity().getColor(R.color.black)
        if (isError)
            color = requireActivity().getColor(R.color.red)
        editText.compoundDrawables[0].setTint(color)
    }

    private fun applyAnimation(view: View) {
        YoYo.with(Techniques.Shake)
            .duration(700)
            .playOn(view)
    }
}