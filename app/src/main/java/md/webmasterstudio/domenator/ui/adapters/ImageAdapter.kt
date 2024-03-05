package md.webmasterstudio.domenator.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import md.webmasterstudio.domenator.R

class ImageAdapter(private var files: List<Uri>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileUri = files[position]
        // Check the MIME type of the file
        val mimeType = holder.itemView.context.contentResolver.getType(fileUri)
        if (mimeType == "application/pdf") {
            // Display PDF file placeholder
            holder.imageView.setImageResource(R.drawable.baseline_picture_as_pdf_24)
        } else {
            // Display image using Glide or any other image loading library
            Glide.with(holder.itemView)
                .load(fileUri)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

    fun updateData(newFiles: List<Uri>) {
        files = newFiles
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
