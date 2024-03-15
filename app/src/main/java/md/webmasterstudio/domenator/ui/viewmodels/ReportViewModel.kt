package md.webmasterstudio.domenator.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import md.webmasterstudio.domenator.data.db.dao.ReportsInfoDao
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity
import md.webmasterstudio.domenator.data.db.entity.ReportInfoEntity

class ReportViewModel(val reportsInfoDao: ReportsInfoDao) : ViewModel() {

    var reports: MutableLiveData<MutableList<ReportInfoEntity>> = MutableLiveData(mutableListOf())

    fun loadReports() {
        reports.postValue(reportsInfoDao.getAll().toMutableList())
    }

    fun deleteAll() {
        viewModelScope.launch {
            reportsInfoDao.deleteAll()
        }
    }

    fun addReport(report: ReportInfoEntity) {
        val reportss = reports.value?.toMutableList() ?: mutableListOf()
        reportss.add(0, report)
        viewModelScope.launch {
            reportsInfoDao.insert(report)
        }
        reports.value = reportss
    }

    fun updateReport(position: Int, report: ReportInfoEntity) {
        val reportss = reports.value?.toMutableList() ?: mutableListOf()
        reportss[position] = report
        viewModelScope.launch {
            reportsInfoDao.update(report)
        }
        reports.value = reportss
    }

    fun sortReports() {
        val reportss = reports.value?.toMutableList() ?: mutableListOf()
        reportss.sortByDescending { it.date }
        reports.value = reportss
    }

    fun removeReport(report: ReportInfoEntity) {
        val reportss = reports.value?.toMutableList() ?: return
        reportss.remove(report)

        viewModelScope.launch {
            reportsInfoDao.delete(report)
        }

        reports.value = reportss
    }
}
