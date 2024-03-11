package md.webmasterstudio.domenator

import android.app.Application
import android.util.Log
import md.webmasterstudio.domenator.data.model.DictionaryModel

class Domenator: Application() {

    private var ruDictionary = DictionaryModel()
    private var roDictionary = DictionaryModel()
    private var enDictionary = DictionaryModel()

    private var appLanguage = "ru"

    companion object {
        lateinit var instance: Domenator
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getDictionary(): DictionaryModel {
        Log.d("CREATION", "Domenator -> DictionaryModel -> getDictionary $appLanguage")
        Log.d("CREATION", "Domenator -> DictionaryModel -> getDictionary 2 $ruDictionary")
        return when (appLanguage) {
            "ru" -> ruDictionary
            "ro" -> roDictionary
            "en" -> enDictionary

            else -> ruDictionary
        }
    }

}