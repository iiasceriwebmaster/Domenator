package md.webmasterstudio.domenator

import android.app.Application
import android.util.Log
import md.webmasterstudio.domenator.data.model.DictionaryModel

class App: Application() {

    private var ruDictionary = DictionaryModel()
    private var roDictionary = DictionaryModel()
    private var enDictionary = DictionaryModel()

    private var appLanguage = "ru"

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getDictionary(): DictionaryModel {
        Log.d("CREATION", "App -> DictionaryModel -> getDictionary $appLanguage")
        Log.d("CREATION", "App -> DictionaryModel -> getDictionary 2 $ruDictionary")
        return when (appLanguage) {
            "ru" -> ruDictionary
            "ro" -> roDictionary
            "en" -> enDictionary

            else -> ruDictionary
        }
    }

}