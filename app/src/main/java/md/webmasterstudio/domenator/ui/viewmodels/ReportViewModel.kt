package md.webmasterstudio.domenator.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import md.webmasterstudio.domenator.ui.adapters.ReportItem

class ReportViewModel : ViewModel() {
    private val _addedReports = MutableLiveData<List<ReportItem>>()

    val reports: LiveData<List<ReportItem>>
        get() = _addedReports

    fun addReport(report: ReportItem) {
        val reports = _addedReports.value?.toMutableList() ?: mutableListOf()
        reports.add(report)
        _addedReports.value = reports
    }

    fun removeSelectedImage(report: ReportItem) {
        val reports = _addedReports.value?.toMutableList() ?: return
        reports.remove(report)
        _addedReports.value = reports
    }
}
