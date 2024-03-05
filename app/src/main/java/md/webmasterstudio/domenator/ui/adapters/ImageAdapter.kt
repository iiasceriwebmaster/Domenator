package md.webmasterstudio.domenator.md.webmasterstudio.domenator.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import md.webmasterstudio.domenator.R

class ImageAdapter(private var imageUris: List<Uri>) :
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
    }

    override fun getItemCount() = imageUris.size

    fun updateData(newImageUris: List<Uri>) {
        imageUris = newImageUris
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}