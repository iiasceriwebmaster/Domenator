package md.webmasterstudio.domenator.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import md.webmasterstudio.domenator.R

class ImageAdapter(private var imageUris: MutableList<Uri>, private val context: Context, private val onDeleteListener: (Uri) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = imageUris[position]
        // Load image into ImageView using Glide or Picasso or any other image loading library
        Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.imageView)

        holder.itemView.setOnLongClickListener {
            showDeleteDialog(imageUri)
            true
        }
    }

    private fun showDeleteDialog(imageUri: Uri) {
        AlertDialog.Builder(context)
            .setMessage("Are you sure you want to delete this image?")
            .setPositiveButton("Delete") { _, _ ->
                removeItem(imageUri)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun removeItem(imageUri: Uri) {
        imageUris.remove(imageUri)
        onDeleteListener(imageUri) // Notify ViewModel that an image is deleted
        notifyDataSetChanged()
    }

    override fun getItemCount() = imageUris.size

    fun updateData(newImageUris: MutableList<Uri>) {
        imageUris = newImageUris
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}