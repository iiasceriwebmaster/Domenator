package md.webmasterstudio.domenator

import android.app.Application
import android.content.Context
import android.util.Log
import md.webmasterstudio.domenator.data.model.DictionaryModel

class App: Application() {

    private var ruDictionary = DictionaryModel()
    private var roDictionary = DictionaryModel()
    private var enDictionary = DictionaryModel()
    private var APP_LANGUAGE = "ru"

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getDictionary(): DictionaryModel {
        Log.d("CREATION", "App -> DictionaryModel -> getDictionary $APP_LANGUAGE")
        Log.d("CREATION", "App -> DictionaryModel -> getDictionary 2 $ruDictionary")
        return when (APP_LANGUAGE) {
            "ru" -> ruDictionary
            "ro" -> roDictionary
            "en" -> enDictionary

            else -> ruDictionary
        }
    }

}