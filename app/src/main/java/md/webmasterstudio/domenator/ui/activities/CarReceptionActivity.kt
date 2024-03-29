package md.webmasterstudio.domenator.ui.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.DomenatorDatabase
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity
import md.webmasterstudio.domenator.databinding.ActivityCarReceptionBinding
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.viewutility.GridSpacingItemDecoration
import md.webmasterstudio.domenator.ui.adapters.ImageAdapter
import md.webmasterstudio.domenator.ui.viewmodels.CarInfoViewModel
import java.io.InputStream

class CarReceptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarReceptionBinding
    private lateinit var carInfoViewModel: CarInfoViewModel
    private lateinit var appDatabase: DomenatorDatabase
    private lateinit var adapter: ImageAdapter
    private var PICK_IMAGE_MULTIPLE = 123
    private var isViewMode = false

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

        setupFonts()

        appDatabase = DomenatorDatabase.getInstance(applicationContext)
        carInfoViewModel = CarInfoViewModel(appDatabase.carInfoDao(), appDatabase.imageDao())

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
        adapter = ImageAdapter(mutableListOf(), this) {deleteBitmap ->
            carInfoViewModel.removeSelectedImage(deleteBitmap, isDocument())
        }
        binding.grid.adapter = adapter




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
        carInfoViewModel.selectedPhotos.observe(
            this@CarReceptionActivity,
            Observer { photos ->
                // Update adapter data
                adapter.updateData(photos, isViewMode)
                updateEmptyUI()
            })

        carInfoViewModel.selectedDocuments.observe(
            this@CarReceptionActivity,
            Observer { documents ->
                // Update adapter data
                adapter.updateData(documents, isViewMode)
                updateEmptyUI()
            })



        binding.saveBtn.setOnClickListener {
            val date = intent.getStringExtra("date").toString()
            val km = intent.getLongExtra("km", 0)
            val licencePlateNr = intent.getStringExtra("licencePlateNr").toString()
            carInfoViewModel.saveCarInfo(date, licencePlateNr, km, this@CarReceptionActivity) {
                val intent =
                    Intent(this@CarReceptionActivity, ReportActivity::class.java)
                startActivity(intent)
            }

//            val carPhotosUri = carInfoViewModel.selectedPhotos.value
//            val documentPhotosUri = carInfoViewModel.selectedDocuments.value
//
//            val carPhotosBase64 =
//                carPhotosUri?.map { uriToBase64(contentResolver, it) }
//            val documentPhotosBase64 =
//                documentPhotosUri?.map { uriToBase64(contentResolver, it) }
        }
        //
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var carInfoEntities: List<CarInfoEntity>
                runBlocking {  carInfoEntities = carInfoViewModel.getCarInfoEntities() }
                if (carInfoEntities.isNotEmpty()) {
                    isViewMode = true
                    carInfoViewModel.loadImages()
                }
            }
        }
        //

        // Observing radio button changes
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            updateEmptyUI()
            when (checkedId) {
                R.id.radioBtnPhotos -> {
                    // Load selected photos
                    val photos = carInfoViewModel.selectedPhotos.value ?: mutableListOf()
                    adapter.updateData(photos, isViewMode)
                }

                R.id.radioBtnDocuments -> {
                    // Load selected documents
                    val documents = carInfoViewModel.selectedDocuments.value ?: mutableListOf()
                    adapter.updateData(documents, isViewMode)
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
                    val inputStream: InputStream? = this.contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    carInfoViewModel.addSelectedImage(bitmap, isDocument())
                }
            } else {
                val imageUri = data.data
                val inputStream: InputStream? = imageUri?.let {
                    this.contentResolver.openInputStream(
                        it
                    )
                }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                carInfoViewModel.addSelectedImage(bitmap!!, isDocument())
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun isDocument(): Boolean {
        return binding.radioGroup.checkedRadioButtonId == R.id.radioBtnDocuments
    }

    private fun setupFonts() {
        binding.emptyText.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
        binding.saveBtn.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_medium))
        binding.title.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_medium))
        binding.radioBtnPhotos.setTypeface(ResourcesCompat.getFont(this, R.font.robotoserif_28pt_semibold))
        binding.radioBtnDocuments.setTypeface(ResourcesCompat.getFont(this, R.font.robotoserif_28pt_semibold))
    }

    private fun updateEmptyUI() {
        val documents = carInfoViewModel.selectedDocuments.value ?: mutableListOf()
        val photos = carInfoViewModel.selectedPhotos.value ?: mutableListOf()

        if (documents.isNotEmpty() && photos.isNotEmpty()) {
            binding.saveBtnCard.visibility = View.VISIBLE
        }

        if (isViewMode) {
            binding.saveBtnCard.visibility = View.GONE
            binding.standardFab.visibility = View.GONE
        }

        if (isDocument()) {

            if (documents.size > 0) {
                binding.emptyTextLL.visibility = View.GONE
                binding.grid.visibility = View.VISIBLE
            } else {
                binding.emptyTextLL.visibility = View.VISIBLE
                binding.grid.visibility = View.GONE
            }
        } else {

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
