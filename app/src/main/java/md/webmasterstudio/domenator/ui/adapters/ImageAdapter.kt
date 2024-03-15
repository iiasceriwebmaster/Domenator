package md.webmasterstudio.domenator.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import md.webmasterstudio.domenator.R

class ImageAdapter(
    private var imageBitmaps: MutableList<Bitmap>,
    private val context: Context,
    private val onDeleteListener: (Bitmap) -> Unit
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    var isViewMode = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageBitmap = imageBitmaps[position]
        // Load image into ImageView using Glide or Picasso or any other image loading library
        Glide.with(holder.itemView)
            .load(imageBitmap)
            .into(holder.imageView)

        if (!isViewMode)
            holder.itemView.setOnLongClickListener {
                showDeleteDialog(imageBitmap)
                true
            }
    }

    private fun showDeleteDialog(imageBitmap: Bitmap) {
        AlertDialog.Builder(context)
            .setMessage("Are you sure you want to delete this image?")
            .setPositiveButton("Delete") { _, _ ->
                removeItem(imageBitmap)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun removeItem(imageBitmap: Bitmap) {
        imageBitmaps.remove(imageBitmap)
        onDeleteListener(imageBitmap) // Notify ViewModel that an image is deleted
        notifyDataSetChanged()
    }

    override fun getItemCount() = imageBitmaps.size

    fun updateData(newImageBitmaps: MutableList<Bitmap>, isViewMode: Boolean) {
        imageBitmaps = newImageBitmaps
        this.isViewMode = isViewMode
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}