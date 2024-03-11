package md.webmasterstudio.domenator.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import md.webmasterstudio.domenator.data.db.entity.ReportInfo

class ReportViewModel : ViewModel() {
    private val _addedReports = MutableLiveData<List<ReportInfo>>()

    val reports: LiveData<List<ReportInfo>>
        get() = _addedReports

    fun addReport(report: ReportInfo) {
        val reports = _addedReports.value?.toMutableList() ?: mutableListOf()
        reports.add(report)
        _addedReports.value = reports
    }

    fun removeSelectedImage(report: ReportInfo) {
        val reports = _addedReports.value?.toMutableList() ?: return
        reports.remove(report)
        _addedReports.value = reports
    }
}
