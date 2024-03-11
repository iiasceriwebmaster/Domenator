package md.webmasterstudio.domenator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.databinding.ActivityCarReceptionBinding
import md.webmasterstudio.domenator.ui.adapters.ImageAdapter
import md.webmasterstudio.domenator.ui.viewmodels.CarReceptionViewModel
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.viewutility.GridSpacingItemDecoration

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

        val date = intent.getStringExtra("date")
        val km = intent.getLongExtra("km", 0)
        val licencePlateNr = intent.getStringExtra("licencePlateNr")

        binding

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
        adapter = ImageAdapter(mutableListOf(), this) {deletedUri ->
            viewModel.removeSelectedImage(deletedUri, isDocument())
        }
        binding.grid.adapter = adapter

        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)

            intent.putExtra("km", km)
            intent.putExtra("date", date)
            intent.putExtra("licencePlateNr", licencePlateNr)
            
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
            updateEmptyUI()
        })

        viewModel.selectedDocuments.observe(this, Observer { documents ->
            // Update adapter data
            adapter.updateData(documents)
            updateEmptyUI()
        })

        // Observing radio button changes
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            updateEmptyUI()
            when (checkedId) {
                R.id.radioBtnPhotos -> {
                    // Load selected photos
                    val photos = viewModel.selectedPhotos.value ?: mutableListOf()
                    adapter.updateData(photos)
                }

                R.id.radioBtnDocuments -> {
                    // Load selected documents
                    val documents = viewModel.selectedDocuments.value ?: mutableListOf()
                    adapter.updateData(documents)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {

            if (data.clipData != null) {
                val clipData = data.clipData
                val count = clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = clipData.getItemAt(i).uri
                    viewModel.addSelectedImage(imageUri, isDocument())
                }
            } else {
                val imageUri = data.data
                viewModel.addSelectedImage(imageUri!!, isDocument())
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun isDocument(): Boolean {
        return binding.radioGroup.checkedRadioButtonId == R.id.radioBtnDocuments
    }

    private fun updateEmptyUI() {
        if (isDocument()) {
            val documents = viewModel.selectedDocuments.value ?: mutableListOf()
            if (documents.size > 0) {
                binding.emptyTextLL.visibility = View.GONE
                binding.grid.visibility = View.VISIBLE
            } else {
                binding.emptyTextLL.visibility = View.VISIBLE
                binding.grid.visibility = View.GONE
            }
        } else {
            val photos = viewModel.selectedPhotos.value ?: mutableListOf()
            if (photos.size > 0) {
                binding.emptyTextLL.visibility = View.GONE
                binding.grid.visibility = View.VISIBLE
            } else {
                binding.emptyTextLL.visibility = View.VISIBLE
                binding.grid.visibility = View.GONE
            }
        }
    }
}
