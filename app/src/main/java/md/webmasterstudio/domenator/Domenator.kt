package md.webmasterstudio.domenator

import android.app.Application
import android.util.Log
import md.webmasterstudio.domenator.data.model.DictionaryModel

class Domenator: Application() {

    var ruDictionary = DictionaryModel()
    var roDictionary = DictionaryModel()
    var enDictionary = DictionaryModel()

    private var appLanguage = "ru"

    companion object {
        lateinit var instance: Domenator
            private set
    }

    override fun onCreate() {
        Log.d("CREATION", "App -> onCreate")
        super.onCreate()
        instance = this
    }

    init {
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