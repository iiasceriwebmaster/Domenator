package md.webmasterstudio.domenator.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import md.webmasterstudio.domenator.data.db.entity.ReportInfoEntity

class ReportViewModel() : ViewModel() {
    private val _addedReports = MutableLiveData<List<ReportInfoEntity>>()

    val reports: LiveData<List<ReportInfoEntity>>
        get() = _addedReports

    fun addReport(report: ReportInfoEntity) {
        val reports = _addedReports.value?.toMutableList() ?: mutableListOf()
        reports.add(0, report)
        _addedReports.value = reports
    }

    fun updateReport(position: Int, report: ReportInfoEntity) {
        val reports = _addedReports.value?.toMutableList() ?: mutableListOf()
        reports[position] = report
        _addedReports.value = reports
    }

    fun sortReports() {
        val reports = _addedReports.value?.toMutableList() ?: mutableListOf()
        reports.sortByDescending { it.date }
        _addedReports.value = reports
    }

    fun removeReport(report: ReportInfoEntity) {
        val reports = _addedReports.value?.toMutableList() ?: return
        reports.remove(report)
        _addedReports.value = reports
    }
}
