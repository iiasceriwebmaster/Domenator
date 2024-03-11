package md.webmasterstudio.domenator.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.entity.ReportInfo

class ReportAdapter(
    private var reportItems: List<ReportInfo>,
    private val onEditClick: (Int) -> Unit
) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val reportItem = reportItems[position]
        holder.reportDate.text = reportItem.date
        holder.reportDistance.text = reportItem.speedometerValue.toString()


        val languageLiterSymbol = "L"
        val languageCurrencySymbol = "€"
        val fuelTxt =
            "${reportItem.fuelAmount} $languageLiterSymbol | ${reportItem.fuelPrice} $languageCurrencySymbol/$languageLiterSymbol"

        holder.reportConsumption.text = fuelTxt

        if (position == reportItems.lastIndex) {
            holder.stroke.visibility = View.INVISIBLE
            holder.grayHelperStroke.visibility = View.VISIBLE
        } else if (position == 0 && position != reportItems.lastIndex) {
            holder.whiteHelperStroke.visibility = View.VISIBLE
        }

        holder.editReportBtn.setOnClickListener {
            onEditClick(position)
        }
    }

    fun updateData(newReportItems: List<ReportInfo>) {
        reportItems = newReportItems
        notifyDataSetChanged()
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

        val editReportBtn: View = itemView.findViewById(R.id.editReportBtn)
    }

}