package md.webmasterstudio.domenator.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import md.webmasterstudio.domenator.R

class ReportItem(val date: String, val km: String, val quantityAndPricePerUnit: String)

class ReportAdapter(private var reportItems: List<ReportItem>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val reportItem = reportItems[position]
        holder.reportDate.text = reportItem.date
        holder.reportDistance.text = reportItem.km
        holder.reportConsumption.text = reportItem.quantityAndPricePerUnit
    }

    override fun getItemCount() = reportItems.size

//    fun updateData(newReport: ReportItem) {
//        reportItems = newImageUris
//        notifyDataSetChanged()
//    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportDate: TextView = itemView.findViewById(R.id.reportDate)
        val reportDistance: TextView = itemView.findViewById(R.id.reportDistance)
        val reportConsumption: TextView = itemView.findViewById(R.id.reportConsumption)
    }

}