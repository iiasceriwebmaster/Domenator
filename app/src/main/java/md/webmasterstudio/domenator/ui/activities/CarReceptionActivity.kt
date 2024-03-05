package md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.databinding.ActivityCarReceptionBinding
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.adapters.ImageAdapter
import md.webmasterstudio.domenator.ui.viewmodels.CarReceptionViewModel
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.viewutility.GridSpacingItemDecoration
import md.webmasterstudio.domenator.ui.activities.NotificationActivity

class CarReceptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarReceptionBinding
    private val viewModel: CarReceptionViewModel by viewModels()
    private lateinit var adapter: ImageAdapter
    private var PICK_IMAGE_MULTIPLE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCarReceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.leftButton.setOnClickListener {
            onBackPressed()
        }

        binding.rightButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        val spacingInPixels =
            resources.getDimensionPixelSize(R.dimen.spacing) // Change to your desired spacing
        val spanCount = 4 // Change to your desired span count
        val includeEdge =
            true // Set to true if you want to include spacing at the edges of the grid

        binding.grid.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacingInPixels,
                includeEdge
            )
        )

        // Initialize adapter
        adapter = ImageAdapter(emptyList())
        binding.grid.adapter = adapter

        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        binding.standardFab.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_MULTIPLE
            )
        }

        // Observing selected photos and documents
        viewModel.selectedPhotos.observe(this, Observer { photos ->
            // Update adapter data
            adapter.updateData(photos)
        })

        viewModel.selectedDocuments.observe(this, Observer { documents ->
            // Update adapter data
            adapter.updateData(documents)
        })

        // Observing radio button changes
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioBtnPhotos -> {
                    // Load selected photos
                    val photos = viewModel.selectedPhotos.value ?: emptyList()
                    adapter.updateData(photos)
                }

                R.id.radioBtnDocuments -> {
                    // Load selected documents
                    val documents = viewModel.selectedDocuments.value ?: emptyList()
                    adapter.updateData(documents)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {

            val isDocument = binding.radioGroup.checkedRadioButtonId == R.id.radioBtnDocuments

            if (data.clipData != null) {
                val clipData = data.clipData
                val count = clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = clipData.getItemAt(i).uri
                    viewModel.addSelectedImage(imageUri, isDocument)
                }
            } else {
                val imageUri = data.data
                viewModel.addSelectedImage(imageUri!!, isDocument)
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}
