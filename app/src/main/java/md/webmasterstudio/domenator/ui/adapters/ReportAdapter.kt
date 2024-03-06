package md.webmasterstudio.domenator.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import md.webmasterstudio.domenator.R


class ReportItem(val date: String, val km: String, val quantity: String, val pricePerUnit: String)

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


        val languageLiterSymbol = "L"
        val fuelTxt = "${reportItem.quantity} $languageLiterSymbol | ${reportItem.pricePerUnit} \$/$languageLiterSymbol"

        holder.reportConsumption.text = fuelTxt

        if (position == reportItems.lastIndex) {
            holder.stroke.visibility = View.INVISIBLE
            holder.grayHelperStroke.visibility = View.VISIBLE
        } else if (position == 0) {
            holder.whiteHelperStroke.visibility = View.VISIBLE
        }
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
        val stroke: View = itemView.findViewById(R.id.stroke)
        val whiteHelperStroke: View = itemView.findViewById(R.id.whiteHelperStroke)
        val grayHelperStroke: View = itemView.findViewById(R.id.grayHelperStroke)
    }

}