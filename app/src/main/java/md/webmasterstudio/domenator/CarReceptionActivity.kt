package md.webmasterstudio.domenator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import md.webmasterstudio.domenator.databinding.ActivityCarReceptionBinding


class CarReceptionActivity : AppCompatActivity() {

    lateinit var binding: ActivityCarReceptionBinding
    var PICK_IMAGE_MULTIPLE = 123
    val mArrayUri = ArrayList<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCarReceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.standardFab.setOnClickListener {
            // initialising intent
            // initialising intent
            val intent = Intent()

            // setting type to select to be image

            // setting type to select to be image
            intent.setType("image/*")

            // allowing multiple image to be selected

            // allowing multiple image to be selected
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_MULTIPLE
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.clipData != null) {
                val mClipData = data.clipData
                val cout = data.clipData!!.itemCount
                for (i in 0 until cout) {
                    // adding imageuri in array
                    val imageurl = data.clipData!!.getItemAt(i).uri
                    mArrayUri.add(imageurl)
                }
                // setting 1st selected image into image switcher
//                imageView.setImageURI(mArrayUri[0])
//                position = 0

                // Pass imagePaths to RecyclerView adapter
                val adapter = ImageAdapter(mArrayUri)
                binding.grid.adapter = adapter
            } else {
                val imageurl = data.data
                mArrayUri.add(imageurl!!)
//                imageView.setImageURI(mArrayUri[0])
//                position = 0

                val adapter = ImageAdapter(mArrayUri)
                binding.grid.adapter = adapter
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}